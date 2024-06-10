package com.example.petshopapp.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class HoaDon {
    private DonDat donDat;
    private long maHoaDon;
    private Timestamp ngayLap;
    private BigDecimal tongHoaDon;
    private String maNhanVien;
    private String tenNhanVien;

    public HoaDon() {
    }

    public HoaDon(DonDat donDat, long maHoaDon, Timestamp ngayLap,
                  BigDecimal tongHoaDon, String maNhanVien, String tenNhanVien) {
        this.donDat = donDat;
        this.maHoaDon = maHoaDon;
        this.ngayLap = ngayLap;
        this.tongHoaDon = tongHoaDon;
        this.maNhanVien = maNhanVien;
        this.tenNhanVien = tenNhanVien;
    }

    public DonDat getDonDat() {
        return donDat;
    }

    public void setDonDat(DonDat donDat) {
        this.donDat = donDat;
    }

    public long getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(long maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public Timestamp getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(Timestamp ngayLap) {
        this.ngayLap = ngayLap;
    }

    public BigDecimal getTongHoaDon() {
        return tongHoaDon;
    }

    public void setTongHoaDon(BigDecimal tongHoaDon) {
        this.tongHoaDon = tongHoaDon;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }
}
