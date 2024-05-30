package com.example.petshopapp.model;

public class HinhAnh {
    private long maHinhAnh;
    private String path;
    private String tenHinhAnh;
    private String tenDuyNhat;
    private String loaiHinhAnh;
    private String source;

    public HinhAnh() {

    }

    public HinhAnh(long maHinhAnh, String path, String tenHinhAnh, String tenDuyNhat, String loaiHinhAnh, String source) {
        this.maHinhAnh = maHinhAnh;
        this.path = path;
        this.tenHinhAnh = tenHinhAnh;
        this.tenDuyNhat = tenDuyNhat;
        this.loaiHinhAnh = loaiHinhAnh;
        this.source = source;
    }

    public long getMaHinhAnh() {
        return maHinhAnh;
    }

    public void setMaHinhAnh(long maHinhAnh) {
        this.maHinhAnh = maHinhAnh;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTenHinhAnh() {
        return tenHinhAnh;
    }

    public void setTenHinhAnh(String tenHinhAnh) {
        this.tenHinhAnh = tenHinhAnh;
    }

    public String getTenDuyNhat() {
        return tenDuyNhat;
    }

    public void setTenDuyNhat(String tenDuyNhat) {
        this.tenDuyNhat = tenDuyNhat;
    }

    public String getLoaiHinhAnh() {
        return loaiHinhAnh;
    }

    public void setLoaiHinhAnh(String loaiHinhAnh) {
        this.loaiHinhAnh = loaiHinhAnh;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
