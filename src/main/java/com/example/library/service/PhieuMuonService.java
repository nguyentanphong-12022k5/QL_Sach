package com.example.library.service;

import com.example.library.dto.*;
import com.example.library.entity.PhieuMuon;
import com.example.library.repository.PhieuMuonRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhieuMuonService {

    private final PhieuMuonRepository phieuMuonRepository;

    public PhieuMuonService(PhieuMuonRepository phieuMuonRepository) {
        this.phieuMuonRepository = phieuMuonRepository;
    }

    public List<PhieuMuonDTO> findAll() {
        return phieuMuonRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PhieuMuonDTO findById(Long id) {
        return phieuMuonRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public List<PhieuMuonDTO> findByDocGiaId(Long docGiaId) {
        return phieuMuonRepository.findByDocGiaId(docGiaId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PhieuMuonDTO> findDangMuon() {
        return phieuMuonRepository.findByNgayTraIsNull().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PhieuMuonDTO> findQuaHan() {
        return phieuMuonRepository.findQuaHan(LocalDate.now().minusDays(7)).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private PhieuMuonDTO convertToDTO(PhieuMuon phieuMuon) {
        DocGiaDTO docGiaDTO = new DocGiaDTO(
                phieuMuon.getDocGia().getId(),
                phieuMuon.getDocGia().getHoTen(),
                phieuMuon.getDocGia().getEmail(),
                phieuMuon.getDocGia().getSoDienThoai()
        );

        List<ChiTietPhieuMuonDTO> chiTietDTOs = phieuMuon.getChiTietPhieuMuons().stream()
                .map(chiTiet -> {
                    SachDTO sachDTO = new SachDTO(
                            chiTiet.getSach().getId(),
                            chiTiet.getSach().getTenSach(),
                            chiTiet.getSach().getTacGia() != null ? chiTiet.getSach().getTacGia().getTenTacGia() : null,
                            chiTiet.getSach().getNhaXuatBan() != null ? chiTiet.getSach().getNhaXuatBan().getTenNhaXuatBan() : null,
                            chiTiet.getSach().getLoaiSach() != null ? chiTiet.getSach().getLoaiSach().getTenLoaiSach() : null,
                            chiTiet.getSach().getKeSach() != null ? chiTiet.getSach().getKeSach().getViTri() : null,
                            chiTiet.getSach().getImageUrl()
                    );
                    return new ChiTietPhieuMuonDTO(chiTiet.getId(), sachDTO, chiTiet.getSoLuong());
                })
                .collect(Collectors.toList());

        return new PhieuMuonDTO(
                phieuMuon.getId(),
                docGiaDTO,
                phieuMuon.getNgayMuon(),
                phieuMuon.getNgayTra(),
                chiTietDTOs
        );
    }
}