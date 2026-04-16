package com.example.library.service;

import com.example.library.dto.SachDTO;
import com.example.library.entity.Sach;
import com.example.library.repository.SachRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SachService {

    private final SachRepository sachRepository;

    public SachService(SachRepository sachRepository) {
        this.sachRepository = sachRepository;
    }

    public List<SachDTO> findAll() {
        return sachRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public SachDTO findById(Long id) {
        return sachRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public List<SachDTO> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAll();
        }
        return sachRepository.searchByKeyword(keyword.trim()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SachDTO> findAvailable() {
        return sachRepository.findAvailableBooks().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SachDTO> findWithFilters(String keyword, Long loaiSachId, String filter) {
        List<Sach> baseList;
        if (keyword != null && !keyword.trim().isEmpty()) {
            baseList = sachRepository.searchByKeyword(keyword.trim());
        } else {
            baseList = sachRepository.findAll();
        }

        return baseList.stream()
                .filter(s -> loaiSachId == null || (s.getLoaiSach() != null && s.getLoaiSach().getId().equals(loaiSachId)))
                .filter(s -> !"available".equals(filter) || s.getSoLuong() > 0)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SachDTO> findByTacGia(Long tacGiaId) {
        return sachRepository.findByTacGiaId(tacGiaId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SachDTO> findByLoaiSach(Long loaiSachId) {
        return sachRepository.findByLoaiSachId(loaiSachId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private SachDTO convertToDTO(Sach sach) {
        String tacGia = sach.getTacGia() != null ? sach.getTacGia().getTenTacGia() : "Chưa có";
        String nhaXuatBan = sach.getNhaXuatBan() != null ? sach.getNhaXuatBan().getTenNhaXuatBan() : "Chưa có";
        String loaiSach = sach.getLoaiSach() != null ? sach.getLoaiSach().getTenLoaiSach() : "Chưa có";
        String keSach = sach.getKeSach() != null ? sach.getKeSach().getViTri() : "Chưa có";
        String imageUrl = sach.getImageUrl() != null ? sach.getImageUrl() : "default-book.jpg";

        return new SachDTO(
                sach.getId(),
                sach.getTenSach(),
                tacGia,
                nhaXuatBan,
                loaiSach,
                keSach,
                imageUrl,
                sach.getSoLuong(),
                sach.getGiaMuon(),
                sach.getNamXB(),
                sach.getTrangThai()
        );
    }
}