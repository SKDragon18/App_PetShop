package com.example.petshopapp.api.apiservice;


import com.example.petshopapp.model.KhachHang;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface KhachHangService {
    @GET("identity/khachhang")
    Call<List<KhachHang>> getAll();

    @GET("identity/khachhang/{id}")
    Call<KhachHang> getOneById(@Path("id") String id);

    @POST("identity/khachhang")
    Call<KhachHang> insert(@Body KhachHang khachHang);

    @PUT("identity/khachhang")
    Call<KhachHang> update(@Body KhachHang khachHang);

    @DELETE("identity/khachhang/{id}")
    Call<ResponseBody> delete(@Path("id")String id);
}
