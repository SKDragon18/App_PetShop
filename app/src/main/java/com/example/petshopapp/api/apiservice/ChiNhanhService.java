package com.example.petshopapp.api.apiservice;

import com.example.petshopapp.model.ChiNhanh;
import com.example.petshopapp.model.NhanVien;

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

public interface ChiNhanhService {
    @GET("center/chinhanh")
    Call<List<ChiNhanh>> getAll();

    @GET("center/chinhanh/{id}")
    Call<ChiNhanh> getOneById(@Path("id") int id);

    @POST("center/chinhanh")
    Call<ChiNhanh> insert(@Body RequestBody tenChiNhanh);

    @PUT("center/chinhanh")
    Call<ChiNhanh> update(@Body ChiNhanh chinhanh);

    @DELETE("center/chinhanh/{id}")
    Call<ResponseBody> delete(@Path("id") int id);
}
