package com.example.petshopapp.model;

public class ThongTinDangKy {
    private String ho;
    private String ten;
    private String tenDangNhap;
    private String matKhau;
    private String cccd;
    private String soDienThoai;
    private String email;

    public ThongTinDangKy() {
    }

    public ThongTinDangKy(String ho, String ten, String tenDangNhap,
                          String matKhau, String cccd, String soDienThoai, String email) {
        this.ho = ho;
        this.ten = ten;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.cccd = cccd;
        this.soDienThoai = soDienThoai;
        this.email = email;
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

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
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
}
