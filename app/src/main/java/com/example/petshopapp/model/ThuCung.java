package com.example.petshopapp.model;

import java.math.BigDecimal;
import java.util.List;

public class ThuCung {
    private long maThuCung;
    private String tenThuCung;
    private int trangThaiBan;
    private String chu;
    private String moTa;
    private BigDecimal giaHienTai;
    private BigDecimal giaKM;
    private ChiNhanh chiNhanh;
    private Giong giong;
    private List<Long> hinhAnh;
    private int soLuongTon;

    public ThuCung() {
    }

    public ThuCung(long maThuCung, String tenThuCung, int trangThaiBan, String chu, String moTa, BigDecimal giaHienTai, BigDecimal giaKM, ChiNhanh chiNhanh, Giong giong, List<Long> hinhAnh, int soLuongTon) {
        this.maThuCung = maThuCung;
        this.tenThuCung = tenThuCung;
        this.trangThaiBan = trangThaiBan;
        this.chu = chu;
        this.moTa = moTa;
        this.giaHienTai = giaHienTai;
        this.giaKM = giaKM;
        this.chiNhanh = chiNhanh;
        this.giong = giong;
        this.hinhAnh = hinhAnh;
        this.soLuongTon = soLuongTon;
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

    public BigDecimal getGiaKM() {
        return giaKM;
    }

    public void setGiaKM(BigDecimal giaKM) {
        this.giaKM = giaKM;
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

    public List<Long> getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(List<Long> hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }
}
