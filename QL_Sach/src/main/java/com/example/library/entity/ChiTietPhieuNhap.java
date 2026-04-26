package com.example.library.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "chi_tiet_phieu_nhap")
public class ChiTietPhieuNhap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "phieu_nhap_id", nullable = false)
    private PhieuNhap phieuNhap;

    @ManyToOne
    @JoinColumn(name = "sach_id", nullable = false)
    private Sach sach;

    @Column(name = "so_luong", nullable = false)
    private int soLuong;

    @Column(name = "don_gia", nullable = false)
    private double donGia;

    // Constructors
    public ChiTietPhieuNhap() {
    }

    public ChiTietPhieuNhap(PhieuNhap phieuNhap, Sach sach, int soLuong, double donGia) {
        this.phieuNhap = phieuNhap;
        this.sach = sach;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PhieuNhap getPhieuNhap() {
        return phieuNhap;
    }

    public void setPhieuNhap(PhieuNhap phieuNhap) {
        this.phieuNhap = phieuNhap;
    }

    public Sach getSach() {
        return sach;
    }

    public void setSach(Sach sach) {
        this.sach = sach;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }
}