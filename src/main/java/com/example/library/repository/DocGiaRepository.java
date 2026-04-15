package com.example.library.repository;

import com.example.library.entity.DocGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DocGiaRepository extends JpaRepository<DocGia, Long> {
    @Query("SELECT d FROM DocGia d WHERE LOWER(d.hoTen) LIKE LOWER(CONCAT('%', :hoTen, '%')) OR d.soDienThoai LIKE CONCAT('%', :sdt, '%')")
    List<DocGia> findByHoTenContainingIgnoreCaseOrSoDienThoaiContaining(@Param("hoTen") String hoTen, @Param("sdt") String sdt);
}