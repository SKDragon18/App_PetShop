package com.example.petshopapp.api.apiservice;

import com.example.petshopapp.model.ThongTinDangNhap;
import com.example.petshopapp.model.ThongTinPhanHoiDangNhap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DangNhapService {
    @POST("identity/login")
    Call<ThongTinPhanHoiDangNhap> checkLogin(@Body ThongTinDangNhap thongTinDangNhap);
}
