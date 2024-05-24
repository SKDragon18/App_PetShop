package com.example.petshopapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class PetShopLogin extends AppCompatActivity {
    private EditText edtUsername, edtPassword;
    private Button btnLogin, btnRegister;
    private CheckBox cbGhiNho;

    //Đối tượng lưu thông tin vào file
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public void init(){
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
        
        cbGhiNho.setChecked(sharedPreferences.getBoolean("saveStatus",false));
        if (sharedPreferences.getBoolean("saveStatus",false)){
            edtUsername.setText(sharedPreferences.getString("username",""));
            edtPassword.setText(sharedPreferences.getString("password",""));
        }


    }
    public void setEvent(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Kiểm tra
                String username=edtUsername.getText().toString();
                String password=edtPassword.getText().toString();
                String role=username;

                if(username.isEmpty()||password.isEmpty()){
                    Toast.makeText(PetShopLogin.this,"Mời nhập đầy đủ thông tin",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!role.equals("admin")&&!role.equals("employee")&&!role.equals("customer")&&!role.equals("manager")){
                    Toast.makeText(PetShopLogin.this,"Không có phân quyền",Toast.LENGTH_SHORT).show();
                    return;
                }

                //Lưu trữ thông tin

                boolean click= cbGhiNho.isChecked();

                editor.putString("username",username);
                editor.putString("password",password);
                editor.putString("role",role);
                editor.putBoolean("saveStatus",click);//Lần sau sẽ load vào input username nếu có click

                editor.commit();

                //Chuyển trang home
                Intent newIntent=new Intent(PetShopLogin.this, PetShopMain.class);
                startActivity(newIntent);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_shop_login);
        init();
        setEvent();
    }
}