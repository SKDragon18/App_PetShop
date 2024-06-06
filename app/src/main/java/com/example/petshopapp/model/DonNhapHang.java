package com.example.petshopapp.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DonNhapHang {
    private long maDonNhapHang;
    private Timestamp ngayLap;
    private String maNhanVien;
    private ChiNhanh chiNhanhDTO;

    public DonNhapHang() {
    }

    public DonNhapHang(long maDonNhapHang, Timestamp ngayLap, String maNhanVien, ChiNhanh chiNhanhDTO) {
        this.maDonNhapHang = maDonNhapHang;
        this.ngayLap = ngayLap;
        this.maNhanVien = maNhanVien;
        this.chiNhanhDTO = chiNhanhDTO;
    }

    public long getMaDonNhapHang() {
        return maDonNhapHang;
    }

    public void setMaDonNhapHang(long maDonNhapHang) {
        this.maDonNhapHang = maDonNhapHang;
    }

    public Timestamp getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(Timestamp ngayLap) {
        this.ngayLap = ngayLap;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public ChiNhanh getChiNhanhDTO() {
        return chiNhanhDTO;
    }

    public void setChiNhanhDTO(ChiNhanh chiNhanhDTO) {
        this.chiNhanhDTO = chiNhanhDTO;
    }
}
