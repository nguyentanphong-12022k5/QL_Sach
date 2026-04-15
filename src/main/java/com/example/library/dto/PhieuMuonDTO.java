package com.example.library.dto;

import java.time.LocalDate;
import java.util.List;

public record PhieuMuonDTO(Long id, DocGiaDTO docGia, LocalDate ngayMuon, LocalDate ngayTra,
        List<ChiTietPhieuMuonDTO> chiTiet) {
}
