package com.example.petshopapp.model;

import java.math.BigDecimal;

public class ChiTietNhapSanPham {
    private long maDonNhap;
    private long maSanPham;
    private int soLuong;
    private BigDecimal donGia;

    public ChiTietNhapSanPham() {
    }

    public ChiTietNhapSanPham(long maDonNhap, long maSanPham, int soLuong, BigDecimal donGia) {
        this.maDonNhap = maDonNhap;
        this.maSanPham = maSanPham;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public long getMaDonNhap() {
        return maDonNhap;
    }

    public void setMaDonNhap(long maDonNhap) {
        this.maDonNhap = maDonNhap;
    }

    public long getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(long maSanPham) {
        this.maSanPham = maSanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public BigDecimal getDonGia() {
        return donGia;
    }

    public void setDonGia(BigDecimal donGia) {
        this.donGia = donGia;
    }
}
