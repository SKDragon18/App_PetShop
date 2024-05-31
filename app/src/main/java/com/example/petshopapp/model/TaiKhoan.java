package com.example.petshopapp.model;

import java.time.LocalDateTime;

public class TaiKhoan {
    private String tenDangNhap;
    private String matKhau;
    private String quyen;
    private Boolean trangThai;
    private String maXacNhan;
    private LocalDateTime thoiGianTaoMa;
    private LocalDateTime thoiGianHetHan;
    private LocalDateTime thoiGianXacNhan;

    public TaiKhoan() {
    }

    public TaiKhoan(String tenDangNhap, String matKhau, String quyen, Boolean trangThai,
                    String maXacNhan, LocalDateTime thoiGianTaoMa, LocalDateTime thoiGianHetHan,
                    LocalDateTime thoiGianXacNhan) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.quyen = quyen;
        this.trangThai = trangThai;
        this.maXacNhan = maXacNhan;
        this.thoiGianTaoMa = thoiGianTaoMa;
        this.thoiGianHetHan = thoiGianHetHan;
        this.thoiGianXacNhan = thoiGianXacNhan;
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

    public String getQuyen() {
        return quyen;
    }

    public void setQuyen(String quyen) {
        this.quyen = quyen;
    }

    public Boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
    }

    public String getMaXacNhan() {
        return maXacNhan;
    }

    public void setMaXacNhan(String maXacNhan) {
        this.maXacNhan = maXacNhan;
    }

    public LocalDateTime getThoiGianTaoMa() {
        return thoiGianTaoMa;
    }

    public void setThoiGianTaoMa(LocalDateTime thoiGianTaoMa) {
        this.thoiGianTaoMa = thoiGianTaoMa;
    }

    public LocalDateTime getThoiGianHetHan() {
        return thoiGianHetHan;
    }

    public void setThoiGianHetHan(LocalDateTime thoiGianHetHan) {
        this.thoiGianHetHan = thoiGianHetHan;
    }

    public LocalDateTime getThoiGianXacNhan() {
        return thoiGianXacNhan;
    }

    public void setThoiGianXacNhan(LocalDateTime thoiGianXacNhan) {
        this.thoiGianXacNhan = thoiGianXacNhan;
    }
}
