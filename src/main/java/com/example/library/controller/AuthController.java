package com.example.library.controller;

import com.example.library.entity.TaiKhoan;
import com.example.library.repository.TaiKhoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
public class AuthController {

	@Autowired
	private TaiKhoanRepository taiKhoanRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/login")
	public String loginPage(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout,
			@RequestParam(value = "registered", required = false) String registered,
			@RequestParam(value = "reset", required = false) String reset, Model model) {
		if (error != null) {
			model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
		}
		if (logout != null) {
			model.addAttribute("message", "Bạn đã đăng xuất thành công!");
		}
		if (registered != null) {
			model.addAttribute("message", "Đăng ký thành công! Vui lòng đăng nhập.");
		}
		if (reset != null) {
			model.addAttribute("message", "Mật khẩu đã được đặt lại thành công! Vui lòng đăng nhập.");
		}
		return "auth/login";
	}

	@GetMapping("/register")
	public String registerPage(Model model) {
		model.addAttribute("taiKhoan", new TaiKhoan());
		return "auth/register";
	}

	@PostMapping("/register")
	public String register(@ModelAttribute TaiKhoan taiKhoan,
	        @RequestParam("confirmPassword") String confirmPassword,
	        RedirectAttributes redirectAttributes) {
	    try {
	        if (!taiKhoan.getPassword().equals(confirmPassword)) {
	            redirectAttributes.addFlashAttribute("error", "Mật khẩu xác nhận không khớp!");
	            return "redirect:/register";
	        }

	        if (taiKhoanRepository.existsByUsername(taiKhoan.getUsername())) {
	            redirectAttributes.addFlashAttribute("error", "Tên đăng nhập đã tồn tại!");
	            return "redirect:/register";
	        }

	        // KHÔNG MÃ HÓA - Lưu thẳng mật khẩu
	        // taiKhoan.setPassword(passwordEncoder.encode(taiKhoan.getPassword()));
	        
	        taiKhoan.setQuyen(4); // Mặc định là Khách hàng
	        taiKhoan.setTrangThai(1);
	        taiKhoanRepository.save(taiKhoan);
	        
	        redirectAttributes.addFlashAttribute("message", "Đăng ký thành công!");
	        return "redirect:/login?registered=true";
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("error", "Đăng ký thất bại: " + e.getMessage());
	        return "redirect:/register";
	    }
	}

	// ===== QUÊN MẬT KHẨU =====
	@GetMapping("/forgot-password")
	public String forgotPasswordPage() {
		return "auth/forgot-password";
	}

	@PostMapping("/forgot-password")
	public String processForgotPassword(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
		try {
			TaiKhoan taiKhoan = taiKhoanRepository.findByEmail(email).orElse(null);

			if (taiKhoan == null) {
				redirectAttributes.addFlashAttribute("error", "Email không tồn tại trong hệ thống!");
				return "redirect:/forgot-password";
			}

			// Tạo token reset password (trong thực tế sẽ gửi qua email)
			String resetToken = UUID.randomUUID().toString();
			taiKhoan.setResetToken(resetToken);
			taiKhoanRepository.save(taiKhoan);

			// Giả lập gửi email - hiển thị link reset
			String resetLink = "http://localhost:8080/reset-password?token=" + resetToken;

			redirectAttributes.addFlashAttribute("message", "Link đặt lại mật khẩu đã được tạo!<br>"
					+ "<strong>Link (giả lập):</strong> <a href='" + resetLink + "'>" + resetLink + "</a>");
			return "redirect:/forgot-password";

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
			return "redirect:/forgot-password";
		}
	}

	@GetMapping("/reset-password")
	public String resetPasswordPage(@RequestParam("token") String token, Model model) {
		TaiKhoan taiKhoan = taiKhoanRepository.findByResetToken(token).orElse(null);

		if (taiKhoan == null) {
			model.addAttribute("error", "Link đặt lại mật khẩu không hợp lệ hoặc đã hết hạn!");
			return "auth/reset-password";
		}

		model.addAttribute("token", token);
		return "auth/reset-password";
	}

	@PostMapping("/reset-password")
	public String processResetPassword(@RequestParam("token") String token, @RequestParam("password") String password,
			@RequestParam("confirmPassword") String confirmPassword, RedirectAttributes redirectAttributes) {
		try {
			if (!password.equals(confirmPassword)) {
				redirectAttributes.addFlashAttribute("error", "Mật khẩu xác nhận không khớp!");
				return "redirect:/reset-password?token=" + token;
			}

			TaiKhoan taiKhoan = taiKhoanRepository.findByResetToken(token).orElse(null);

			if (taiKhoan == null) {
				redirectAttributes.addFlashAttribute("error", "Link đặt lại mật khẩu không hợp lệ!");
				return "redirect:/login";
			}

			// Cập nhật mật khẩu mới
			taiKhoan.setPassword(passwordEncoder.encode(password));
			taiKhoan.setResetToken(null); // Xóa token sau khi dùng
			taiKhoanRepository.save(taiKhoan);

			redirectAttributes.addFlashAttribute("message", "Đặt lại mật khẩu thành công!");
			return "redirect:/login?reset=true";

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
			return "redirect:/reset-password?token=" + token;
		}
	}

	@Controller
	public class ErrorController {

		@GetMapping("/access-denied")
		public String accessDenied() {
			return "access-denied";
		}
	}
}