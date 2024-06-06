package com.example.petshopapp.model;

import java.math.BigDecimal;

public class ChiTietNhapThuCung {
    private long maDonNhap;
    private long maThuCung;
    private BigDecimal giaNhap;
    private int soLuong;

    public ChiTietNhapThuCung() {
    }

    public ChiTietNhapThuCung(long maDonNhap, long maThuCung, BigDecimal giaNhap, int soLuong) {
        this.maDonNhap = maDonNhap;
        this.maThuCung = maThuCung;
        this.giaNhap = giaNhap;
        this.soLuong = soLuong;
    }

    public long getMaDonNhap() {
        return maDonNhap;
    }

    public void setMaDonNhap(long maDonNhap) {
        this.maDonNhap = maDonNhap;
    }

    public long getMaThuCung() {
        return maThuCung;
    }

    public void setMaThuCung(long maThuCung) {
        this.maThuCung = maThuCung;
    }

    public BigDecimal getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(BigDecimal giaNhap) {
        this.giaNhap = giaNhap;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}

