package com.example.petshopapp.model;

import java.util.Date;
import java.util.List;

public class KhachHang {
    private String maKhachHang;
    private String ho;
    private String ten;
    private Boolean gioiTinh;
    private Date ngaySinh;
    private String soDienThoai;
    private String email;
    private String cccd;
    private String diaChi;
    private List<Long> hinhAnh;

    public KhachHang() {
    }

    public KhachHang(String maKhachHang, String ho, String ten, Boolean gioiTinh,
                     Date ngaySinh, String soDienThoai, String email, String cccd,
                     String diaChi, List<Long> hinhAnh) {
        this.maKhachHang = maKhachHang;
        this.ho = ho;
        this.ten = ten;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.cccd = cccd;
        this.diaChi = diaChi;
        this.hinhAnh = hinhAnh;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
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

    public Boolean getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(Boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
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

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public List<Long> getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(List<Long> hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
}
