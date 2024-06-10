package com.example.petshopapp.api.apiservice;

import com.example.petshopapp.model.DonDat;
import com.example.petshopapp.model.DonDatSanPhamGui;
import com.example.petshopapp.model.DonDatThuCungGui;
import com.example.petshopapp.model.HoaDon;
import com.example.petshopapp.model.HoaDonGui;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DonDatHangService {
    @GET("order/dat-hang")
    Call<List<HoaDon>> getAllHoaDon();

    @GET("order/dat-hang/{id}")
    Call<HoaDon> getOneById(@Path("id") long id);

    @POST("order/dat-hang/sp")
    Call<ResponseBody> themSP(@Body List<DonDatSanPhamGui> list);

    @POST("order/dat-hang/tc")
    Call<ResponseBody> themTC(@Body List<DonDatThuCungGui> list);

    @GET("order/don-dat")
    Call<List<DonDat>> getAllDonDat();

    @POST("order/don-dat")
    Call<DonDat> insert(@Body DonDat donDat);

    @GET("/order/dat-hang/thanhtien/{id}")
    Call<ResponseBody> thanhTien(@Path("id")long id);

    @POST("/order/dat-hang/hoa-don")
    Call<ResponseBody> taoHoaDon(@Body HoaDonGui hoaDonGui);
}
