package com.example.petshopapp.model;

import java.math.BigDecimal;

public class ThuCung {
    private long maThuCung;
    private String tenThuCung;
    private int trangThaiBan;
    String chu;
    String moTa;
    BigDecimal giaHienTai;
    ChiNhanh chiNhanh;
    Giong giong;

    public ThuCung() {
    }

    public ThuCung(long maThuCung, String tenThuCung, int trangThaiBan,
                   String chu, String moTa, BigDecimal giaHienTai,
                   ChiNhanh chiNhanh, Giong giong) {
        this.maThuCung = maThuCung;
        this.tenThuCung = tenThuCung;
        this.trangThaiBan = trangThaiBan;
        this.chu = chu;
        this.moTa = moTa;
        this.giaHienTai = giaHienTai;
        this.chiNhanh = chiNhanh;
        this.giong = giong;
    }

    public long getMaThuCung() {
        return maThuCung;
    }

    public void setMaThuCung(long maThuCung) {
        this.maThuCung = maThuCung;
    }

    public String getTenThuCung() {
        return tenThuCung;
    }

    public void setTenThuCung(String tenThuCung) {
        this.tenThuCung = tenThuCung;
    }

    public int getTrangThaiBan() {
        return trangThaiBan;
    }

    public void setTrangThaiBan(int trangThaiBan) {
        this.trangThaiBan = trangThaiBan;
    }

    public String getChu() {
        return chu;
    }

    public void setChu(String chu) {
        this.chu = chu;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public BigDecimal getGiaHienTai() {
        return giaHienTai;
    }

    public void setGiaHienTai(BigDecimal giaHienTai) {
        this.giaHienTai = giaHienTai;
    }

    public ChiNhanh getChiNhanh() {
        return chiNhanh;
    }

    public void setChiNhanh(ChiNhanh chiNhanh) {
        this.chiNhanh = chiNhanh;
    }

    public Giong getGiong() {
        return giong;
    }

    public void setGiong(Giong giong) {
        this.giong = giong;
    }
}
