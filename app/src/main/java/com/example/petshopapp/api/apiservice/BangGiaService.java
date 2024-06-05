package com.example.petshopapp.api.apiservice;

import com.example.petshopapp.model.BangGia;
import com.example.petshopapp.model.BangGiaSanPham;
import com.example.petshopapp.model.BangGiaSanPhamGui;
import com.example.petshopapp.model.BangGiaThuCung;
import com.example.petshopapp.model.BangGiaThuCungGui;

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

public interface BangGiaService {
    @GET("center/banggia")
    Call<List<BangGia>> getAll();

    @POST("center/banggia")
    Call<BangGia> insert(@Body BangGia bangGia);

    @PUT("center/banggia")
    Call<BangGia> update(@Body BangGia banggia);

    @DELETE("center/banggia/{id}")
    Call<ResponseBody> delete(@Path("id") long id);

    //Chi tiết sản phẩm
    @GET("center/ct-san-pham")
    Call<List<BangGiaSanPham>> getAllSP();

    @POST("center/ct-san-pham")
    Call<ResponseBody> updateSP(@Body List<BangGiaSanPhamGui> bangGiaSanPhamGuiList);

    //Chi tiết thú cưng
    @GET("center/ct-thu-cung")
    Call<List<BangGiaThuCung>> getAllTC();

    @POST("center/ct-thu-cung")
    Call<ResponseBody> updateTC(@Body List<BangGiaThuCungGui> bangGiaThuCungGuiList);
}
