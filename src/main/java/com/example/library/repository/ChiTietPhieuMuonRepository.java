package com.example.library.repository;

import com.example.library.entity.ChiTietPhieuMuon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChiTietPhieuMuonRepository extends JpaRepository<ChiTietPhieuMuon, Long> {
    List<ChiTietPhieuMuon> findByPhieuMuonId(Long phieuMuonId);
}