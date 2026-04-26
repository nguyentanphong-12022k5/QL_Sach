package com.example.library.repository;

import com.example.library.entity.ThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ThanhToanRepository extends JpaRepository<ThanhToan, Long> {
    
    List<ThanhToan> findByPhieuMuonId(Long phieuMuonId);
    
    List<ThanhToan> findByTaiKhoanId(Long taiKhoanId);
    
    List<ThanhToan> findByTrangThai(Integer trangThai);
    
    @Query("SELECT SUM(t.soTien) FROM ThanhToan t WHERE t.trangThai = 1")
    Double getTongDoanhThu();
    
    @Query("SELECT SUM(t.soTien) FROM ThanhToan t WHERE t.trangThai = 1 AND DATE(t.ngayThanhToan) = CURRENT_DATE")
    Double getDoanhThuHomNay();
    
    @Query("SELECT SUM(t.soTien) FROM ThanhToan t WHERE t.trangThai = 1 AND t.ngayThanhToan BETWEEN :start AND :end")
    Double getDoanhThuTheoKhoangThoiGian(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT t FROM ThanhToan t WHERE t.trangThai = 1 ORDER BY t.ngayThanhToan DESC")
    List<ThanhToan> findRecentPayments();

    @Query(value = "SELECT MONTH(ngay_thanh_toan) as month, SUM(so_tien) as total " +
                   "FROM thanh_toan " +
                   "WHERE trang_thai = 1 AND ngay_thanh_toan >= DATE_SUB(CURRENT_DATE, INTERVAL 6 MONTH) " +
                   "GROUP BY MONTH(ngay_thanh_toan) " +
                   "ORDER BY month", nativeQuery = true)
    List<Object[]> getMonthlyRevenue();
}