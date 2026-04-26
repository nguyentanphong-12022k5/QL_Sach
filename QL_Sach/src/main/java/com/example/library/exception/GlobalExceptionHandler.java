package com.example.library.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrityViolation(DataIntegrityViolationException ex, 
                                               HttpServletRequest request, 
                                               RedirectAttributes redirectAttributes) {
        logger.error("Dữ liệu bị liên kết (Data Integrity Violation) tại URL: {}. Chi tiết: {}", 
                     request.getRequestURL(), ex.getMessage());
        
        redirectAttributes.addFlashAttribute("error", 
            "Không thể hoàn tất thao tác: Dữ liệu đang được liên kết (Ví dụ: Độc giả đang mượn sách, Sách này đã nằm trong một phiếu mượn/nhập, Tác giả đang thuộc về sách...). Vui lòng xóa các dữ liệu liên kết trước.");
        
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        logger.error("Lỗi không lường trước xảy ra tại URL: {}. Chi tiết: ", request.getRequestURL(), ex);
        
        redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi hệ thống: " + ex.getMessage());
        
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }
}
