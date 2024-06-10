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
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.apiservice.HinhAnhService;
import com.example.petshopapp.api.apiservice.KhachHangService;
import com.example.petshopapp.api.apiservice.NhanVienService;
import com.example.petshopapp.factory.FragmentFactoryCustom;
import com.example.petshopapp.fragment.HomeScreen;
import com.example.petshopapp.fragment.adapter.ViewPagerAdapter;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.HinhAnh;
import com.example.petshopapp.model.KhachHang;
import com.example.petshopapp.model.NhanVien;
import com.example.petshopapp.tools.ImageInteract;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetShopMain extends AppCompatActivity{
    //main_0: nơi bắt đầu giao diện
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private LinearLayout llFragment;
    //bar
    private LinearLayout llNavigation;

    //main: giao diện được nối thể hiện thành phần chính
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;

    //nav_header
    private TextView tvNameNav, tvEmailNav, tvRole, tvUsername;
    private ImageView ivAvatar;

    //menu
    private Menu menu;

    private Toolbar toolbar;
    //Factory
    private FragmentFactoryCustom fragmentFactoryCustom;

    //Info
    private SharedPreferences sharedPreferences;
    private String username;
    private String role;
    private NhanVien nhanVien;
    private KhachHang khachHang;


    //Hỗ trợ giao diện
    private List<Integer> idList;//Danh sách id các thẻ ở bottom navigation theo role
    private List<String> tagList;//Danh sách tên các thẻ ở bottom navigation theo role
    //Api
    private NhanVienService nhanVienService;
    private KhachHangService khachHangService;
    private HinhAnhService hinhAnhService;


    //Chương trình
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
    void setInit(){

        //SharedPreferences: Lưu trữ thông tin bằng file trong điện thoại
        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key),MODE_PRIVATE);

        //Lấy thông tin
        username= sharedPreferences.getString("username","");
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

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//ẩn app name mặc định

        ActionBarDrawerToggle toggle =new ActionBarDrawerToggle(
                this, drawer, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Set api
        ApiClient apiClient = ApiClient.getApiClient();
        nhanVienService = apiClient.getRetrofit().create(NhanVienService.class);
        khachHangService = apiClient.getRetrofit().create(KhachHangService.class);
        hinhAnhService = apiClient.getRetrofit().create(HinhAnhService.class);
//        các trường
        tvNameNav=navigationView.getHeaderView(0).findViewById(R.id.tvNameNav);
        tvEmailNav=navigationView.getHeaderView(0).findViewById(R.id.tvEmailNav);
        tvRole=navigationView.getHeaderView(0).findViewById(R.id.tvRole);
        tvUsername = navigationView.getHeaderView(0).findViewById(R.id.tvUsername);
        ivAvatar = navigationView.getHeaderView(0).findViewById(R.id.ivAvatar);
        //Thông tin chi tiết
        if(role.equals(fragmentFactoryCustom.getRoleList().get(0))){
            DocDLKhachHang(username);
        }
        else{
            DocDLNhanVien(username);
        }
    }

    private void updateInfoHeader(){
        //        Gán view
        if(nhanVien!=null){
            String hoTen= nhanVien.getHo()+" "+nhanVien.getTen();
            String taiKhoan = "Tài khoản: " + username;
            String email = "Email: "+nhanVien.getEmail();
            String quyen = "Bạn đang đăng nhập bằng quyền: "+ role;
            tvNameNav.setText(hoTen);
            tvEmailNav.setText(email);
            tvRole.setText(quyen);
            tvUsername.setText(taiKhoan);
        }
        else if(khachHang!=null){
            String hoTen= khachHang.getHo()+" "+khachHang.getTen();
            String taiKhoan = "Tài khoản: " + username;
            String email = "Email: "+khachHang.getEmail();
            String quyen = "Bạn đang đăng nhập bằng quyền: "+ role;
            tvNameNav.setText(hoTen);
            tvEmailNav.setText(email);
            tvRole.setText(quyen);
            tvUsername.setText(taiKhoan);
        }

    }

    void setEvent(){
        ViewPagerAdapter adapter= new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, tagList);
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

                else if(id==R.id.nav_user){
                    if(role.equals(fragmentFactoryCustom.getRole(0)))viewPager.setCurrentItem(tagList.indexOf("user"));
                    else{
                        viewPager.setCurrentItem(tagList.indexOf("user2"));
                    }
                }
                else if(id==R.id.log_out){
                    if (!sharedPreferences.getBoolean("saveStatus",false)){
                        sharedPreferences.edit().remove("username").apply();
                        sharedPreferences.edit().remove("password").apply();
                        sharedPreferences.edit().remove("role").apply();
                    }
                    ApiClient apiClient = ApiClient.getApiClient();
                    apiClient.setAuToken("");
                    apiClient.deleteRetrofit();
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
    public void changeFragment(Fragment fragment){
        llNavigation.setVisibility(View.GONE);
        llFragment.setVisibility(View.VISIBLE);
        FragmentManager fragmentManager =getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main,fragment);
//        fragmentTransaction.addToBackStack("");
        fragmentTransaction.commit();
    }

    private void DocDLKhachHang(String username){
        khachHangService.getOneById(username).enqueue(new Callback<KhachHang>() {
            @Override
            public void onResponse(Call<KhachHang> call, Response<KhachHang> response) {
                if(response.code()==200){
                    khachHang = response.body();
                    updateInfoHeader();
                    getImage();
                }
                else{
                    try {
                        int code = response.code();
                        String message = response.message();
                        String error = response.errorBody().string();
                        SendMessage.sendMessageFail(PetShopMain.this,code,error,message);
                    } catch (Exception e) {
                        SendMessage.sendCatch(PetShopMain.this,e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<KhachHang> call, Throwable throwable) {
                SendMessage.sendApiFail(PetShopMain.this,throwable);
            }
        });
    }

    private void DocDLNhanVien(String username){
        nhanVienService.getOneById(username).enqueue(new Callback<NhanVien>() {
            @Override
            public void onResponse(Call<NhanVien> call, Response<NhanVien> response) {
                if(response.code()==200){
                    nhanVien = response.body();
                    updateInfoHeader();
                    getImage();
                }
                else{
                    try {
                        int code = response.code();
                        String message = response.message();
                        String error = response.errorBody().string();
                        SendMessage.sendMessageFail(PetShopMain.this,code,error,message);
                    } catch (Exception e) {
                        SendMessage.sendCatch(PetShopMain.this,e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<NhanVien> call, Throwable throwable) {
                SendMessage.sendApiFail(PetShopMain.this,throwable);
            }
        });
    }

    private void getImage(){
        long idHinh = -1;
        if(khachHang!=null && khachHang.getHinhAnh()!=null&&khachHang.getHinhAnh().size()!=0) {
            idHinh= khachHang.getHinhAnh().get(0);
        }
        else if(nhanVien!=null && nhanVien.getHinhAnh()!=null&&nhanVien.getHinhAnh().size()!=0){
            idHinh = nhanVien.getHinhAnh().get(0);
        }
        else{
            return;
        }
        hinhAnhService.getImage(new long[]{idHinh}).enqueue(new Callback<List<HinhAnh>>() {
            @Override
            public void onResponse(Call<List<HinhAnh>> call, Response<List<HinhAnh>> response) {
                try{
                    if(response.code() == 200){
                        List<HinhAnh> list = response.body();
                        String source = list.get(0).getSource();
                        Bitmap bitmap= ImageInteract.convertStringToBitmap(source);
                        if(bitmap == null){
                            Toast.makeText(PetShopMain.this,"Bitmap null",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(ivAvatar != null)ivAvatar.setImageBitmap(bitmap);
                    }
                    else{
                        try {
                            int code = response.code();
                            String message = response.message();
                            String error = response.errorBody().string();
                            SendMessage.sendMessageFail(PetShopMain.this,code,error,message);
                        } catch (Exception e) {
                            SendMessage.sendCatch(PetShopMain.this,e.getMessage());
                        }
                    }
                }
                catch (Exception e){
                    SendMessage.sendCatch(PetShopMain.this,e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<List<HinhAnh>> call, Throwable throwable) {
                SendMessage.sendApiFail(PetShopMain.this,throwable);
            }
        });
    }
}