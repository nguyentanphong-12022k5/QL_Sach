package com.example.library.controller;

import com.example.library.entity.*;
import com.example.library.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/muon-sach")
public class MuonSachController {

    @Autowired private SachRepository sachRepository;
    @Autowired private DocGiaRepository docGiaRepository;
    @Autowired private PhieuMuonRepository phieuMuonRepository;
    @Autowired private ChiTietPhieuMuonRepository chiTietPhieuMuonRepository;
    @Autowired private TaiKhoanRepository taiKhoanRepository;

    @GetMapping("/{sachId}")
    public String muonSachForm(@PathVariable Long sachId, Model model) {
        Sach sach = sachRepository.findById(sachId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sách"));
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(username).orElse(null);
        
        DocGia docGia = null;
        if (taiKhoan != null) {
            docGia = docGiaRepository.findAll().stream()
                    .filter(dg -> dg.getHoTen() != null && dg.getHoTen().equals(taiKhoan.getHoTen()))
                    .findFirst().orElse(null);
            
            if (docGia == null) {
                docGia = new DocGia();
                docGia.setHoTen(taiKhoan.getHoTen() != null ? taiKhoan.getHoTen() : username);
                docGia.setSoDienThoai(taiKhoan.getSoDienThoai());
                docGia.setDiaChi(taiKhoan.getDiaChi());
                docGia.setTrangThai(1);
                docGia = docGiaRepository.save(docGia);
            }
        }
        
        model.addAttribute("sach", sach);
        model.addAttribute("docGia", docGia);
        model.addAttribute("docGiaList", docGiaRepository.findAll());
        model.addAttribute("today", LocalDate.now());
        
        return "muon-sach/form";
    }

    @PostMapping("/submit")
    public String submitMuonSach(@RequestParam("sachId") Long sachId,
                                  @RequestParam("docGiaId") Long docGiaId,
                                  @RequestParam("soLuong") int soLuong,
                                  RedirectAttributes redirect) {
        try {
            Sach sach = sachRepository.findById(sachId).orElseThrow();
            DocGia docGia = docGiaRepository.findById(docGiaId).orElseThrow();
            
            if (sach.getSoLuong() == null || sach.getSoLuong() < soLuong) {
                redirect.addFlashAttribute("error", "Sách không đủ số lượng!");
                return "redirect:/sach";
            }
            
            PhieuMuon phieuMuon = new PhieuMuon();
            phieuMuon.setDocGia(docGia);
            phieuMuon.setNgayMuon(LocalDate.now());
            phieuMuon.setNgayTra(null);
            PhieuMuon saved = phieuMuonRepository.save(phieuMuon);
            
            ChiTietPhieuMuon chiTiet = new ChiTietPhieuMuon();
            chiTiet.setPhieuMuon(saved);
            chiTiet.setSach(sach);
            chiTiet.setSoLuong(soLuong);
            chiTietPhieuMuonRepository.save(chiTiet);
            
            sach.setSoLuong(sach.getSoLuong() - soLuong);
            if (sach.getSoLuong() == 0) sach.setTrangThai("0");
            sachRepository.save(sach);
            
            redirect.addFlashAttribute("success", "Mượn sách thành công! Mã phiếu: #" + saved.getId());
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/sach";
    }
}