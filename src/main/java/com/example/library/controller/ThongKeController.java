package com.example.library.controller;

import com.example.library.service.ThongKeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThongKeController {

    private final ThongKeService thongKeService;

    public ThongKeController(ThongKeService thongKeService) {
        this.thongKeService = thongKeService;
    }

    @GetMapping("/thongke")
    public String index(Model model) {
        model.addAttribute("stats", thongKeService.getDetailedStats());
        return "thongke/index";
    }
}