package com.example.petshopapp.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class BangGiaSanPham implements Serializable {
    private long maSanPham;
    private String tenSanPham;
    private int maLoaiSanPham;
    private String tenLoaiSanPham;
    private long maBangGia;
    private Timestamp thoiGianBatDau;
    private Timestamp thoiGianKetThuc;
    private int maChiNhanh;
    private String tenChiNhanh;
    private BigDecimal giaHienTai;
    private BigDecimal giaKhuyenMai;
    private long soLuongTon;
    private String hinhAnh;

    public BangGiaSanPham() {
    }

    public BangGiaSanPham(long maSanPham, String tenSanPham, int maLoaiSanPham, String tenLoaiSanPham,
                          long maBangGia, Timestamp thoiGianBatDau, Timestamp thoiGianKetThuc,
                          int maChiNhanh, String tenChiNhanh,
                          BigDecimal giaHienTai, BigDecimal giaKhuyenMai, long soLuongTon, String hinhAnh) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.maLoaiSanPham = maLoaiSanPham;
        this.tenLoaiSanPham = tenLoaiSanPham;
        this.maBangGia = maBangGia;
        this.thoiGianBatDau = thoiGianBatDau;
        this.thoiGianKetThuc = thoiGianKetThuc;
        this.maChiNhanh = maChiNhanh;
        this.tenChiNhanh = tenChiNhanh;
        this.giaHienTai = giaHienTai;
        this.giaKhuyenMai = giaKhuyenMai;
        this.soLuongTon = soLuongTon;
        this.hinhAnh = hinhAnh;
    }

    public long getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(long maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public int getMaLoaiSanPham() {
        return maLoaiSanPham;
    }

    public void setMaLoaiSanPham(int maLoaiSanPham) {
        this.maLoaiSanPham = maLoaiSanPham;
    }

    public String getTenLoaiSanPham() {
        return tenLoaiSanPham;
    }

    public void setTenLoaiSanPham(String tenLoaiSanPham) {
        this.tenLoaiSanPham = tenLoaiSanPham;
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

    public long getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(long soLuongTon) {
        this.soLuongTon = soLuongTon;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
}
