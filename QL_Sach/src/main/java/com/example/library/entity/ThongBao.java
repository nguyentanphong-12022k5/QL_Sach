package com.example.library.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "thong_bao")
public class ThongBao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tai_khoan_id", nullable = false)
    private Long taiKhoanId;

    @Column(name = "tieu_de", nullable = false)
    private String tieuDe;

    @Column(name = "noi_dung", columnDefinition = "LONGTEXT")
    private String noiDung;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao = LocalDateTime.now();

    @Column(name = "da_doc")
    private boolean daDoc = false;

    @Column(name = "loai")
    private String loai; // INFO, WARNING, SUCCESS, DANGER

    // Constructors
    public ThongBao() {}

    public ThongBao(Long taiKhoanId, String tieuDe, String noiDung, String loai) {
        this.taiKhoanId = taiKhoanId;
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;
        this.loai = loai;
        this.ngayTao = LocalDateTime.now();
        this.daDoc = false;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTaiKhoanId() { return taiKhoanId; }
    public void setTaiKhoanId(Long taiKhoanId) { this.taiKhoanId = taiKhoanId; }

    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }

    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }

    public LocalDateTime getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDateTime ngayTao) { this.ngayTao = ngayTao; }

    public boolean isDaDoc() { return daDoc; }
    public void setDaDoc(boolean daDoc) { this.daDoc = daDoc; }

    public String getLoai() { return loai; }
    public void setLoai(String loai) { this.loai = loai; }
}
