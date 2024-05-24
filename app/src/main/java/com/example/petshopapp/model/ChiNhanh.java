package com.example.petshopapp.model;

public class ChiNhanh {
    int maChiNhanh;
    String tenChiNhanh;

    public ChiNhanh() {
    }

    public ChiNhanh(int maChiNhanh, String tenChiNhanh) {
        this.maChiNhanh = maChiNhanh;
        this.tenChiNhanh = tenChiNhanh;
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
}
