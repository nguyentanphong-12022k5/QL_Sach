package com.example.library.repository;

import com.example.library.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Long> {
    Optional<TaiKhoan> findByUsername(String username);
    Optional<TaiKhoan> findByEmail(String email);
    Optional<TaiKhoan> findByResetToken(String resetToken);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<TaiKhoan> findByQuyen(Integer quyen);
    List<TaiKhoan> findByTrangThai(Integer trangThai);

    @Query(value = "SELECT MONTH(ngaytao) as month, COUNT(*) as count " +
                   "FROM taikhoan " +
                   "WHERE quyen = 3 AND ngaytao >= DATE_SUB(CURRENT_DATE, INTERVAL 6 MONTH) " +
                   "GROUP BY MONTH(ngaytao) " +
                   "ORDER BY month", nativeQuery = true)
    List<Object[]> getMonthlyNewReaderCount();
}