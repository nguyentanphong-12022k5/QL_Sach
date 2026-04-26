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
@Table(name = "nhaxuatban")
public class NhaXuatBan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaNXB")
    private Long id;

    @Column(name = "TenNXB", nullable = false)
    private String tenNhaXuatBan;

    @Column(name = "DiaChi")
    private String diaChi;

    @Column(name = "Sdt")
    private String sdt;

    @Column(name = "TrangThai")
    private Integer trangThai;

    @OneToMany(mappedBy = "nhaXuatBan")
    private List<Sach> saches;

    // Constructors
    public NhaXuatBan() {
    }

    public NhaXuatBan(String tenNhaXuatBan, String diaChi) {
        this.tenNhaXuatBan = tenNhaXuatBan;
        this.diaChi = diaChi;
        this.trangThai = 1;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenNhaXuatBan() {
        return tenNhaXuatBan;
    }

    public void setTenNhaXuatBan(String tenNhaXuatBan) {
        this.tenNhaXuatBan = tenNhaXuatBan;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public Integer getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Integer trangThai) {
        this.trangThai = trangThai;
    }

    public List<Sach> getSaches() {
        return saches;
    }

    public void setSaches(List<Sach> saches) {
        this.saches = saches;
    }
}