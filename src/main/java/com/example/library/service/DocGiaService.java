package com.example.library.service;

import com.example.library.dto.DocGiaDTO;
import com.example.library.entity.DocGia;
import com.example.library.repository.DocGiaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocGiaService {

    private final DocGiaRepository docGiaRepository;

    public DocGiaService(DocGiaRepository docGiaRepository) {
        this.docGiaRepository = docGiaRepository;
    }

    public List<DocGiaDTO> findAll() {
        return docGiaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DocGiaDTO findById(Long id) {
        return docGiaRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public List<DocGiaDTO> search(String keyword) {
        return docGiaRepository.findByHoTenContainingIgnoreCaseOrSoDienThoaiContaining(keyword, keyword).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private DocGiaDTO convertToDTO(DocGia docGia) {
        return new DocGiaDTO(
                docGia.getId(),
                docGia.getHoTen(),
                docGia.getEmail(),
                docGia.getSoDienThoai()
        );
    }
}