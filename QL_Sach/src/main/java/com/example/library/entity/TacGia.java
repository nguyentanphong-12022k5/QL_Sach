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
@Table(name = "tacgia")
public class TacGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaTacGia")
    private Long id;

    @Column(name = "TenTacGia", nullable = false)
    private String tenTacGia;

    @Column(name = "NamSinh")
    private String namSinh;

    @Column(name = "QueQuan")
    private String queQuan;

    @Column(name = "TrangThai")
    private Integer trangThai;

    @OneToMany(mappedBy = "tacGia")
    private List<Sach> saches;

    // Constructors
    public TacGia() {
    }

    public TacGia(String tenTacGia, String queQuan) {
        this.tenTacGia = tenTacGia;
        this.queQuan = queQuan;
        this.trangThai = 1;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenTacGia() {
        return tenTacGia;
    }

    public void setTenTacGia(String tenTacGia) {
        this.tenTacGia = tenTacGia;
    }

    public String getNamSinh() {
        return namSinh;
    }

    public void setNamSinh(String namSinh) {
        this.namSinh = namSinh;
    }

    public String getQueQuan() {
        return queQuan;
    }

    public void setQueQuan(String queQuan) {
        this.queQuan = queQuan;
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

    // Thêm method để tương thích
    public String getQuocGia() {
        return queQuan;
    }
}