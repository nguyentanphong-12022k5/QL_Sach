package com.example.library.dto;

import java.time.LocalDate;
import java.util.List;

public record PhieuNhapDTO(Long id, LocalDate ngayNhap, String nhaCungCap, String ghiChu,
        List<ChiTietPhieuNhapDTO> chiTiet) {
}
