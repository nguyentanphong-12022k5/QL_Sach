package com.example.library.controller;

import com.example.library.dto.SachDTO;
import com.example.library.service.SachService;
import com.example.library.repository.LoaiSachRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;

@Controller
public class SachController {

    private final SachService sachService;
    private final LoaiSachRepository loaiSachRepository;

    public SachController(SachService sachService, LoaiSachRepository loaiSachRepository) {
        this.sachService = sachService;
        this.loaiSachRepository = loaiSachRepository;
    }

    @GetMapping({"/sach", "/products"})
    public String getSachPage(@RequestParam(value = "keyword", required = false) String keyword,
                              @RequestParam(value = "loaiSachId", required = false) Long loaiSachId,
                              @RequestParam(value = "filter", required = false) String filter,
                              Model model) {
        try {
            model.addAttribute("loaiSachList", loaiSachRepository.findAll());
            List<SachDTO> sachList = sachService.findWithFilters(keyword, loaiSachId, filter);
            
            // Xử lý đếm kết quả Text Search
            if (keyword != null && !keyword.trim().isEmpty()) {
                model.addAttribute("keyword", keyword);
                model.addAttribute("resultCount", sachList.size());
            }

            model.addAttribute("loaiSachId", loaiSachId);
            model.addAttribute("filter", filter);
            model.addAttribute("sachList", sachList);
        } catch (Exception e) {
            model.addAttribute("loaiSachList", loaiSachRepository.findAll());
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