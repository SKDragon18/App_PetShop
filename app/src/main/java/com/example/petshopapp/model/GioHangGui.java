package com.example.petshopapp.model;

public class GioHangGui {
    private String maKhachHang;
    private int maChiNhanh;

    public GioHangGui() {
    }

    public GioHangGui(String maKhachHang, int maChiNhanh) {
        this.maKhachHang = maKhachHang;
        this.maChiNhanh = maChiNhanh;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public int getMaChiNhanh() {
        return maChiNhanh;
    }

    public void setMaChiNhanh(int maChiNhanh) {
        this.maChiNhanh = maChiNhanh;
    }
}
