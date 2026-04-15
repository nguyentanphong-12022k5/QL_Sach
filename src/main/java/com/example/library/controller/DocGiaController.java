package com.example.library.controller;

import com.example.library.entity.DocGia;
import com.example.library.repository.DocGiaRepository;
import com.example.library.repository.PhieuMuonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/docgia")
public class DocGiaController {

    @Autowired private DocGiaRepository docGiaRepository;
    @Autowired private PhieuMuonRepository phieuMuonRepository;

    @GetMapping
    public String list(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        java.util.List<DocGia> docGiaList;
        if (keyword != null && !keyword.trim().isEmpty()) {
            docGiaList = docGiaRepository.findByHoTenContainingIgnoreCaseOrSoDienThoaiContaining(keyword, keyword);
            model.addAttribute("keyword", keyword);
            model.addAttribute("resultCount", docGiaList.size());
        } else {
            docGiaList = docGiaRepository.findAll();
        }
        model.addAttribute("docGiaList", docGiaList);
        return "docgia/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("docGia", new DocGia());
        return "docgia/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute DocGia docGia, RedirectAttributes redirect) {
        if (docGia.getTrangThai() == null) docGia.setTrangThai(1);
        docGiaRepository.save(docGia);
        redirect.addFlashAttribute("success", "Lưu độc giả thành công!");
        return "redirect:/docgia";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("docGia", docGiaRepository.findById(id).orElseThrow());
        return "docgia/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        docGiaRepository.deleteById(id);
        redirect.addFlashAttribute("success", "Xóa độc giả thành công!");
        return "redirect:/docgia";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("docGia", docGiaRepository.findById(id).orElseThrow());
        model.addAttribute("phieuMuonList", phieuMuonRepository.findByDocGiaId(id));
        return "docgia/view";
    }
}