package com.example.library.controller;

import com.example.library.entity.TaiKhoan;
import com.example.library.repository.TaiKhoanRepository;
import com.example.library.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @ModelAttribute("unreadNotificationCount")
    public long getUnreadCount() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
                String username = auth.getName();
                return taiKhoanRepository.findByUsername(username)
                        .map(tk -> notificationService.getUnreadCount(tk.getId()))
                        .orElse(0L);
            }
        } catch (Exception e) {
            // Silently fail to avoid redirect loops on database errors
            return 0;
        }
        return 0;
    }
}
