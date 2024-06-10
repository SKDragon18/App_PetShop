package com.example.petshopapp.model;

public class HoaDonGui {
    private long soHoaDon;
    private String maNhanVien;

    public HoaDonGui() {
    }

    public HoaDonGui(long soHoaDon, String maNhanVien) {
        this.soHoaDon = soHoaDon;
        this.maNhanVien = maNhanVien;
    }

    public long getSoHoaDon() {
        return soHoaDon;
    }

    public void setSoHoaDon(long soHoaDon) {
        this.soHoaDon = soHoaDon;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }
}
