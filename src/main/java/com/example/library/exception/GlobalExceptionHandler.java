package com.example.library.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrityViolation(DataIntegrityViolationException ex, 
                                               HttpServletRequest request, 
                                               RedirectAttributes redirectAttributes) {
        
        // Tránh app bị crash 500 khi xóa dữ liệu có khóa ngoại
        redirectAttributes.addFlashAttribute("error", 
            "Không thể hoàn tất thao tác: Dữ liệu đang được liên kết (Ví dụ: Độc giả đang mượn sách, Sách này đã nằm trong một phiếu mượn/nhập, Tác giả đang thuộc về sách...). Vui lòng xóa các dữ liệu liên kết trước.");
        
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }
}
