package com.example.library.controller;

import com.example.library.entity.TaiKhoan;
import com.example.library.repository.TaiKhoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @GetMapping("/")
    public String home(Model model, Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(username).orElse(null);
            model.addAttribute("currentUser", taiKhoan);
        }
        return "index";
    }
}