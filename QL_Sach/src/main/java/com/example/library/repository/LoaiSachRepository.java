package com.example.library.repository;

import com.example.library.entity.LoaiSach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LoaiSachRepository extends JpaRepository<LoaiSach, Long> {
    List<LoaiSach> findByTenLoaiSachContainingIgnoreCase(String tenLoaiSach);
}