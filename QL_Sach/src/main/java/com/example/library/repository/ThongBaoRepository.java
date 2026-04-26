package com.example.library.repository;

import com.example.library.entity.ThongBao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ThongBaoRepository extends JpaRepository<ThongBao, Long> {
    List<ThongBao> findByTaiKhoanIdOrderByNgayTaoDesc(Long taiKhoanId);
    long countByTaiKhoanIdAndDaDocFalse(Long taiKhoanId);
}
