package com.example.library.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "thanh_toan")
public class ThanhToan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "phieu_muon_id", nullable = false)
    private PhieuMuon phieuMuon;
    
    @ManyToOne
    @JoinColumn(name = "tai_khoan_id", nullable = false)
    private TaiKhoan taiKhoan;
    
    @Column(name = "so_tien", nullable = false)
    private Double soTien;
    
    @Column(name = "ly_do")
    private String lyDo = "Phí phạt trả sách quá hạn";
    
    @Column(name = "ngay_thanh_toan")
    private LocalDateTime ngayThanhToan;
    
    @Column(name = "phuong_thuc")
    private String phuongThuc = "TIEN_MAT"; // TIEN_MAT, CHUYEN_KHOAN
    
    @Column(name = "trang_thai")
    private Integer trangThai = 1; // 1: Thành công, 0: Đã hủy
    
    @Column(name = "ma_giao_dich", unique = true)
    private String maGiaoDich;
    
    @Column(name = "ghi_chu")
    private String ghiChu;
    
    // Constructors
    public ThanhToan() {
        this.ngayThanhToan = LocalDateTime.now();
        this.maGiaoDich = generateMaGiaoDich();
    }
    
    private String generateMaGiaoDich() {
        return "TT" + System.currentTimeMillis();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public PhieuMuon getPhieuMuon() { return phieuMuon; }
    public void setPhieuMuon(PhieuMuon phieuMuon) { this.phieuMuon = phieuMuon; }
    
    public TaiKhoan getTaiKhoan() { return taiKhoan; }
    public void setTaiKhoan(TaiKhoan taiKhoan) { this.taiKhoan = taiKhoan; }
    
    public Double getSoTien() { return soTien; }
    public void setSoTien(Double soTien) { this.soTien = soTien; }
    
    public String getLyDo() { return lyDo; }
    public void setLyDo(String lyDo) { this.lyDo = lyDo; }
    
    public LocalDateTime getNgayThanhToan() { return ngayThanhToan; }
    public void setNgayThanhToan(LocalDateTime ngayThanhToan) { this.ngayThanhToan = ngayThanhToan; }
    
    public String getPhuongThuc() { return phuongThuc; }
    public void setPhuongThuc(String phuongThuc) { this.phuongThuc = phuongThuc; }
    
    public Integer getTrangThai() { return trangThai; }
    public void setTrangThai(Integer trangThai) { this.trangThai = trangThai; }
    
    public String getMaGiaoDich() { return maGiaoDich; }
    public void setMaGiaoDich(String maGiaoDich) { this.maGiaoDich = maGiaoDich; }
    
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}