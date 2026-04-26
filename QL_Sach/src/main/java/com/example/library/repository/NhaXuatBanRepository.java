package com.example.library.repository;

import com.example.library.entity.NhaXuatBan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NhaXuatBanRepository extends JpaRepository<NhaXuatBan, Long> {
    List<NhaXuatBan> findByTenNhaXuatBanContainingIgnoreCase(String tenNhaXuatBan);
}