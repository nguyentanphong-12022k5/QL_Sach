package com.example.library.controller;

import com.example.library.entity.*;
import com.example.library.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/dat-truoc")
public class AdminDatTruocController {

    @Autowired private DatTruocRepository datTruocRepository;
    @Autowired private PhieuMuonRepository phieuMuonRepository;
    @Autowired private ChiTietPhieuMuonRepository chiTietPhieuMuonRepository;
    @Autowired private SachRepository sachRepository;

    @GetMapping
    public String list(@RequestParam(value = "status", required = false) Integer status, Model model) {
        List<DatTruoc> list;
        if (status != null) {
            list = datTruocRepository.findByTrangThaiOrderByNgayDatDesc(status);
        } else {
            list = datTruocRepository.findAll();
        }
        model.addAttribute("datTruocList", list);
        return "admin/dat-truoc/list";
    }

    @GetMapping("/approve/{id}")
    public String approve(@PathVariable Long id, RedirectAttributes redirect) {
        DatTruoc dt = datTruocRepository.findById(id).orElse(null);
        if (dt != null && dt.getTrangThai() == 0) {
            dt.setTrangThai(1); // Đã duyệt
            datTruocRepository.save(dt);
            redirect.addFlashAttribute("success", "Đã duyệt yêu cầu đặt trước!");
        }
        return "redirect:/admin/dat-truoc";
    }

    @GetMapping("/reject/{id}")
    public String reject(@PathVariable Long id, RedirectAttributes redirect) {
        DatTruoc dt = datTruocRepository.findById(id).orElse(null);
        if (dt != null && (dt.getTrangThai() == 0 || dt.getTrangThai() == 1)) {
            dt.setTrangThai(3); // Đã hủy/từ chối
            datTruocRepository.save(dt);
            redirect.addFlashAttribute("success", "Đã từ chối/hủy yêu cầu đặt trước.");
        }
        return "redirect:/admin/dat-truoc";
    }

    @GetMapping("/convert/{id}")
    public String convertToBorrow(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            DatTruoc dt = datTruocRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu"));
            
            if (dt.getTrangThai() != 1) {
                throw new RuntimeException("Yêu cầu phải được duyệt trước khi chuyển thành phiếu mượn.");
            }

            Sach sach = dt.getSach();
            if (sach.getSoLuong() < dt.getSoLuong()) {
                throw new RuntimeException("Sách hiện tại không đủ số lượng để cho mượn!");
            }

            // Tạo phiếu mượn
            PhieuMuon phieuMuon = new PhieuMuon();
            phieuMuon.setDocGia(dt.getDocGia());
            phieuMuon.setNgayMuon(LocalDate.now());
            phieuMuon.setNgayTraDuKien(LocalDate.now().plusDays(7));
            phieuMuon.setTienPhat(5000.0);
            PhieuMuon savedPhieu = phieuMuonRepository.save(phieuMuon);

            // Tạo chi tiết phiếu mượn
            ChiTietPhieuMuon ct = new ChiTietPhieuMuon();
            ct.setPhieuMuon(savedPhieu);
            ct.setSach(sach);
            ct.setSoLuong(dt.getSoLuong());
            ct.setGiaMuon(sach.getGiaMuon() != null ? sach.getGiaMuon() : 0.0);
            chiTietPhieuMuonRepository.save(ct);

            // Cập nhật số lượng sách
            sach.setSoLuong(sach.getSoLuong() - dt.getSoLuong());
            if (sach.getSoLuong() == 0) sach.setTrangThai("0");
            sachRepository.save(sach);

            // Cập nhật trạng thái yêu cầu đặt trước
            dt.setTrangThai(2); // Đã nhận sách
            datTruocRepository.save(dt);

            redirect.addFlashAttribute("success", "Đã chuyển yêu cầu đặt trước thành phiếu mượn thành công!");
            return "redirect:/phieumuon/view/" + savedPhieu.getId();
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/admin/dat-truoc";
        }
    }
}
