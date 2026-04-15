package com.example.library.repository;

import com.example.library.entity.Sach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SachRepository extends JpaRepository<Sach, Long> {
    
    List<Sach> findByTenSachContainingIgnoreCase(String tenSach);
    
    List<Sach> findByTacGiaId(Long tacGiaId);
    
    List<Sach> findByLoaiSachId(Long loaiSachId);
    
    List<Sach> findByNhaXuatBanId(Long nxbId);
    
    @Query("SELECT s FROM Sach s WHERE " +
           "LOWER(s.tenSach) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.tacGia.tenTacGia) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.nhaXuatBan.tenNhaXuatBan) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Sach> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT s FROM Sach s WHERE s.soLuong > 0")
    List<Sach> findAvailableBooks();
}