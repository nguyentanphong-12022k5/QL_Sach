package com.example.library.repository;

import com.example.library.entity.TacGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TacGiaRepository extends JpaRepository<TacGia, Long> {
    List<TacGia> findByTenTacGiaContainingIgnoreCase(String tenTacGia);
    List<TacGia> findByQueQuanContainingIgnoreCase(String queQuan);
}