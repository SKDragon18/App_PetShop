package com.example.petshopapp.api.apiservice;

import com.example.petshopapp.model.LoaiThuCung;
import com.example.petshopapp.model.ThuCung;

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

public interface LoaiThuCungService {
    @GET("loaithucung")
    Call<List<LoaiThuCung>> getAll();

    @GET("loaithucung/{id}")
    Call<LoaiThuCung> getOneById(@Path("id") int id);

    @POST("loaithucung")
    Call<LoaiThuCung> insert(@Body RequestBody tenLoaiThuCung);

    @PUT("loaithucung")
    Call<LoaiThuCung> update(@Body LoaiThuCung loaiThuCung);

    @DELETE("loaithucung/{id}")
    Call<ResponseBody> delete(@Path("id") int id);

}
