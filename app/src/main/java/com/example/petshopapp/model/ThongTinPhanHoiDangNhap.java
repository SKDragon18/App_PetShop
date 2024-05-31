package com.example.petshopapp.model;

public class ThongTinPhanHoiDangNhap {
    private String tenDangNhap;
    private String quyen;
    private String token;

    public ThongTinPhanHoiDangNhap() {
    }

    public ThongTinPhanHoiDangNhap(String tenDangNhap, String quyen, String token) {
        this.tenDangNhap = tenDangNhap;
        this.quyen = quyen;
        this.token = token;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getQuyen() {
        return quyen;
    }

    public void setQuyen(String quyen) {
        this.quyen = quyen;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
