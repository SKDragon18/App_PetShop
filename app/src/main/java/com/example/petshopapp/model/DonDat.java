package com.example.petshopapp.model;

import java.util.Date;

public class DonDat {
    private long soDonDat;
    private Date ngayLap;
    private String diaChi;
    private String soDienThoai;
    private Integer maChiNhanh;
    private String maKhachhang;
    private Boolean trangThai;

    public DonDat() {
    }

    public DonDat(long soDonDat, Date ngayLap, String diaChi, String soDienThoai, Integer maChiNhanh, String maKhachhang, Boolean trangThai) {
        this.soDonDat = soDonDat;
        this.ngayLap = ngayLap;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
        this.maChiNhanh = maChiNhanh;
        this.maKhachhang = maKhachhang;
        this.trangThai = trangThai;
    }

    public long getSoDonDat() {
        return soDonDat;
    }

    public void setSoDonDat(long soDonDat) {
        this.soDonDat = soDonDat;
    }

    public Date getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(Date ngayLap) {
        this.ngayLap = ngayLap;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public Integer getMaChiNhanh() {
        return maChiNhanh;
    }

    public void setMaChiNhanh(Integer maChiNhanh) {
        this.maChiNhanh = maChiNhanh;
    }

    public String getMaKhachhang() {
        return maKhachhang;
    }

    public void setMaKhachhang(String maKhachhang) {
        this.maKhachhang = maKhachhang;
    }

    public Boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
    }
}
