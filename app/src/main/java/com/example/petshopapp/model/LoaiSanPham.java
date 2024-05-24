package com.example.petshopapp.model;

public class LoaiSanPham {
    int maLoaiSanPham;
    String tenLoaiSanPham;

    public LoaiSanPham() {
    }

    public LoaiSanPham(int maLoaiSanPham, String tenLoaiSanPham) {
        this.maLoaiSanPham = maLoaiSanPham;
        this.tenLoaiSanPham = tenLoaiSanPham;
    }

    public int getMaLoaiSanPham() {
        return maLoaiSanPham;
    }

    public void setMaLoaiSanPham(int maLoaiSanPham) {
        this.maLoaiSanPham = maLoaiSanPham;
    }

    public String getTenLoaiSanPham() {
        return tenLoaiSanPham;
    }

    public void setTenLoaiSanPham(String tenLoaiSanPham) {
        this.tenLoaiSanPham = tenLoaiSanPham;
    }
}
