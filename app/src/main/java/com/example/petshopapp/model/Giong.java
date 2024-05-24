package com.example.petshopapp.model;

public class Giong {
    int maGiong;
    String tengiong;
    LoaiThuCung loaiThuCung;

    public Giong() {
    }

    public Giong(int maGiong, String tengiong, LoaiThuCung loaiThuCung) {
        this.maGiong = maGiong;
        this.tengiong = tengiong;
        this.loaiThuCung = loaiThuCung;
    }

    public int getMaGiong() {
        return maGiong;
    }

    public void setMaGiong(int maGiong) {
        this.maGiong = maGiong;
    }

    public String getTengiong() {
        return tengiong;
    }

    public void setTengiong(String tengiong) {
        this.tengiong = tengiong;
    }

    public LoaiThuCung getLoaiThuCung() {
        return loaiThuCung;
    }

    public void setLoaiThuCung(LoaiThuCung loaiThuCung) {
        this.loaiThuCung = loaiThuCung;
    }
}
