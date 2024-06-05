package com.example.petshopapp.api.apiservice;

import com.example.petshopapp.model.SanPham;
import com.example.petshopapp.model.ThuCung;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SanPhamService {
    @GET("center/sanpham")
    Call<List<SanPham>> getAll();

    @GET("center/sanpham/{id}")
    Call<SanPham> getOneById(@Path("id") String id);

    @POST("center/sanpham")
    Call<SanPham> insert(@Body SanPham sanPham);

    @PUT("center/sanpham")
    Call<SanPham> update(@Body SanPham sanPham);

    @DELETE("center/sanpham/{sanpham}/{chinhanh}")
    Call<ResponseBody> delete(@Path("sanpham")Long maSanPham, @Path("chinhanh")int maChiNhanh);

    @PUT("center/sanpham/soluongton")
    Call<ResponseBody> updateSL(@Body SanPham sanPham);
}
