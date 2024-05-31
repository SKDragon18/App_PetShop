package com.example.petshopapp.api.apiservice;

import com.example.petshopapp.model.ThongTinDangKy;
import com.example.petshopapp.model.ThongTinXacNhan;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DangKyService {
    @POST("identity/register")
    Call<ResponseBody> register(@Body ThongTinDangKy thongTinDangKy);

    @POST("identity/register/confirm")
    Call<ResponseBody> confirm(@Body ThongTinXacNhan thongTinXacNhan);

    @POST("identity/register/getVerifiedCode")
    Call<ResponseBody> getAgain(@Body ThongTinDangKy thongTinDangKy);
}
