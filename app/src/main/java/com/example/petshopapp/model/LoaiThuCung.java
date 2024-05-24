package com.example.petshopapp.model;

public class LoaiThuCung {
    int maLoaiThuCung;
    String tenLoaiThuCung;

    public LoaiThuCung() {
    }

    public LoaiThuCung(int maLoaiThuCung, String tenLoaiThuCung) {
        this.maLoaiThuCung = maLoaiThuCung;
        this.tenLoaiThuCung = tenLoaiThuCung;
    }

    public int getMaLoaiThuCung() {
        return maLoaiThuCung;
    }

    public void setMaLoaiThuCung(int maLoaiThuCung) {
        this.maLoaiThuCung = maLoaiThuCung;
    }

    public String getTenLoaiThuCung() {
        return tenLoaiThuCung;
    }

    public void setTenLoaiThuCung(String tenLoaiThuCung) {
        this.tenLoaiThuCung = tenLoaiThuCung;
    }
}
