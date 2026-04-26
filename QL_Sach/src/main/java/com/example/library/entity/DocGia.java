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

    // --- Hệ thống Cấp bậc "Tu Tiên" ---
    public int getTongSachMuon() {
        if (phieuMuons == null) return 0;
        int total = 0;
        for (PhieuMuon pm : phieuMuons) {
            if (pm.getChiTietPhieuMuons() != null) {
                total += pm.getChiTietPhieuMuons().size();
            }
        }
        return total;
    }

    public String getTenCapBac() {
        int count = getTongSachMuon();
        if (count == 0) return "Phàm Nhân";
        if (count <= 10) return "Luyện Khí";
        if (count <= 30) return "Trúc Cơ";
        if (count <= 70) return "Kim Đan";
        if (count <= 150) return "Nguyên Anh";
        if (count <= 300) return "Hóa Thần";
        if (count <= 600) return "Luyện Hư";
        if (count <= 1000) return "Hợp Thể";
        if (count <= 2000) return "Đại Thừa";
        return "Độ Kiếp";
    }

    public String getRankColor() {
        int count = getTongSachMuon();
        if (count == 0) return "#94a3b8"; // Phàm Nhân - Slate
        if (count <= 10) return "#10b981"; // Luyện Khí - Emerald
        if (count <= 30) return "#3b82f6"; // Trúc Cơ - Blue
        if (count <= 70) return "#f59e0b"; // Kim Đan - Amber
        if (count <= 150) return "#8b5cf6"; // Nguyên Anh - Violet
        if (count <= 300) return "#ec4899"; // Hóa Thần - Pink
        if (count <= 600) return "#06b6d4"; // Luyện Hư - Cyan
        if (count <= 1000) return "#f43f5e"; // Hợp Thể - Rose
        if (count <= 2000) return "#facc15"; // Đại Thừa - Yellow
        return "#ffffff"; // Độ Kiếp - White (Glow)
    }

    public String getRankTextColor() {
        String bgColor = getRankColor();
        // Kim Đan và Đại Thừa dùng chữ tối
        if ("#f59e0b".equals(bgColor) || "#facc15".equals(bgColor) || "#ffffff".equals(bgColor)) return "#1e293b";
        return "#ffffff";
    }
}