package com.example.petshopapp.tabView.importProduct.model;

import com.example.petshopapp.model.ChiTietNhapThuCung;
import com.example.petshopapp.model.ThuCung;

import java.math.BigDecimal;

public class ChiTietThuCung {
    private long maDonNhap;
    private ThuCung thuCung;
    private BigDecimal giaNhap;
    private int soLuong;

    public ChiTietThuCung(long maDonNhap, ThuCung thuCung, BigDecimal giaNhap, int soLuong) {
        this.maDonNhap = maDonNhap;
        this.thuCung = thuCung;
        this.giaNhap = giaNhap;
        this.soLuong = soLuong;
    }

    public ChiTietThuCung() {
    }

    public ChiTietNhapThuCung convertDTO(){
        ChiTietNhapThuCung chiTietNhapThuCung = new ChiTietNhapThuCung();
        chiTietNhapThuCung.setMaThuCung(thuCung.getMaThuCung());
        chiTietNhapThuCung.setMaDonNhap(maDonNhap);
        chiTietNhapThuCung.setGiaNhap(giaNhap);
        chiTietNhapThuCung.setSoLuong(soLuong);
        return chiTietNhapThuCung;
    }


    public long getMaDonNhap() {
        return maDonNhap;
    }

    public void setMaDonNhap(long maDonNhap) {
        this.maDonNhap = maDonNhap;
    }

    public ThuCung getThuCung() {
        return thuCung;
    }

    public void setThuCung(ThuCung thuCung) {
        this.thuCung = thuCung;
    }

    public BigDecimal getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(BigDecimal giaNhap) {
        this.giaNhap = giaNhap;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
