package com.example.petshopapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.factory.FragmentFactoryCustom;
import com.example.petshopapp.fragment.HomeScreen;
import com.example.petshopapp.fragment.adapter.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class PetShopMain extends AppCompatActivity{
    //main_0: nơi bắt đầu giao diện
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    LinearLayout llFragment;
    //bar
    LinearLayout llNavigation;

    //main: giao diện được nối thể hiện thành phần chính
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;

    //nav_header
    private TextView tvNameNav, tvEmailNav, tvRole;

    //menu
    Menu menu;

    private Toolbar toolbar;
    //Factory
    FragmentFactoryCustom fragmentFactoryCustom;

    //Info
    SharedPreferences sharedPreferences;
    String name;
    String role;
    //Hỗ trợ giao diện
    List<Integer> idList;//Danh sách id các thẻ ở bottom navigation theo role
    List<String> tagList;//Danh sách tên các thẻ ở bottom navigation theo role

    void setInit(){

        //SharedPreferences: Lưu trữ thông tin bằng file trong điện thoại
        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key),MODE_PRIVATE);

        //Lấy thông tin
        name= sharedPreferences.getString("username","");
        role= sharedPreferences.getString("role","");

        //factory
        fragmentFactoryCustom=new FragmentFactoryCustom();
        idList=fragmentFactoryCustom.getIdArray(role);
        tagList=fragmentFactoryCustom.getTagArray(role);

        //main_0
        drawer=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.navigation_view);
        toolbar=findViewById(R.id.toolbar);

        //bar
        llNavigation=findViewById(R.id.llNavigation);
        llFragment=findViewById(R.id.llFlagment);
        llFragment.setVisibility(View.GONE);

        //main
        viewPager =findViewById(R.id.view_pager);
        bottomNavigationView=findViewById(R.id.bottom_navigation);

        //menu
        MenuItem menuItem;

        //for navigation bottom
        //Phân menu navigation bottom theo role
        if(role.equals(fragmentFactoryCustom.getRole(3))){
            bottomNavigationView.getMenu().clear();
            bottomNavigationView.inflateMenu(R.menu.menu_bottom_navigation_manager);
        }
        else if(role.equals(fragmentFactoryCustom.getRole(2))){
            bottomNavigationView.getMenu().clear();
            bottomNavigationView.inflateMenu(R.menu.menu_bottom_navigation_admin);
        }
        else if(role.equals(fragmentFactoryCustom.getRole(1))){
            bottomNavigationView.getMenu().clear();
            bottomNavigationView.inflateMenu(R.menu.menu_bottom_navigation_employee);
        }

        //Duyệt menu bật những item trong danh sách của role
        //và tắt những item không trong danh sách
        //Tác dụng: giúp tránh tình trạng hiển thị các chức năng không được khai báo
        //trong factory mà xuất hiện trong xml menu
        menu=bottomNavigationView.getMenu();
        for(int i =0;i<menu.size();i++){
            menuItem=menu.getItem(i);
            if(idList.contains(menuItem.getItemId())){
                menuItem.setVisible(true);
            }
            else{
                menuItem.setVisible(false);
            }
        }

        //for navigation tool
        menu = navigationView.getMenu();

        if(role.equals(fragmentFactoryCustom.getRole(2))){
            int groupId= R.id.groupAdmin;
            for(int i =0;i<menu.size();i++){
                menuItem=menu.getItem(i);
                if(menuItem.getGroupId()==groupId){
                    menuItem.setVisible(false);
                }
            }
        }



        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//ẩn app name mặc định

        ActionBarDrawerToggle toggle =new ActionBarDrawerToggle(
                this, drawer, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState();

//        các trường
        tvNameNav=navigationView.getHeaderView(0).findViewById(R.id.tvNameNav);
        tvEmailNav=navigationView.getHeaderView(0).findViewById(R.id.tvEmailNav);
        tvRole=navigationView.getHeaderView(0).findViewById(R.id.tvRole);

//        Gán view
        tvNameNav.setText(name);
        tvEmailNav.setText(name+"@gmail.com");
        tvRole.setText(role);
    }

    void setEvent(){

        ViewPagerAdapter adapter= new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, tagList);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().findItem(idList.get(position)).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                viewPager.setCurrentItem(idList.indexOf(id));
                return true;
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                llNavigation.setVisibility(View.VISIBLE);
                llFragment.setVisibility(View.GONE);
                if(id==R.id.nav_home){
                    viewPager.setCurrentItem(tagList.indexOf("home"));
                }
                else if(id==R.id.nav_cart){
                    llNavigation.setVisibility(View.GONE);
                    llFragment.setVisibility(View.VISIBLE);
                    Fragment fragment=new HomeScreen();
                    FragmentManager fragmentManager =getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment_content_main,fragment);
                    fragmentTransaction.commit();
                }
                else if(id==R.id.nav_user){
                    viewPager.setCurrentItem(tagList.indexOf("user"));
                }
                else if(id==R.id.log_out){
                    if (!sharedPreferences.getBoolean("saveStatus",false)){
                        sharedPreferences.edit().remove("username").apply();
                        sharedPreferences.edit().remove("password").apply();
                        sharedPreferences.edit().remove("role").apply();
                    }
                    ApiClient.setAuToken("");//giải phóng token
                    finish();
                }

                //Tắt toolbar khi chọn xong trên menu
                if(drawer.isDrawerOpen(GravityCompat.START)){
                    drawer.closeDrawer(GravityCompat.START);
                }
                return true;
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_shop_main_0);
        setInit();
        setEvent();
    }

    @Override
    public void onBackPressed(){
        //Tắt toolbar khi nhẩn phím trả về mặc định
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

}