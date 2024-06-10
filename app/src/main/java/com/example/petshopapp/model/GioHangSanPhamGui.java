package com.example.petshopapp.model;

public class GioHangSanPhamGui {
    private String maKhachHang;
    private long maSanPham;
    private int maChiNhanh;

    public GioHangSanPhamGui() {
    }

    public GioHangSanPhamGui(String maKhachHang, long maSanPham, int maChiNhanh) {
        this.maKhachHang = maKhachHang;
        this.maSanPham = maSanPham;
        this.maChiNhanh = maChiNhanh;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public long getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(long maSanPham) {
        this.maSanPham = maSanPham;
    }

    public int getMaChiNhanh() {
        return maChiNhanh;
    }

    public void setMaChiNhanh(int maChiNhanh) {
        this.maChiNhanh = maChiNhanh;
    }
}
