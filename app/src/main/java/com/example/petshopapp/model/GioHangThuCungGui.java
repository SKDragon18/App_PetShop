package com.example.petshopapp.model;

public class GioHangThuCungGui {
    private String maKhachHang;
    private long maThuCung;

    public GioHangThuCungGui() {
    }

    public GioHangThuCungGui(String maKhachHang, long maThuCung) {
        this.maKhachHang = maKhachHang;
        this.maThuCung = maThuCung;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public long getMaThuCung() {
        return maThuCung;
    }

    public void setMaThuCung(long maThuCung) {
        this.maThuCung = maThuCung;
    }
}
