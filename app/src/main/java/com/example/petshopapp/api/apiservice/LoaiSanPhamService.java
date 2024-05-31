package com.example.petshopapp.api.apiservice;

import com.example.petshopapp.model.LoaiSanPham;
import com.example.petshopapp.model.LoaiThuCung;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface LoaiSanPhamService {
    @GET("center/loaisanpham")
    Call<List<LoaiSanPham>> getAll();

    @GET("center/loaisanpham/{id}")
    Call<LoaiSanPham> getOneById(@Path("id") int id);

    @POST("center/loaisanpham")
    Call<LoaiSanPham> insert(@Body RequestBody tenLoaiSanPham);

    @PUT("center/loaisanpham")
    Call<LoaiSanPham> update(@Body LoaiSanPham loaiSanPham);

    @DELETE("center/loaisanpham/{id}")
    Call<ResponseBody> delete(@Path("id") int id);

}
