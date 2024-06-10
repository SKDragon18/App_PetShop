package com.example.petshopapp.api.apiservice;

import com.example.petshopapp.model.GioHangGui;
import com.example.petshopapp.model.GioHangSanPhamGui;
import com.example.petshopapp.model.GioHangThuCungGui;
import com.example.petshopapp.model.SanPham;
import com.example.petshopapp.model.ThuCung;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GioHangService {
    @POST("center/gio-hang/thu-cung")
    Call<List<ThuCung>> getThuCung(@Body GioHangGui gioHangGui);

    @POST("center/gio-hang/san-pham")
    Call<List<SanPham>> getSanPham(@Body GioHangGui gioHangGui);

    @POST("center/gio-hang/them-thu-cung")
    Call<ResponseBody> themThuCung(@Body GioHangThuCungGui gioHangThuCungGui);

    @POST("center/gio-hang/them-san-pham")
    Call<ResponseBody> themSanPham(@Body GioHangSanPhamGui gioHangSanPhamGui);
}
