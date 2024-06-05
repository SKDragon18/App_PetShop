package com.example.petshopapp.api.apiservice;

import com.example.petshopapp.model.ThuCung;
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

public interface ThuCungService {
    @GET("center/thucung")
    Call<List<ThuCung>> getAll();

    @GET("center/thucung/{id}")
    Call<ThuCung> getOneById(@Path("id") String id);

    @POST("center/thucung")
    Call<ThuCung> insert(@Body ThuCung thuCung);

    @PUT("center/thucung")
    Call<ThuCung> update(@Body ThuCung thuCung);

    @DELETE("center/thucung/{id}")
    Call<ResponseBody> delete(@Path("id")long maThuCung);

    @PUT("center/thucung/soluongton")
    Call<ResponseBody> updateSL(@Body ThuCung thuCung);
}
