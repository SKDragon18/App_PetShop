package com.example.petshopapp.api.apiservice;

import com.example.petshopapp.api.Const;
import com.example.petshopapp.model.HinhAnh;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface HinhAnhService {
    @Multipart
    @POST("identity/hinhanh")
    Call<ResponseBody> saveImage(@Part MultipartBody.Part avatar,
                                 @Part(Const.KEY_MA_NHAN_VIEN)RequestBody maNhanVien,
                                 @Part(Const.KEY_MA_KHACH_HANG) RequestBody maKhachHang);
    @POST("identity/hinhanh/get")
    Call<List<HinhAnh>> getImage(@Body long []id);

    @Multipart
    @POST("center/hinhanh")
    Call<ResponseBody> saveImageCenter(@Part MultipartBody.Part avatar,
                                       @Part(Const.KEY_MA_NHAN_VIEN)RequestBody maNhanVien,
                                       @Part(Const.KEY_MA_KHACH_HANG) RequestBody maKhachHang,
                                     @Part(Const.KEY_MA_THU_CUNG)RequestBody maThuCung,
                                     @Part(Const.KEY_MA_SAN_PHAM) RequestBody maSanPham);
    @POST("center/hinhanh/get")
    Call<List<HinhAnh>> getImageCenter(@Body long []id);
}
