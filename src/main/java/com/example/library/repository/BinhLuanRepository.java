package com.example.library.repository;

import com.example.library.entity.BinhLuan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BinhLuanRepository extends JpaRepository<BinhLuan, Long> {
    
    // Lấy bình luận theo sách (chỉ hiển thị)
    List<BinhLuan> findBySachIdAndTrangThaiOrderByNgayDangDesc(Long sachId, Integer trangThai);
    
    // Lấy góp ý chung (chỉ hiển thị)
    @Query("SELECT b FROM BinhLuan b WHERE b.sach IS NULL AND b.trangThai = 1 ORDER BY b.ngayDang DESC")
    List<BinhLuan> findGopYChung();
    
    // Lấy bình luận đã thu hồi của người dùng
    List<BinhLuan> findByTaiKhoanIdAndTrangThaiOrderByNgayDangDesc(Long taiKhoanId, Integer trangThai);
    
    long countBySachIdAndTrangThai(Long sachId, Integer trangThai);
    
    List<BinhLuan> findTop10ByTrangThaiOrderByNgayDangDesc(Integer trangThai);
    
    // Admin: Lấy tất cả bình luận (bao gồm cả đã thu hồi)
    @Query("SELECT b FROM BinhLuan b ORDER BY b.ngayDang DESC")
    List<BinhLuan> findAllOrderByNgayDangDesc();
}