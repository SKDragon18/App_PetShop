package com.example.petshopapp.api.apiservice;

import com.example.petshopapp.model.ChiNhanh;

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
    @GET("chinhanh")
    Call<List<ChiNhanh>> getAll();

    @POST("chinhanh")
    Call<ChiNhanh> insert(@Body RequestBody tenChiNhanh);

    @PUT("chinhanh")
    Call<ChiNhanh> update(@Body ChiNhanh chinhanh);

    @DELETE("chinhanh/{id}")
    Call<ResponseBody> delete(@Path("id") int id);
}
