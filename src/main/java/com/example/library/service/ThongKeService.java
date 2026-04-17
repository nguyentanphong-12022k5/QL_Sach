package com.example.library.service;

import com.example.library.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ThongKeService {

    private final SachRepository sachRepository;
    private final DocGiaRepository docGiaRepository;
    private final PhieuMuonRepository phieuMuonRepository;
    private final PhieuNhapRepository phieuNhapRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    private final ThanhToanRepository thanhToanRepository;

    public ThongKeService(SachRepository sachRepository,
                          DocGiaRepository docGiaRepository,
                          PhieuMuonRepository phieuMuonRepository,
                          PhieuNhapRepository phieuNhapRepository,
                          TaiKhoanRepository taiKhoanRepository,
                          ThanhToanRepository thanhToanRepository) {
        this.sachRepository = sachRepository;
        this.docGiaRepository = docGiaRepository;
        this.phieuMuonRepository = phieuMuonRepository;
        this.phieuNhapRepository = phieuNhapRepository;
        this.taiKhoanRepository = taiKhoanRepository;
        this.thanhToanRepository = thanhToanRepository;
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
        stats.put("chartData", getChartData());
        return stats;
    }

    private Map<String, Object> getChartData() {
        Map<String, Object> chartData = new HashMap<>();
        
        // Months labels (1-12)
        chartData.put("revenue", formatMonthlyData(thanhToanRepository.getMonthlyRevenue()));
        chartData.put("borrows", formatMonthlyData(phieuMuonRepository.getMonthlyBorrowCount()));
        chartData.put("readers", formatMonthlyData(taiKhoanRepository.getMonthlyNewReaderCount()));
        
        return chartData;
    }

    private Map<Integer, Object> formatMonthlyData(List<Object[]> queryResult) {
        Map<Integer, Object> data = new HashMap<>();
        // Initialize with 0 for last 6 months
        LocalDate now = LocalDate.now();
        for (int i = 0; i < 6; i++) {
            data.put(now.minusMonths(i).getMonthValue(), 0);
        }
        
        for (Object[] row : queryResult) {
            if (row != null && row.length >= 2) {
                Integer month = ((Number) row[0]).intValue();
                Object value = row[1];
                data.put(month, value);
            }
        }
        return data;
    }
}