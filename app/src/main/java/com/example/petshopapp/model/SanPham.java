package com.example.petshopapp.model;

import java.math.BigDecimal;
import java.util.List;

public class SanPham {
    private long maSanPham;
    private String tenSanPham;
    private BigDecimal giaHienTai;
    private LoaiSanPham loaiSanPham;
    private List<Long> hinhAnh;
    private int maChiNhanh;
    private long soLuongTon;

    public SanPham() {
    }

    public SanPham(long maSanPham, String tenSanPham, BigDecimal giaHienTai,
                   LoaiSanPham loaiSanPham, List<Long> hinhAnh, int maChiNhanh, long soLuongTon) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.giaHienTai = giaHienTai;
        this.loaiSanPham = loaiSanPham;
        this.hinhAnh = hinhAnh;
        this.maChiNhanh = maChiNhanh;
        this.soLuongTon = soLuongTon;
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

    public BigDecimal getGiaHienTai() {
        return giaHienTai;
    }

    public void setGiaHienTai(BigDecimal giaHienTai) {
        this.giaHienTai = giaHienTai;
    }

    public LoaiSanPham getLoaiSanPham() {
        return loaiSanPham;
    }

    public void setLoaiSanPham(LoaiSanPham loaiSanPham) {
        this.loaiSanPham = loaiSanPham;
    }

    public List<Long> getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(List<Long> hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public int getMaChiNhanh() {
        return maChiNhanh;
    }

    public void setMaChiNhanh(int maChiNhanh) {
        this.maChiNhanh = maChiNhanh;
    }

    public long getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(long soLuongTon) {
        this.soLuongTon = soLuongTon;
    }
}
