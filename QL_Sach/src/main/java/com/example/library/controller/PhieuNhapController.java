package com.example.library.controller;

import com.example.library.entity.*;
import com.example.library.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/phieunhap")
public class PhieuNhapController {

    @Autowired private PhieuNhapRepository phieuNhapRepository;
    @Autowired private SachRepository sachRepository;
    @Autowired private ChiTietPhieuNhapRepository chiTietPhieuNhapRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("phieuNhapList", phieuNhapRepository.findAll());
        return "phieunhap/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("phieuNhap", new PhieuNhap());
        model.addAttribute("sachList", sachRepository.findAll());
        return "phieunhap/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute PhieuNhap phieuNhap,
                       @RequestParam("sachIds") List<Long> sachIds,
                       @RequestParam("soLuongs") List<Integer> soLuongs,
                       @RequestParam("donGias") List<Double> donGias,
                       RedirectAttributes redirect) {
        try {
            phieuNhap.setNgayNhap(LocalDate.now());
            PhieuNhap saved = phieuNhapRepository.save(phieuNhap);
            
            List<ChiTietPhieuNhap> chiTiets = new ArrayList<>();
            double tongTien = 0;
            
            for (int i = 0; i < sachIds.size(); i++) {
                Sach sach = sachRepository.findById(sachIds.get(i)).orElseThrow();
                int soLuong = soLuongs.get(i);
                double donGia = donGias.get(i);
                
                sach.setSoLuong(sach.getSoLuong() + soLuong);
                sachRepository.save(sach);
                
                ChiTietPhieuNhap chiTiet = new ChiTietPhieuNhap();
                chiTiet.setPhieuNhap(saved);
                chiTiet.setSach(sach);
                chiTiet.setSoLuong(soLuong);
                chiTiet.setDonGia(donGia);
                chiTiets.add(chiTiet);
                
                tongTien += soLuong * donGia;
            }
            chiTietPhieuNhapRepository.saveAll(chiTiets);
            
            redirect.addFlashAttribute("success", 
                String.format("Tạo phiếu nhập thành công! Tổng tiền: %,.0f VNĐ", tongTien));
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/phieunhap";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        PhieuNhap phieuNhap = phieuNhapRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu nhập"));
        List<ChiTietPhieuNhap> chiTiets = chiTietPhieuNhapRepository.findByPhieuNhapId(id);
        double tongTien = chiTiets.stream().mapToDouble(ct -> ct.getSoLuong() * ct.getDonGia()).sum();
        
        model.addAttribute("phieuNhap", phieuNhap);
        model.addAttribute("chiTiets", chiTiets);
        model.addAttribute("tongTien", tongTien);
        return "phieunhap/view";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        phieuNhapRepository.deleteById(id);
        redirect.addFlashAttribute("success", "Xóa phiếu nhập thành công!");
        return "redirect:/phieunhap";
    }
}