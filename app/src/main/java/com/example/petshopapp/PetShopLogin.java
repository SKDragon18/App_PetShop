package com.example.petshopapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.apiservice.DangNhapService;
import com.example.petshopapp.factory.FragmentFactoryCustom;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.ThongTinDangNhap;
import com.example.petshopapp.model.ThongTinPhanHoiDangNhap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PetShopLogin extends AppCompatActivity {
    //View
    private EditText edtUsername, edtPassword;
    private Button btnLogin, btnRegister;
    private CheckBox cbGhiNho;

    //Đối tượng lưu thông tin vào file
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    //Api
    DangNhapService dangNhapService;

    //Factory
    FragmentFactoryCustom fragmentFactoryCustom;

    public void init(){
        //Fractory
        fragmentFactoryCustom=new FragmentFactoryCustom();
        //edittext
        edtUsername=findViewById(R.id.edtUsername);
        edtPassword=findViewById(R.id.edtPassword);
        //button
        btnLogin=findViewById(R.id.btnLogin);
        btnRegister=findViewById(R.id.btnRegister);
        //checkbox
        cbGhiNho=findViewById(R.id.cbGhiNho);

        sharedPreferences = getSharedPreferences(
                getString(R.string.preference_file_key),MODE_PRIVATE);
        editor =  sharedPreferences.edit();
    }
    public void setEvent(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Kiểm tra
                String username=edtUsername.getText().toString();
                String password=edtPassword.getText().toString();
                if(username.isEmpty()||password.isEmpty()){
                    Toast.makeText(PetShopLogin.this,"Mời nhập đầy đủ thông tin",Toast.LENGTH_SHORT).show();
                    return;
                }
                //Gọi api đăng nhập
                dangNhap(username, password);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent=new Intent(PetShopLogin.this, PetShopRegister.class);
                startActivity(newIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cbGhiNho.setChecked(sharedPreferences.getBoolean("saveStatus",false));
        edtUsername.setText(sharedPreferences.getString("username",""));
        edtPassword.setText(sharedPreferences.getString("password",""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_shop_login);

        Retrofit retrofit = ApiClient.getClient();
        dangNhapService =retrofit.create(DangNhapService.class);

        init();
        setEvent();
    }

    private void dangNhap(String username, String password){
        dangNhapService.checkLogin(new ThongTinDangNhap(username,password)).enqueue(new Callback<ThongTinPhanHoiDangNhap>() {
            @Override
            public void onResponse(Call<ThongTinPhanHoiDangNhap> call, Response<ThongTinPhanHoiDangNhap> response) {
                if(response.code()==200){
                    ThongTinPhanHoiDangNhap thongTinPhanHoiDangNhap = response.body();

                    //Kiểm tra quyền
                    String role= thongTinPhanHoiDangNhap.getQuyen();
                    if(!fragmentFactoryCustom.getRoleList().contains(role)){
                        Toast.makeText(PetShopLogin.this,"Không có phân quyền",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //Lưu trữ thông tin vào file
                    boolean click= cbGhiNho.isChecked();
                    editor.putString("username",thongTinPhanHoiDangNhap.getTenDangNhap());
                    editor.putString("password",password);
                    editor.putString("role",role);
                    editor.putBoolean("saveStatus",click);//Lần sau sẽ load vào input username nếu có click
                    editor.commit();

                    //Lưu trữ thông tin token vào lớp ApiClient
                    ApiClient.setAuToken(thongTinPhanHoiDangNhap.getToken());

                    //Chuyển trang home
                    Intent newIntent=new Intent(PetShopLogin.this, PetShopMain.class);
                    startActivity(newIntent);
                }
                else{
                    try {
                        int code = response.code();
                        String message = response.message();
                        String error = response.errorBody().string();
                        SendMessage.sendMessageFail(PetShopLogin.this,code,error,message);
                    } catch (Exception e) {
                        SendMessage.sendCatch(PetShopLogin.this,e.getMessage());
                        return;
                    }
                }
            }
            @Override
            public void onFailure(Call<ThongTinPhanHoiDangNhap> call, Throwable throwable) {
                SendMessage.sendApiFail(PetShopLogin.this,throwable);
            }
        });
    }
}