package com.example.petshopapp.api.apiservice;

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

public interface NhanVienService {
    @GET("identity/nhanvien")
    Call<List<NhanVien>> getAll();

    @GET("identity/nhanvien/{id}")
    Call<NhanVien> getOneById(@Path("id") String id);

    @POST("identity/nhanvien")
    Call<NhanVien> insert(@Body NhanVien nhanVien);

    @PUT("identity/nhanvien")
    Call<NhanVien> update(@Body NhanVien nhanVien);

    @DELETE("identity/nhanvien/{id}")
    Call<ResponseBody> delete(@Path("id")String id);

}
