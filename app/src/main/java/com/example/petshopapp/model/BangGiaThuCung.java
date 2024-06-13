package com.example.petshopapp.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class BangGiaThuCung implements Serializable {
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
    private int soLuongTon;
    private String hinhAnh;

    public BangGiaThuCung() {
    }

    public BangGiaThuCung(long maBangGia, Timestamp thoiGianBatDau, Timestamp thoiGianKetThuc,
                          int maChiNhanh, String tenChiNhanh, long maThuCung,
                          String tenThuCung, String moTa, int maGiong, String tenGiong,
                          BigDecimal giaHienTai, BigDecimal giaKhuyenMai,int soLuongTon, String hinhAnh) {
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
        this.soLuongTon = soLuongTon;
        this.hinhAnh = hinhAnh;
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

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
}
