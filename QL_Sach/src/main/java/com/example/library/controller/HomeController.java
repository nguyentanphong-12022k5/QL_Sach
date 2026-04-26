package com.example.library.controller;

import com.example.library.entity.TaiKhoan;
import com.example.library.repository.TaiKhoanRepository;
import com.example.library.repository.SachRepository;
import com.example.library.repository.DocGiaRepository;
import com.example.library.repository.PhieuMuonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDate;

@Controller
public class HomeController {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;
    @Autowired
    private SachRepository sachRepository;
    @Autowired
    private DocGiaRepository docGiaRepository;
    @Autowired
    private PhieuMuonRepository phieuMuonRepository;

    @GetMapping("/")
    public String home(Model model, Authentication auth) {
        addAuthAttributes(model, auth);
        // Thống kê thực từ DB
        model.addAttribute("totalSach", sachRepository.count());
        model.addAttribute("totalDocGia", docGiaRepository.count());
        model.addAttribute("dangMuon", phieuMuonRepository.countByNgayTraIsNull());
        model.addAttribute("quaHan", phieuMuonRepository.countQuaHan(LocalDate.now().minusDays(7)));
        return "index";
    }

    @GetMapping("/about")
    public String about(Model model, Authentication auth) {
        addAuthAttributes(model, auth);
        return "about";
    }

    private void addAuthAttributes(Model model, Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(username).orElse(null);
            model.addAttribute("currentUser", taiKhoan);
        }
    }
}