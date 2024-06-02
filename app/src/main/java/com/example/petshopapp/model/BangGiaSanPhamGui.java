package com.example.petshopapp.model;

import java.math.BigDecimal;

public class BangGiaSanPhamGui {
    private long maBangGia;
    private long maSanPham;
    private BigDecimal donGia;

    public BangGiaSanPhamGui() {
    }

    public BangGiaSanPhamGui(long maBangGia, long maSanPham, BigDecimal donGia) {
        this.maBangGia = maBangGia;
        this.maSanPham = maSanPham;
        this.donGia = donGia;
    }

    public long getMaBangGia() {
        return maBangGia;
    }

    public void setMaBangGia(long maBangGia) {
        this.maBangGia = maBangGia;
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
}
