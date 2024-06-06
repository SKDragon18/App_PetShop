package com.example.petshopapp.tabView.importProduct.model;

import com.example.petshopapp.model.ChiTietNhapSanPham;
import com.example.petshopapp.model.SanPham;

import java.math.BigDecimal;

public class ChiTietSanPham {
    private long maDonNhap;
    private SanPham sanPham;
    private int soLuong;
    private BigDecimal donGia;

    public ChiTietSanPham() {
    }

    public ChiTietSanPham(long maDonNhap, SanPham sanPham, int soLuong, BigDecimal donGia) {
        this.maDonNhap = maDonNhap;
        this.sanPham = sanPham;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public ChiTietNhapSanPham convertDTO(){
        ChiTietNhapSanPham chiTietNhapSanPham = new ChiTietNhapSanPham();
        chiTietNhapSanPham.setMaSanPham(sanPham.getMaSanPham());
        chiTietNhapSanPham.setMaDonNhap(maDonNhap);
        chiTietNhapSanPham.setDonGia(donGia);
        chiTietNhapSanPham.setSoLuong(soLuong);
        return chiTietNhapSanPham;
    }

    public long getMaDonNhap() {
        return maDonNhap;
    }

    public void setMaDonNhap(long maDonNhap) {
        this.maDonNhap = maDonNhap;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
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
