package com.example.library.repository;

import com.example.library.entity.Sach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

@Repository
public interface SachRepository extends JpaRepository<Sach, Long> {
    
    // === THÊM @EntityGraph CHO findAll() ===
    @Override
    @EntityGraph(attributePaths = {"tacGia", "nhaXuatBan", "loaiSach", "keSach"})
    List<Sach> findAll();
    
    @EntityGraph(attributePaths = {"tacGia", "nhaXuatBan", "loaiSach", "keSach"})
    List<Sach> findByTenSachContainingIgnoreCase(String tenSach);
    
    @EntityGraph(attributePaths = {"tacGia", "nhaXuatBan", "loaiSach", "keSach"})
    List<Sach> findByTacGiaId(Long tacGiaId);
    
    @EntityGraph(attributePaths = {"tacGia", "nhaXuatBan", "loaiSach", "keSach"})
    List<Sach> findByLoaiSachId(Long loaiSachId);
    
    @EntityGraph(attributePaths = {"tacGia", "nhaXuatBan", "loaiSach", "keSach"})
    List<Sach> findByNhaXuatBanId(Long nxbId);
    
    @EntityGraph(attributePaths = {"tacGia", "nhaXuatBan", "loaiSach", "keSach"})
    @Query("SELECT s FROM Sach s WHERE " +
           "LOWER(s.tenSach) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.tacGia.tenTacGia) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.nhaXuatBan.tenNhaXuatBan) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Sach> searchByKeyword(@Param("keyword") String keyword);
    
    @EntityGraph(attributePaths = {"tacGia", "nhaXuatBan", "loaiSach", "keSach"})
    @Query("SELECT s FROM Sach s WHERE s.soLuong > 0")
    List<Sach> findAvailableBooks();
}