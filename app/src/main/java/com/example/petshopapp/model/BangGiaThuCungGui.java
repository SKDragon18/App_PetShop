package com.example.petshopapp.model;

import java.math.BigDecimal;

public class BangGiaThuCungGui {
    private long maBangGia;
    private long maThuCung;
    private BigDecimal donGia;

    public BangGiaThuCungGui() {
    }

    public BangGiaThuCungGui(long maBangGia, long maThuCung, BigDecimal donGia) {
        this.maBangGia = maBangGia;
        this.maThuCung = maThuCung;
        this.donGia = donGia;
    }

    public long getMaBangGia() {
        return maBangGia;
    }

    public void setMaBangGia(long maBangGia) {
        this.maBangGia = maBangGia;
    }

    public long getMaThuCung() {
        return maThuCung;
    }

    public void setMaThuCung(long maThuCung) {
        this.maThuCung = maThuCung;
    }

    public BigDecimal getDonGia() {
        return donGia;
    }

    public void setDonGia(BigDecimal donGia) {
        this.donGia = donGia;
    }
}
