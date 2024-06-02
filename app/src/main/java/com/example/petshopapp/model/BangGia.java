package com.example.petshopapp.model;

import java.sql.Timestamp;;

public class BangGia {
    long maBangGia;
    Timestamp thoiGianBatDau;
    Timestamp thoiGianKetThuc;
    String noiDung;
    Boolean trangThai;
    ChiNhanh chiNhanh;

    public BangGia() {
    }

    public BangGia(long maBangGia, Timestamp thoiGianBatDau, Timestamp thoiGianKetThuc,
                   String noiDung, Boolean trangThai, ChiNhanh chiNhanh) {
        this.maBangGia = maBangGia;
        this.thoiGianBatDau = thoiGianBatDau;
        this.thoiGianKetThuc = thoiGianKetThuc;
        this.noiDung = noiDung;
        this.trangThai = trangThai;
        this.chiNhanh = chiNhanh;
    }

    public long getMaBangGia() {
        return maBangGia;
    }

    public void setMaBangGia(long maBangGia) {
        this.maBangGia = maBangGia;
    }

    public Timestamp getThoiGianBatDau() {
        return thoiGianBatDau;
    }

    public void setThoiGianBatDau(Timestamp thoiGianBatDau) {
        this.thoiGianBatDau = thoiGianBatDau;
    }

    public Timestamp getThoiGianKetThuc() {
        return thoiGianKetThuc;
    }

    public void setThoiGianKetThuc(Timestamp thoiGianKetThuc) {
        this.thoiGianKetThuc = thoiGianKetThuc;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public Boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
    }

    public ChiNhanh getChiNhanh() {
        return chiNhanh;
    }

    public void setChiNhanh(ChiNhanh chiNhanh) {
        this.chiNhanh = chiNhanh;
    }
}
