package com.example.library.service;

import com.example.library.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class ThongKeService {

    private final SachRepository sachRepository;
    private final DocGiaRepository docGiaRepository;
    private final PhieuMuonRepository phieuMuonRepository;
    private final PhieuNhapRepository phieuNhapRepository;
    private final TaiKhoanRepository taiKhoanRepository;

    public ThongKeService(SachRepository sachRepository,
                          DocGiaRepository docGiaRepository,
                          PhieuMuonRepository phieuMuonRepository,
                          PhieuNhapRepository phieuNhapRepository,
                          TaiKhoanRepository taiKhoanRepository) {
        this.sachRepository = sachRepository;
        this.docGiaRepository = docGiaRepository;
        this.phieuMuonRepository = phieuMuonRepository;
        this.phieuNhapRepository = phieuNhapRepository;
        this.taiKhoanRepository = taiKhoanRepository;
    }

    public Map<String, Object> getSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalSach", sachRepository.count());
        summary.put("totalDocGia", docGiaRepository.count());
        summary.put("totalPhieuMuon", phieuMuonRepository.count());
        summary.put("dangMuon", phieuMuonRepository.countByNgayTraIsNull());
        summary.put("quaHan", phieuMuonRepository.countQuaHan(LocalDate.now().minusDays(7)));
        summary.put("totalPhieuNhap", phieuNhapRepository.count());
        summary.put("totalTaiKhoan", taiKhoanRepository.count());
        summary.put("sachConTrongKho", sachRepository.findAvailableBooks().size());
        return summary;
    }

    public Map<String, Object> getDetailedStats() {
        Map<String, Object> stats = getSummary();
        stats.put("today", LocalDate.now());
        stats.put("sachSapHet", sachRepository.findAll().stream()
                .filter(s -> s.getSoLuong() != null && s.getSoLuong() < 5)
                .count());
        return stats;
    }
}