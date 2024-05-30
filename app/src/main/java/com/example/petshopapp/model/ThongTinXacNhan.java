package com.example.petshopapp.model;

public class ThongTinXacNhan {
    private String tenDangNhap;
    private String maXacNhan;

    public ThongTinXacNhan() {
    }

    public ThongTinXacNhan(String tenDangNhap, String maXacNhan) {
        this.tenDangNhap = tenDangNhap;
        this.maXacNhan = maXacNhan;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMaXacNhan() {
        return maXacNhan;
    }

    public void setMaXacNhan(String maXacNhan) {
        this.maXacNhan = maXacNhan;
    }
}
