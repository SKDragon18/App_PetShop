package com.example.petshopapp.api.apiservice;

import com.example.petshopapp.model.ThongTinDangKy;
import com.example.petshopapp.model.ThongTinXacNhan;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DangKyService {
    @POST("/register")
    Call<ResponseBody> register(@Body ThongTinDangKy thongTinDangKy);

    @POST("/register/confirm")
    Call<ResponseBody> confirm(@Body ThongTinXacNhan thongTinXacNhan);

    @POST("/register/getVerifiedCode")
    Call<ResponseBody> getAgain(@Body ThongTinDangKy thongTinDangKy);
}
