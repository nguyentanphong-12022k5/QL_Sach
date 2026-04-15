package com.example.library.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "docgia")
public class DocGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "madocgia")
    private Long id;

    @Column(name = "tendocgia", nullable = false)
    private String hoTen;

    @Column(name = "gioitinh")
    private String gioiTinh;

    @Column(name = "diachi")
    private String diaChi;

    @Column(name = "sdt")
    private String soDienThoai;

    @Column(name = "TrangThai")
    private Integer trangThai;

    @OneToMany(mappedBy = "docGia")
    private List<PhieuMuon> phieuMuons;

    // Constructors
    public DocGia() {
    }

    public DocGia(String hoTen, String email, String soDienThoai) {
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.trangThai = 1;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public Integer getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Integer trangThai) {
        this.trangThai = trangThai;
    }

    public List<PhieuMuon> getPhieuMuons() {
        return phieuMuons;
    }

    public void setPhieuMuons(List<PhieuMuon> phieuMuons) {
        this.phieuMuons = phieuMuons;
    }

    // Thêm method để tương thích với code cũ
    public String getEmail() {
        return null;
    }
}