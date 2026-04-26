package com.example.library.repository;

import com.example.library.entity.PhieuNhap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PhieuNhapRepository extends JpaRepository<PhieuNhap, Long> {
    List<PhieuNhap> findByNgayNhapBetween(LocalDate start, LocalDate end);
    List<PhieuNhap> findByNhaCungCapContainingIgnoreCase(String nhaCungCap);
}