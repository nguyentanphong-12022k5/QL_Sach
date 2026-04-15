package com.example.library.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "taikhoan")
public class TaiKhoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matk")
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "hoten", length = 100)
    private String hoTen;

    @Column(name = "quyen", nullable = false)
    private Integer quyen = 3;

    @Column(name = "trangthai")
    private Integer trangThai = 1;

    @Column(name = "ngaytao")
    private LocalDateTime ngayTao = LocalDateTime.now();

    @Column(name = "reset_token", length = 100)
    private String resetToken;

    // === THÊM FIELD AVATAR ===
    @Column(name = "avatar", length = 500)
    private String avatar;

    @Column(name = "sodienthoai", length = 20)
    private String soDienThoai;

    @Column(name = "diachi", length = 500)
    private String diaChi;

    // Constructors
    public TaiKhoan() {
    }

    public TaiKhoan(String username, String password, String email, String hoTen, Integer quyen) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.hoTen = hoTen;
        this.quyen = quyen;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public Integer getQuyen() { return quyen; }
    public void setQuyen(Integer quyen) { this.quyen = quyen; }

    public Integer getTrangThai() { return trangThai; }
    public void setTrangThai(Integer trangThai) { this.trangThai = trangThai; }

    public LocalDateTime getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDateTime ngayTao) { this.ngayTao = ngayTao; }

    public String getResetToken() { return resetToken; }
    public void setResetToken(String resetToken) { this.resetToken = resetToken; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
}