package com.example.library.repository;

import com.example.library.entity.PhieuMuon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PhieuMuonRepository extends JpaRepository<PhieuMuon, Long> {
    
    List<PhieuMuon> findByDocGiaId(Long docGiaId);
    
    List<PhieuMuon> findByNgayTraIsNull();
    
    @Query("SELECT p FROM PhieuMuon p WHERE p.ngayTra IS NULL AND p.ngayMuon < :date")
    List<PhieuMuon> findQuaHan(@Param("date") LocalDate date);
    
    long countByNgayTraIsNull();
    
    @Query("SELECT COUNT(p) FROM PhieuMuon p WHERE p.ngayTra IS NULL AND p.ngayMuon < :date")
    long countQuaHan(@Param("date") LocalDate date);
}