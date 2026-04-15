package com.example.library.repository;

import com.example.library.entity.KeSach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface KeSachRepository extends JpaRepository<KeSach, Long> {
    List<KeSach> findByViTriContainingIgnoreCase(String viTri);
}