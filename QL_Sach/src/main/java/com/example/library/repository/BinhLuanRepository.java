package com.example.library.repository;

import com.example.library.entity.BinhLuan;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BinhLuanRepository extends JpaRepository<BinhLuan, Long> {
    
    @EntityGraph(attributePaths = {"taiKhoan", "sach"})
    List<BinhLuan> findBySachIdAndTrangThaiOrderByNgayDangDesc(Long sachId, Integer trangThai);
    
    @EntityGraph(attributePaths = {"taiKhoan", "sach"})
    @Query("SELECT b FROM BinhLuan b WHERE b.sach IS NULL AND b.trangThai = 1 ORDER BY b.ngayDang DESC")
    List<BinhLuan> findGopYChung();
    
    @EntityGraph(attributePaths = {"taiKhoan", "sach"})
    List<BinhLuan> findByTaiKhoanIdAndTrangThaiOrderByNgayDangDesc(Long taiKhoanId, Integer trangThai);
    
    @Query("SELECT COUNT(b) FROM BinhLuan b WHERE b.sach.id = :sachId AND b.trangThai = :trangThai")
    long countBySachIdAndTrangThai(Long sachId, Integer trangThai);
    
    @EntityGraph(attributePaths = {"taiKhoan", "sach"})
    List<BinhLuan> findTop10ByTrangThaiOrderByNgayDangDesc(Integer trangThai);
    
    // === THÊM METHOD NÀY ===
    @EntityGraph(attributePaths = {"taiKhoan", "sach"})
    @Query("SELECT b FROM BinhLuan b ORDER BY b.ngayDang DESC")
    List<BinhLuan> findAllOrderByNgayDangDesc();
    
    @Override
    @EntityGraph(attributePaths = {"taiKhoan", "sach"})
    List<BinhLuan> findAll();
    
    @Override
    @EntityGraph(attributePaths = {"taiKhoan", "sach"})
    Optional<BinhLuan> findById(Long id);

    @Query("SELECT AVG(b.soSao) FROM BinhLuan b WHERE b.sach.id = :sachId AND b.trangThai = 1 AND b.soSao IS NOT NULL")
    Double findAverageRatingBySachId(Long sachId);

    @Query("SELECT COUNT(b) FROM BinhLuan b WHERE b.sach.id = :sachId AND b.trangThai = 1 AND b.soSao IS NOT NULL")
    Integer countRatingsBySachId(Long sachId);
}