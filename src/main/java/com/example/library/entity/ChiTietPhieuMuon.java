package com.example.library.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "chi_tiet_phieu_muon")
public class ChiTietPhieuMuon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "phieu_muon_id", nullable = false)
    private PhieuMuon phieuMuon;

    @ManyToOne
    @JoinColumn(name = "sach_id", nullable = false)
    private Sach sach;

    @Column(name = "so_luong", nullable = false)
    private int soLuong;

    // Constructors
    public ChiTietPhieuMuon() {
    }

    public ChiTietPhieuMuon(PhieuMuon phieuMuon, Sach sach, int soLuong) {
        this.phieuMuon = phieuMuon;
        this.sach = sach;
        this.soLuong = soLuong;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PhieuMuon getPhieuMuon() {
        return phieuMuon;
    }

    public void setPhieuMuon(PhieuMuon phieuMuon) {
        this.phieuMuon = phieuMuon;
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
}