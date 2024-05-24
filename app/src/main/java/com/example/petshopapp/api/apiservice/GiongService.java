package com.example.petshopapp.api.apiservice;

import com.example.petshopapp.model.Giong;
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

public interface GiongService {
    @GET("giong")
    Call<List<Giong>> getAll();

    @POST("giong")
    Call<Giong> insert(@Body Giong giong);

    @PUT("giong")
    Call<Giong> update(@Body Giong giong);

    @DELETE("giong/{id}")
    Call<ResponseBody> delete(@Path("id") int id);
}
