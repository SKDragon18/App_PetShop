package com.example.petshopapp.model;

import java.math.BigDecimal;

public class DonDatSanPhamGui {
    private long maDonDat;
    private long maSanPham;
    private BigDecimal donGia;
    private int soLuong;
    private int maChiNhanh;

    public DonDatSanPhamGui() {
    }

    public DonDatSanPhamGui(long maDonDat, long maSanPham, BigDecimal donGia,
                            int soLuong, int maChiNhanh) {
        this.maDonDat = maDonDat;
        this.maSanPham = maSanPham;
        this.donGia = donGia;
        this.soLuong = soLuong;
        this.maChiNhanh = maChiNhanh;
    }

    public long getMaDonDat() {
        return maDonDat;
    }

    public void setMaDonDat(long maDonDat) {
        this.maDonDat = maDonDat;
    }

    public long getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(long maSanPham) {
        this.maSanPham = maSanPham;
    }

    public BigDecimal getDonGia() {
        return donGia;
    }

    public void setDonGia(BigDecimal donGia) {
        this.donGia = donGia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getMaChiNhanh() {
        return maChiNhanh;
    }

    public void setMaChiNhanh(int maChiNhanh) {
        this.maChiNhanh = maChiNhanh;
    }
}
