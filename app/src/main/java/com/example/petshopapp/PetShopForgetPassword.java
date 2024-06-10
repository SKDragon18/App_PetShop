package com.example.petshopapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.apiservice.QuenMatKhauService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.TaiKhoan;
import com.example.petshopapp.model.ThongTinXacNhan;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PetShopForgetPassword extends AppCompatActivity {
    EditText edtPasswordMoi, edtPasswordNhapLai, edtUsername, edtMaXacNhan;
    Button btnSend,btnConfirm, btnUpdate;

    //Api
    QuenMatKhauService quenMatKhauService;
    String maLuu=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_shop_forget_password);
        ApiClient apiClient = ApiClient.getApiClient();
        quenMatKhauService = apiClient.getRetrofit().create(QuenMatKhauService.class);
        setInit();
        setEvent();
    }
    private void setInit(){
        edtPasswordMoi = findViewById(R.id.edtPasswordMoi);
        edtPasswordNhapLai = findViewById(R.id.edtPasswordNhapLai);
        edtUsername = findViewById(R.id.edtUsername);
        edtMaXacNhan = findViewById(R.id.edtMaXacNhan);

        btnSend = findViewById(R.id.btnSend);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnConfirm = findViewById(R.id.btnConfirm);
    }
    private void close(){
        edtMaXacNhan.setEnabled(false);
        edtPasswordMoi.setEnabled(false);
        edtPasswordNhapLai.setEnabled(false);

        btnConfirm.setEnabled(false);
        btnUpdate.setEnabled(false);
    }
    private void setEvent(){
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
                String username = edtUsername.getText().toString();
                if(username.isEmpty()){
                    edtUsername.setError("Mời nhập username muốn khôi phục");
                    return;
                }
                RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"),username);
                quenMatKhauService.sendCode(requestBody).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.code()==200){
                            try {
                                maLuu= response.body().string();
                            }catch(Exception e){
                                SendMessage.sendCatch(PetShopForgetPassword.this,e.getMessage());
                            }
                            Toast.makeText(PetShopForgetPassword.this, "Đã gửi mã xác nhận đến email của bạn",Toast.LENGTH_SHORT).show();
                            edtMaXacNhan.setEnabled(true);
                            btnConfirm.setEnabled(true);
                        }
                        else{
                            try {
                                int code = response.code();
                                String message = response.message();
                                String error = response.errorBody().string();
                                SendMessage.sendMessageFail(PetShopForgetPassword.this,code,error,message);
                            } catch (Exception e) {
                                SendMessage.sendCatch(PetShopForgetPassword.this,e.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                        SendMessage.sendApiFail(PetShopForgetPassword.this,throwable);
                    }
                });
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();
                String maXacNhan = edtMaXacNhan.getText().toString();
                if(username.isEmpty()){
                    edtUsername.setError("Mời nhập username muốn khôi phục");
                    return;
                }
                if(maXacNhan.isEmpty()){
                    edtMaXacNhan.setError("Mời nhập mã xác nhận");
                    return;
                }
                if(maLuu!=null&&maXacNhan.equals(maLuu)){
                    edtMaXacNhan.setEnabled(false);
                    btnConfirm.setEnabled(false);

                    edtPasswordMoi.setEnabled(true);
                    edtPasswordNhapLai.setEnabled(true);
                    btnUpdate.setEnabled(true);
                    Toast.makeText(PetShopForgetPassword.this, "Xác nhận thành công",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();
                String passwordMoi =edtPasswordMoi.getText().toString();
                String passwordNhapLai =edtPasswordNhapLai.getText().toString();
                if(username.isEmpty()){
                    edtUsername.setError("Mời nhập username muốn khôi phục");
                    return;
                }
                if(passwordMoi.isEmpty()){
                    edtPasswordMoi.setError("Mời nhập password mới");
                    return;
                }
                if(passwordNhapLai.isEmpty()){
                    edtPasswordMoi.setError("Mời nhập lại password như trên");
                    return;
                }
                if(!passwordMoi.equals(passwordNhapLai)){
                    Toast.makeText(PetShopForgetPassword.this,
                            "Mật khẩu mới không khớp với nhập lại",Toast.LENGTH_SHORT).show();
                    return;
                }
                TaiKhoan taiKhoan =new TaiKhoan();
                taiKhoan.setTenDangNhap(username);
                taiKhoan.setMatKhau(passwordMoi);
                quenMatKhauService.updatePassword(taiKhoan).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.code()==200){
                            try{
                                String result = response.body().string();
                                Toast.makeText(PetShopForgetPassword.this, result,Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            catch (Exception e){
                                SendMessage.sendCatch(PetShopForgetPassword.this,e.getMessage());
                            }
                        }
                        else{
                            try {
                                int code = response.code();
                                String message = response.message();
                                String error = response.errorBody().string();
                                SendMessage.sendMessageFail(PetShopForgetPassword.this,code,error,message);
                            } catch (Exception e) {
                                SendMessage.sendCatch(PetShopForgetPassword.this,e.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                        SendMessage.sendApiFail(PetShopForgetPassword.this,throwable);
                    }
                });
            }
        });
    }
}