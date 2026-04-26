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
@Table(name = "loai")
public class LoaiSach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaLoai")
    private Long id;

    @Column(name = "TenLoai", nullable = false)
    private String tenLoaiSach;

    @Column(name = "TrangThai")
    private Integer trangThai;

    @OneToMany(mappedBy = "loaiSach")
    private List<Sach> saches;

    // Constructors
    public LoaiSach() {
    }

    public LoaiSach(String tenLoaiSach) {
        this.tenLoaiSach = tenLoaiSach;
        this.trangThai = 1;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenLoaiSach() {
        return tenLoaiSach;
    }

    public void setTenLoaiSach(String tenLoaiSach) {
        this.tenLoaiSach = tenLoaiSach;
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