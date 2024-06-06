package com.example.petshopapp.api.apiservice;

import com.example.petshopapp.model.ChiTietNhapSanPham;
import com.example.petshopapp.model.ChiTietNhapThuCung;
import com.example.petshopapp.model.DonNhapHang;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface NhapHangService {

    @GET("center/nhaphang")
    Call<List<DonNhapHang>> getAll();

    @POST("center/nhaphang/tao-phieu-nhap")
    Call<DonNhapHang> insert(@Body DonNhapHang donNhapHang);

    @POST("center/nhaphang/them-san-pham")
    Call<ResponseBody> addSP(@Body List<ChiTietNhapSanPham> list);

    @POST("center/nhaphang/them-thu-cung")
    Call<ResponseBody> addTC(@Body List<ChiTietNhapThuCung> list);
}
