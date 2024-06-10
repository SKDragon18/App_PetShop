package com.example.petshopapp.model;

import java.math.BigDecimal;

public class DonDatThuCungGui {
    private long maDonDat;
    private long maThuCung;
    private BigDecimal donGia;
    private int soLuong;

    public DonDatThuCungGui() {
    }

    public DonDatThuCungGui(long maDonDat, long maThuCung, BigDecimal donGia, int soLuong) {
        this.maDonDat = maDonDat;
        this.maThuCung = maThuCung;
        this.donGia = donGia;
        this.soLuong = soLuong;
    }

    public long getMaDonDat() {
        return maDonDat;
    }

    public void setMaDonDat(long maDonDat) {
        this.maDonDat = maDonDat;
    }

    public long getMaThuCung() {
        return maThuCung;
    }

    public void setMaThuCung(long maThuCung) {
        this.maThuCung = maThuCung;
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
}
