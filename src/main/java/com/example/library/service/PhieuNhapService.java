package com.example.library.service;

import com.example.library.dto.*;
import com.example.library.entity.PhieuNhap;
import com.example.library.repository.PhieuNhapRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhieuNhapService {

    private final PhieuNhapRepository phieuNhapRepository;

    public PhieuNhapService(PhieuNhapRepository phieuNhapRepository) {
        this.phieuNhapRepository = phieuNhapRepository;
    }

    public List<PhieuNhapDTO> findAll() {
        return phieuNhapRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PhieuNhapDTO findById(Long id) {
        return phieuNhapRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    private PhieuNhapDTO convertToDTO(PhieuNhap phieuNhap) {
        List<ChiTietPhieuNhapDTO> chiTietDTOs = phieuNhap.getChiTietPhieuNhaps().stream()
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
                    return new ChiTietPhieuNhapDTO(chiTiet.getId(), sachDTO, chiTiet.getSoLuong(), chiTiet.getDonGia());
                })
                .collect(Collectors.toList());

        return new PhieuNhapDTO(
                phieuNhap.getId(),
                phieuNhap.getNgayNhap(),
                phieuNhap.getNhaCungCap(),
                phieuNhap.getGhiChu(),
                chiTietDTOs
        );
    }
}