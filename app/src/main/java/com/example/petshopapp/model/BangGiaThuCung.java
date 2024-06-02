package com.example.petshopapp.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class BangGiaThuCung {
    private long maBangGia;
    private Timestamp thoiGianBatDau;
    private Timestamp thoiGianKetThuc;
    private int maChiNhanh;
    private String tenChiNhanh;
    private long maThuCung;
    private String tenThuCung;
    private String moTa;
    private int maGiong;
    private String tenGiong;
    private BigDecimal giaHienTai;
    private BigDecimal giaKhuyenMai;

    public BangGiaThuCung() {
    }

    public BangGiaThuCung(long maBangGia, Timestamp thoiGianBatDau, Timestamp thoiGianKetThuc,
                          int maChiNhanh, String tenChiNhanh, long maThuCung,
                          String tenThuCung, String moTa, int maGiong, String tenGiong,
                          BigDecimal giaHienTai, BigDecimal giaKhuyenMai) {
        this.maBangGia = maBangGia;
        this.thoiGianBatDau = thoiGianBatDau;
        this.thoiGianKetThuc = thoiGianKetThuc;
        this.maChiNhanh = maChiNhanh;
        this.tenChiNhanh = tenChiNhanh;
        this.maThuCung = maThuCung;
        this.tenThuCung = tenThuCung;
        this.moTa = moTa;
        this.maGiong = maGiong;
        this.tenGiong = tenGiong;
        this.giaHienTai = giaHienTai;
        this.giaKhuyenMai = giaKhuyenMai;
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

    public int getMaChiNhanh() {
        return maChiNhanh;
    }

    public void setMaChiNhanh(int maChiNhanh) {
        this.maChiNhanh = maChiNhanh;
    }

    public String getTenChiNhanh() {
        return tenChiNhanh;
    }

    public void setTenChiNhanh(String tenChiNhanh) {
        this.tenChiNhanh = tenChiNhanh;
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

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public int getMaGiong() {
        return maGiong;
    }

    public void setMaGiong(int maGiong) {
        this.maGiong = maGiong;
    }

    public String getTenGiong() {
        return tenGiong;
    }

    public void setTenGiong(String tenGiong) {
        this.tenGiong = tenGiong;
    }

    public BigDecimal getGiaHienTai() {
        return giaHienTai;
    }

    public void setGiaHienTai(BigDecimal giaHienTai) {
        this.giaHienTai = giaHienTai;
    }

    public BigDecimal getGiaKhuyenMai() {
        return giaKhuyenMai;
    }

    public void setGiaKhuyenMai(BigDecimal giaKhuyenMai) {
        this.giaKhuyenMai = giaKhuyenMai;
    }
}
