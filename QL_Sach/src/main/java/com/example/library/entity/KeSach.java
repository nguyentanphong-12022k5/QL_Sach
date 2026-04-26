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
@Table(name = "kesach")
public class KeSach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaKe")
    private Long id;

    @Column(name = "ViTri")
    private String viTri;

    @Column(name = "TrangThai")
    private Integer trangThai;

    @OneToMany(mappedBy = "keSach")
    private List<Sach> saches;

    // Constructors
    public KeSach() {
    }

    public KeSach(String viTri, int sucChua) {
        this.viTri = viTri;
        this.trangThai = 1;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getViTri() {
        return viTri;
    }

    public void setViTri(String viTri) {
        this.viTri = viTri;
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

    public int getSucChua() {
        return 100;
    } // Giá trị mặc định
}