package com.example.petshopapp.api.apiservice;

import com.example.petshopapp.model.TaiKhoan;
import com.example.petshopapp.model.ThongTinXacNhan;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface QuenMatKhauService {
    @POST("identity/forget")
    Call<ResponseBody> sendCode(@Body RequestBody username);

    @POST("identity/forget/confirm")
    Call<ResponseBody> confirm(@Body ThongTinXacNhan thongTinXacNhan);

    @PUT("identity/forget")
    Call<ResponseBody> updatePassword(@Body TaiKhoan taiKhoan);
}
