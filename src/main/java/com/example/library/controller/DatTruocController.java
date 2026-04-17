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
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/dat-truoc")
public class DatTruocController {

    @Autowired private SachRepository sachRepository;
    @Autowired private DocGiaRepository docGiaRepository;
    @Autowired private DatTruocRepository datTruocRepository;
    @Autowired private TaiKhoanRepository taiKhoanRepository;

    @GetMapping("/{sachId}")
    public String datTruocForm(@PathVariable Long sachId, Model model) {
        Sach sach = sachRepository.findById(sachId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sách"));
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(username).orElse(null);
        
        DocGia docGia = null;
        if (taiKhoan != null) {
            // Logic tìm hoặc tạo DocGia tương ứng với TaiKhoan (giống MuonSachController)
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
        model.addAttribute("today", LocalDate.now());
        
        return "dat-truoc/form";
    }

    @PostMapping("/submit")
    public String submitDatTruoc(@RequestParam("sachId") Long sachId,
                                 @RequestParam("docGiaId") Long docGiaId,
                                 @RequestParam("ngayHen") String ngayHenStr,
                                 @RequestParam(value = "ghiChu", required = false) String ghiChu,
                                 RedirectAttributes redirect) {
        try {
            Sach sach = sachRepository.findById(sachId).orElseThrow();
            DocGia docGia = docGiaRepository.findById(docGiaId).orElseThrow();
            LocalDate ngayHen = LocalDate.parse(ngayHenStr);
            
            if (ngayHen.isBefore(LocalDate.now())) {
                redirect.addFlashAttribute("error", "Ngày hẹn không thể là ngày trong quá khứ!");
                return "redirect:/dat-truoc/" + sachId;
            }

            DatTruoc datTruoc = new DatTruoc();
            datTruoc.setSach(sach);
            datTruoc.setDocGia(docGia);
            datTruoc.setNgayDat(LocalDateTime.now());
            datTruoc.setNgayHen(ngayHen);
            datTruoc.setGhiChu(ghiChu);
            datTruoc.setTrangThai(0); // Chờ duyệt
            
            datTruocRepository.save(datTruoc);
            
            redirect.addFlashAttribute("success", "Đặt trước sách thành công! Vui lòng chờ quản trị viên duyệt.");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/dat-truoc/lich-su";
    }

    @GetMapping("/lich-su")
    public String lichSuDatTruoc(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(username).orElse(null);
        
        if (taiKhoan != null) {
            DocGia docGia = docGiaRepository.findAll().stream()
                    .filter(dg -> dg.getHoTen() != null && dg.getHoTen().equals(taiKhoan.getHoTen()))
                    .findFirst().orElse(null);
            
            if (docGia != null) {
                List<DatTruoc> list = datTruocRepository.findByDocGiaIdOrderByNgayDatDesc(docGia.getId());
                model.addAttribute("datTruocList", list);
            }
        }
        
        return "dat-truoc/lich-su";
    }

    @GetMapping("/cancel/{id}")
    public String cancelDatTruoc(@PathVariable Long id, RedirectAttributes redirect) {
        DatTruoc dt = datTruocRepository.findById(id).orElse(null);
        if (dt != null && dt.getTrangThai() == 0) {
            dt.setTrangThai(3); // Đã hủy
            datTruocRepository.save(dt);
            redirect.addFlashAttribute("success", "Đã hủy yêu cầu đặt trước.");
        } else {
            redirect.addFlashAttribute("error", "Không thể hủy yêu cầu này.");
        }
        return "redirect:/dat-truoc/lich-su";
    }
}
