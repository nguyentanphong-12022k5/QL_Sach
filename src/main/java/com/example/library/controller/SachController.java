package com.example.library.controller;

import com.example.library.dto.SachDTO;
import com.example.library.service.SachService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;        // ← THÊM DÒNG NÀY
import java.util.ArrayList;

@Controller
public class SachController {

    private final SachService sachService;

    public SachController(SachService sachService) {
        this.sachService = sachService;
    }

    @GetMapping("/sach")
    public String getSachPage(@RequestParam(value = "keyword", required = false) String keyword,
                              @RequestParam(value = "filter", required = false) String filter,
                              Model model) {
        try {
            List<SachDTO> sachList;
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                sachList = sachService.search(keyword);
                model.addAttribute("keyword", keyword);
                model.addAttribute("resultCount", sachList.size());
            } else if ("available".equals(filter)) {
                sachList = sachService.findAvailable();
                model.addAttribute("filter", filter);
            } else {
                sachList = sachService.findAll();
            }
            
            model.addAttribute("sachList", sachList);
        } catch (Exception e) {
            model.addAttribute("sachList", new ArrayList<>());
            model.addAttribute("error", "Không thể tải danh sách sách: " + e.getMessage());
        }
        return "sach";
    }

    @GetMapping("/sach/{id}")
    public String getSachDetail(@PathVariable Long id, Model model) {
        try {
            SachDTO sach = sachService.findById(id);
            model.addAttribute("sach", sach);
            return "sach-detail";
        } catch (Exception e) {
            return "redirect:/sach";
        }
    }
}