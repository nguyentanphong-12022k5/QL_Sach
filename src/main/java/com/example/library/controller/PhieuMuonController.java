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
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/phieumuon")
public class PhieuMuonController {

    @Autowired
    private PhieuMuonRepository phieuMuonRepository;
    @Autowired
    private DocGiaRepository docGiaRepository;
    @Autowired
    private SachRepository sachRepository;
    @Autowired
    private ChiTietPhieuMuonRepository chiTietPhieuMuonRepository;
    @Autowired
    private ThanhToanRepository thanhToanRepository;
    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @GetMapping
    public String list(@RequestParam(value = "status", required = false) String status, Model model) {
        List<PhieuMuon> list;
        if ("dangmuon".equals(status)) {
            list = phieuMuonRepository.findByNgayTraIsNull();
        } else if ("quahan".equals(status)) {
            list = phieuMuonRepository.findQuaHan(LocalDate.now().minusDays(7));
        } else {
            list = phieuMuonRepository.findAll();
        }
        model.addAttribute("phieuMuonList", list);
        model.addAttribute("today", LocalDate.now());
        return "phieumuon/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("phieuMuon", new PhieuMuon());
        model.addAttribute("docGiaList", docGiaRepository.findAll());
        model.addAttribute("sachList", sachRepository.findAvailableBooks());
        model.addAttribute("today", LocalDate.now());
        return "phieumuon/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute PhieuMuon phieuMuon, @RequestParam("docGiaId") Long docGiaId,
            @RequestParam("sachIds") List<Long> sachIds, @RequestParam("soLuongs") List<Integer> soLuongs,
            @RequestParam("ngayTraDuKien") String ngayTraDuKien, RedirectAttributes redirect) {
        try {
            DocGia docGia = docGiaRepository.findById(docGiaId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy độc giả"));

            phieuMuon.setDocGia(docGia);
            phieuMuon.setNgayMuon(LocalDate.now());
            phieuMuon.setNgayTra(null);

            PhieuMuon saved = phieuMuonRepository.save(phieuMuon);

            List<ChiTietPhieuMuon> chiTiets = new ArrayList<>();
            for (int i = 0; i < sachIds.size(); i++) {
                Sach sach = sachRepository.findById(sachIds.get(i)).orElseThrow();
                int soLuong = soLuongs.get(i);

                if (sach.getSoLuong() < soLuong) {
                    throw new RuntimeException("Sách '" + sach.getTenSach() + "' không đủ số lượng!");
                }

                sach.setSoLuong(sach.getSoLuong() - soLuong);
                sachRepository.save(sach);

                ChiTietPhieuMuon chiTiet = new ChiTietPhieuMuon();
                chiTiet.setPhieuMuon(saved);
                chiTiet.setSach(sach);
                chiTiet.setSoLuong(soLuong);
                chiTiets.add(chiTiet);
            }
            chiTietPhieuMuonRepository.saveAll(chiTiets);

            redirect.addFlashAttribute("success", "Tạo phiếu mượn thành công!");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/phieumuon";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        PhieuMuon phieuMuon = phieuMuonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu mượn"));
        model.addAttribute("phieuMuon", phieuMuon);
        model.addAttribute("chiTiets", chiTietPhieuMuonRepository.findByPhieuMuonId(id));
        return "phieumuon/view";
    }

    @GetMapping("/return/{id}")
    public String returnBook(@PathVariable Long id, RedirectAttributes redirect) {
        PhieuMuon phieuMuon = phieuMuonRepository.findById(id).orElse(null);
        if (phieuMuon != null) {
            phieuMuon.setNgayTra(LocalDate.now());

            // Trả lại số lượng sách
            for (ChiTietPhieuMuon chiTiet : phieuMuon.getChiTietPhieuMuons()) {
                Sach sach = chiTiet.getSach();
                sach.setSoLuong(sach.getSoLuong() + chiTiet.getSoLuong());
                sachRepository.save(sach);
            }

            phieuMuonRepository.save(phieuMuon);
            redirect.addFlashAttribute("success", "Đã xác nhận trả sách!");
        }
        return "redirect:/phieumuon";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        if (!chiTietPhieuMuonRepository.findByPhieuMuonId(id).isEmpty()) {
            chiTietPhieuMuonRepository.deleteAll(chiTietPhieuMuonRepository.findByPhieuMuonId(id));
        }
        phieuMuonRepository.deleteById(id);
        redirect.addFlashAttribute("success", "Xóa phiếu mượn thành công!");
        return "redirect:/phieumuon";
    }

    // Trang thanh toán khi trả sách
    @GetMapping("/thanh-toan/{id}")
    public String thanhToanForm(@PathVariable Long id, Model model) {
        PhieuMuon phieuMuon = phieuMuonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu mượn"));

        // Tính số ngày quá hạn
        LocalDate ngayMuon = phieuMuon.getNgayMuon();
        LocalDate ngayTraDuKien = ngayMuon.plusDays(7);
        LocalDate ngayTraThucTe = LocalDate.now();

        long soNgayQuaHan = 0;
        double tienPhat = 0;

        if (ngayTraThucTe.isAfter(ngayTraDuKien)) {
            soNgayQuaHan = java.time.temporal.ChronoUnit.DAYS.between(ngayTraDuKien, ngayTraThucTe);
            tienPhat = soNgayQuaHan * 5000; // 5,000 VND/ngày
        }

        // Tổng số sách mượn
        int tongSach = phieuMuon.getChiTietPhieuMuons().stream()
                .mapToInt(ChiTietPhieuMuon::getSoLuong).sum();
        double tongTienPhat = tienPhat * tongSach;

        model.addAttribute("phieuMuon", phieuMuon);
        model.addAttribute("soNgayQuaHan", soNgayQuaHan);
        model.addAttribute("tongSach", tongSach);
        model.addAttribute("tienPhatMoiSach", tienPhat);
        model.addAttribute("tongTienPhat", tongTienPhat);
        model.addAttribute("ngayTraDuKien", ngayTraDuKien);
        model.addAttribute("ngayTraThucTe", ngayTraThucTe);

        return "phieumuon/thanh-toan";
    }

    // Xử lý thanh toán
    @PostMapping("/thanh-toan/{id}")
    public String xuLyThanhToan(@PathVariable Long id, @RequestParam("phuongThuc") String phuongThuc,
            @RequestParam("soTien") Double soTien, @RequestParam(value = "ghiChu", required = false) String ghiChu,
            RedirectAttributes redirect) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

            PhieuMuon phieuMuon = phieuMuonRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu mượn"));

            // Tạo thanh toán
            ThanhToan thanhToan = new ThanhToan();
            thanhToan.setPhieuMuon(phieuMuon);
            thanhToan.setTaiKhoan(taiKhoan);
            thanhToan.setSoTien(soTien);
            thanhToan.setPhuongThuc(phuongThuc);
            thanhToan.setGhiChu(ghiChu);
            thanhToan.setTrangThai(1);

            thanhToanRepository.save(thanhToan);

            // Cập nhật ngày trả
            phieuMuon.setNgayTra(LocalDate.now());

            // Trả lại số lượng sách
            for (ChiTietPhieuMuon chiTiet : phieuMuon.getChiTietPhieuMuons()) {
                Sach sach = chiTiet.getSach();
                sach.setSoLuong(sach.getSoLuong() + chiTiet.getSoLuong());
                sachRepository.save(sach);
            }

            phieuMuonRepository.save(phieuMuon);

            redirect.addFlashAttribute("success", String.format(
                    "Thanh toán thành công! Mã giao dịch: %s - Số tiền: %,.0f VNĐ", 
                    thanhToan.getMaGiaoDich(), soTien));
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi thanh toán: " + e.getMessage());
        }

        return "redirect:/phieumuon";
    }

    // Lịch sử thanh toán
    @GetMapping("/thanh-toan/lich-su")
    public String lichSuThanhToan(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(username).orElse(null);

        if (taiKhoan != null) {
            model.addAttribute("thanhToanList", thanhToanRepository.findByTaiKhoanId(taiKhoan.getId()));
        }

        return "phieumuon/lich-su-thanh-toan";
    }
}