package com.example.library.controller;

import com.example.library.entity.TaiKhoan;
import com.example.library.repository.TaiKhoanRepository;
import com.example.library.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @GetMapping
    public String list(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        TaiKhoan tk = taiKhoanRepository.findByUsername(username).orElseThrow();
        
        model.addAttribute("notifications", notificationService.getAll(tk.getId()));
        return "notifications/list";
    }

    @GetMapping("/read/{id}")
    public String markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return "redirect:/notifications";
    }

    @GetMapping("/read-all")
    public String markAllAsRead(RedirectAttributes redirect) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        TaiKhoan tk = taiKhoanRepository.findByUsername(username).orElseThrow();
        
        notificationService.markAllAsRead(tk.getId());
        redirect.addFlashAttribute("success", "Đã đánh dấu tất cả là đã đọc.");
        return "redirect:/notifications";
    }
}
