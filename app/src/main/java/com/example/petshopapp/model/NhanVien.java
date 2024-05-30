package com.example.petshopapp.model;

import java.util.List;

public class NhanVien {
    private String maNhanVien;
    private String ho;
    private String ten;
    private String cccd;
    private String chucVu;
    private String soDienThoai;
    private String email;
    private ChiNhanh chiNhanh;
    private List<Long> hinhAnh;
    private boolean trangThai;

    public NhanVien() {
    }

    public NhanVien(String maNhanVien, String ho, String ten, String cccd,
                    String chucVu, String soDienThoai, String email, ChiNhanh chiNhanh,
                    List<Long> hinhAnh, boolean trangThai) {
        this.maNhanVien = maNhanVien;
        this.ho = ho;
        this.ten = ten;
        this.cccd = cccd;
        this.chucVu = chucVu;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.chiNhanh = chiNhanh;
        this.hinhAnh = hinhAnh;
        this.trangThai=trangThai;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getHo() {
        return ho;
    }

    public void setHo(String ho) {
        this.ho = ho;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ChiNhanh getChiNhanh() {
        return chiNhanh;
    }

    public void setChiNhanh(ChiNhanh chiNhanh) {
        this.chiNhanh = chiNhanh;
    }

    public List<Long> getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(List<Long> hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }
}
