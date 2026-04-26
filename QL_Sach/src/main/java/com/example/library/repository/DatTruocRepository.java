package com.example.library.repository;

import com.example.library.entity.DatTruoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DatTruocRepository extends JpaRepository<DatTruoc, Long> {
    List<DatTruoc> findByDocGiaIdOrderByNgayDatDesc(Long docGiaId);
    List<DatTruoc> findByTrangThaiOrderByNgayDatDesc(Integer trangThai);
    List<DatTruoc> findBySachIdAndTrangThai(Long sachId, Integer trangThai);
}
