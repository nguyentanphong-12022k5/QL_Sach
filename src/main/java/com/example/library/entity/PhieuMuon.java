package com.example.library.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "phieu_muon")
public class PhieuMuon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doc_gia_id", nullable = false)
    private DocGia docGia;

    @Column(name = "ngay_muon", nullable = false)
    private LocalDate ngayMuon;

    @Column(name = "ngay_tra")
    private LocalDate ngayTra;

    @Column(name = "ngay_tra_du_kien")
    private LocalDate ngayTraDuKien;

    @Column(name = "tien_phat")
    private Double tienPhat;

    @OneToMany(mappedBy = "phieuMuon", cascade = CascadeType.ALL)
    private List<ChiTietPhieuMuon> chiTietPhieuMuons;

    // Constructors
    public PhieuMuon() {
    }

    public PhieuMuon(DocGia docGia, LocalDate ngayMuon, LocalDate ngayTra) {
        this.docGia = docGia;
        this.ngayMuon = ngayMuon;
        this.ngayTra = ngayTra;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DocGia getDocGia() {
        return docGia;
    }

    public void setDocGia(DocGia docGia) {
        this.docGia = docGia;
    }

    public LocalDate getNgayMuon() {
        return ngayMuon;
    }

    public void setNgayMuon(LocalDate ngayMuon) {
        this.ngayMuon = ngayMuon;
    }

    public LocalDate getNgayTra() {
        return ngayTra;
    }

    public void setNgayTra(LocalDate ngayTra) {
        this.ngayTra = ngayTra;
    }

    public List<ChiTietPhieuMuon> getChiTietPhieuMuons() {
        return chiTietPhieuMuons;
    }

    public void setChiTietPhieuMuons(List<ChiTietPhieuMuon> chiTietPhieuMuons) {
        this.chiTietPhieuMuons = chiTietPhieuMuons;
    }

    public LocalDate getNgayTraDuKien() {
        return ngayTraDuKien;
    }

    public void setNgayTraDuKien(LocalDate ngayTraDuKien) {
        this.ngayTraDuKien = ngayTraDuKien;
    }

    public Double getTienPhat() {
        return tienPhat;
    }

    public void setTienPhat(Double tienPhat) {
        this.tienPhat = tienPhat;
    }
}