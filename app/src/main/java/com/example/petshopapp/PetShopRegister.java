package com.example.petshopapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.apiservice.DangKyService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.ThongTinDangKy;
import com.example.petshopapp.model.ThongTinXacNhan;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PetShopRegister extends AppCompatActivity {
    //View
    EditText edtHo, edtTen, edtUsername, edtPassword, edtPasswordNhapLai, edtCCCD;
    EditText edtSDT, edtEmail;
    Button btnRegister;

    //Api
    DangKyService dangKyService;
    //Data
    ThongTinDangKy thongTinDangKy = new ThongTinDangKy();
    String maXacNhan="";

    private void confirmAccount(Dialog dialog, String tenDangNhap, String maXacNhan){
        ThongTinXacNhan thongTinXacNhan = new ThongTinXacNhan(tenDangNhap,maXacNhan);
        dangKyService.confirm(thongTinXacNhan).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()==200){
                    try {
                        String result = response.body().string();
                        Toast.makeText(PetShopRegister.this,result, Toast.LENGTH_SHORT).show();
                        if(result.equals("Xác nhận thành công, đăng nhập để tiếp tục")||result.equals("Tài khoản đã được xác nhận")){
                            dialog.dismiss();
                            finish();
                        }

                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
                else{
                    try {
                        int code = response.code();
                        String message = response.message();
                        String error = response.errorBody().string();
                        SendMessage.sendMessageFail(PetShopRegister.this,code,error,message);
                    } catch (Exception e) {
                        SendMessage.sendCatch(PetShopRegister.this,e.getMessage());
                        return;
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                SendMessage.sendApiFail(PetShopRegister.this,throwable);
            }
        });
    }

    private void openConfirmDialog(int gravity){
        final Dialog dialog = new Dialog(PetShopRegister.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirm_code_delete);

        Window window = dialog.getWindow();
        if(window == null)return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity=gravity;
        window.setAttributes(windowAttributes);

        if(Gravity.BOTTOM == gravity){
            dialog.setCancelable(false);
        }
        else{
            dialog.setCancelable(true);
        }

        EditText edtMaXacNhan = dialog.findViewById(R.id.edtMaXacNhan);
        Button btnAgain = dialog.findViewById(R.id.btnAgain);
        Button btnConfirm = dialog.findViewById(R.id.btnConfirm);
        Button btnCancel= dialog.findViewById(R.id.btnCancel);

        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangKyService.getAgain(thongTinDangKy).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.code()==200){
                            try {
                                maXacNhan = response.body().string();
                                Toast.makeText(PetShopRegister.this, "Đã gửi lại mã",Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                SendMessage.sendCatch(PetShopRegister.this,e.getMessage());
                            }
                        }
                        else{
                            try {
                                int code = response.code();
                                String message = response.message();
                                String error = response.errorBody().string();
                                SendMessage.sendMessageFail(PetShopRegister.this,code,error,message);
                            } catch (Exception e) {
                                SendMessage.sendCatch(PetShopRegister.this,e.getMessage());
                                return;
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                        SendMessage.sendApiFail(PetShopRegister.this,throwable);
                    }
                });
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmAccount(dialog,thongTinDangKy.getTenDangNhap(),edtMaXacNhan.getText().toString());
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void setInit(){
        edtHo=findViewById(R.id.edtHo);
        edtTen=findViewById(R.id.edtTen);
        edtUsername=findViewById(R.id.edtUsername);
        edtPassword=findViewById(R.id.edtPassword);
        edtPasswordNhapLai=findViewById(R.id.edtPasswordNhapLai);
        edtCCCD=findViewById(R.id.edtCCCD);
        edtSDT=findViewById(R.id.edtSDT);
        edtEmail=findViewById(R.id.edtEmail);
        btnRegister=findViewById(R.id.btnRegister);
    }
    private void setControl(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ho = edtHo.getText().toString();
                String ten = edtTen.getText().toString();
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                String passwordNhapLai = edtPasswordNhapLai.getText().toString();
                String cccd = edtCCCD.getText().toString();
                String sdt = edtSDT.getText().toString();
                String email = edtEmail.getText().toString();

                //Check
                if(!password.equals(passwordNhapLai)){
                    Toast.makeText(PetShopRegister.this, "Mật khẩu nhập lại không chính xác", Toast.LENGTH_SHORT).show();
                    return;
                }

                thongTinDangKy.setHo(ho);
                thongTinDangKy.setTen(ten);
                thongTinDangKy.setTenDangNhap(username);
                thongTinDangKy.setMatKhau(password);
                thongTinDangKy.setCccd(cccd);
                thongTinDangKy.setSoDienThoai(sdt);
                thongTinDangKy.setEmail(email);
                dangKyService.register(thongTinDangKy).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.code() == 200) {
                            try {
                                maXacNhan = response.body().string();
                                openConfirmDialog(Gravity.CENTER);
                            } catch (IOException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                        else{
                            try {
                                int code = response.code();
                                String message = response.message();
                                String error = response.errorBody().string();
                                SendMessage.sendMessageFail(PetShopRegister.this,code,error,message);
                            } catch (Exception e) {
                                SendMessage.sendCatch(PetShopRegister.this,e.getMessage());
                                return;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                        SendMessage.sendApiFail(PetShopRegister.this,throwable);
                    }
                });
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Retrofit retrofit = ApiClient.getClient();
        dangKyService =retrofit.create(DangKyService.class);
        setContentView(R.layout.activity_pet_shop_register);
        setInit();
        setControl();
    }
}