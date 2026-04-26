package com.example.library.repository;

import com.example.library.entity.ChiTietPhieuNhap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChiTietPhieuNhapRepository extends JpaRepository<ChiTietPhieuNhap, Long> {
    List<ChiTietPhieuNhap> findByPhieuNhapId(Long phieuNhapId);
}