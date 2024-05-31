package com.example.petshopapp.api.apiservice;

import com.example.petshopapp.model.TaiKhoan;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TaiKhoanService {
    @GET("identity/tk")
    Call<List<TaiKhoan>> getAll();

    @GET("identity/tk/{id}")
    Call<TaiKhoan> getOneById(@Path("id") String id);
    @PUT("identity/tk")
    Call<ResponseBody> update(@Body TaiKhoan taiKhoan);

    @PUT("identity/tk/reset")
    Call<ResponseBody> reset(@Body TaiKhoan taiKhoan);
}
