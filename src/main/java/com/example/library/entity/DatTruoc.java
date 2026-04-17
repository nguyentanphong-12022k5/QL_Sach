package com.example.library.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "dattruoc")
public class DatTruoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MaSach", nullable = false)
    private Sach sach;

    @ManyToOne
    @JoinColumn(name = "MaDocGia", nullable = false)
    private DocGia docGia;

    @Column(name = "NgayDat")
    private LocalDateTime ngayDat = LocalDateTime.now();

    @Column(name = "NgayHen")
    private LocalDate ngayHen;

    @Column(name = "SoLuong")
    private Integer soLuong = 1;

    @Column(name = "GhiChu")
    private String ghiChu;

    /**
     * TrangThai:
     * 0: Chờ duyệt (Pending)
     * 1: Đã duyệt / Đang giữ sách (Confirmed)
     * 2: Đã mượn / Đã lấy sách (Borrowed)
     * 3: Đã hủy (Cancelled)
     */
    @Column(name = "TrangThai")
    private Integer trangThai = 0;

    // Constructors
    public DatTruoc() {}

    public DatTruoc(Sach sach, DocGia docGia, LocalDate ngayHen, Integer soLuong) {
        this.sach = sach;
        this.docGia = docGia;
        this.ngayHen = ngayHen;
        this.soLuong = soLuong;
        this.trangThai = 0;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Sach getSach() { return sach; }
    public void setSach(Sach sach) { this.sach = sach; }

    public DocGia getDocGia() { return docGia; }
    public void setDocGia(DocGia docGia) { this.docGia = docGia; }

    public LocalDateTime getNgayDat() { return ngayDat; }
    public void setNgayDat(LocalDateTime ngayDat) { this.ngayDat = ngayDat; }

    public LocalDate getNgayHen() { return ngayHen; }
    public void setNgayHen(LocalDate ngayHen) { this.ngayHen = ngayHen; }

    public Integer getSoLuong() { return soLuong; }
    public void setSoLuong(Integer soLuong) { this.soLuong = soLuong; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public Integer getTrangThai() { return trangThai; }
    public void setTrangThai(Integer trangThai) { this.trangThai = trangThai; }
}
