package com.example.library.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "binh_luan")
public class BinhLuan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "tai_khoan_id", nullable = false)
    private TaiKhoan taiKhoan;
    
    @ManyToOne
    @JoinColumn(name = "sach_id")
    private Sach sach;
    
    @Column(name = "noi_dung", nullable = false, columnDefinition = "TEXT")
    private String noiDung;
    
    @Column(name = "ngay_dang")
    private LocalDateTime ngayDang;
    
    @Column(name = "trang_thai")
    private Integer trangThai = 1; // 1: Hiển thị, 0: Ẩn
    
    @Column(name = "loai")
    private String loai = "GOP_Y"; // GOP_Y: Góp ý chung, SACH: Bình luận sách
    
    // Constructors
    public BinhLuan() {
        this.ngayDang = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public TaiKhoan getTaiKhoan() { return taiKhoan; }
    public void setTaiKhoan(TaiKhoan taiKhoan) { this.taiKhoan = taiKhoan; }
    
    public Sach getSach() { return sach; }
    public void setSach(Sach sach) { this.sach = sach; }
    
    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }
    
    public LocalDateTime getNgayDang() { return ngayDang; }
    public void setNgayDang(LocalDateTime ngayDang) { this.ngayDang = ngayDang; }
    
    public Integer getTrangThai() { return trangThai; }
    public void setTrangThai(Integer trangThai) { this.trangThai = trangThai; }
    
    public String getLoai() { return loai; }
    public void setLoai(String loai) { this.loai = loai; }
}