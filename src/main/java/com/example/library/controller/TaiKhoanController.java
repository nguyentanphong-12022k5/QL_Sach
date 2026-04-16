package com.example.library.controller;

import com.example.library.entity.TaiKhoan;
import com.example.library.repository.TaiKhoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/profile")
public class TaiKhoanController {

	@Autowired
	private TaiKhoanRepository taiKhoanRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// Đường dẫn lưu ảnh
	private static final String UPLOAD_DIR = "src/main/resources/static/img/avatar/";

	@GetMapping
	public String profile(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();

		TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

		model.addAttribute("taiKhoan", taiKhoan);
		return "profile/index";
	}

	@GetMapping("/settings")
	public String settings(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();

		TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

		model.addAttribute("taiKhoan", taiKhoan);
		return "profile/settings";
	}

	@PostMapping("/update-info")
	public String updateInfo(@ModelAttribute TaiKhoan formData, RedirectAttributes redirect) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();

		TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

		taiKhoan.setHoTen(formData.getHoTen());
		taiKhoan.setEmail(formData.getEmail());
		taiKhoan.setSoDienThoai(formData.getSoDienThoai());
		taiKhoan.setDiaChi(formData.getDiaChi());

		taiKhoanRepository.save(taiKhoan);
		redirect.addFlashAttribute("success", "Cập nhật thông tin thành công!");
		return "redirect:/profile";
	}

	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword,
	                             @RequestParam("newPassword") String newPassword,
	                             @RequestParam("confirmPassword") String confirmPassword,
	                             RedirectAttributes redirect) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String username = auth.getName();
	    
	    TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(username)
	            .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));
	    
	    // So sánh trực tiếp - không dùng passwordEncoder.matches()
	    if (!oldPassword.equals(taiKhoan.getPassword())) {
	        redirect.addFlashAttribute("error", "Mật khẩu cũ không đúng!");
	        return "redirect:/profile/settings";
	    }
	    
	    if (!newPassword.equals(confirmPassword)) {
	        redirect.addFlashAttribute("error", "Mật khẩu xác nhận không khớp!");
	        return "redirect:/profile/settings";
	    }
	    
	    // Lưu thẳng - không mã hóa
	    taiKhoan.setPassword(newPassword);
	    taiKhoanRepository.save(taiKhoan);
	    
	    redirect.addFlashAttribute("success", "Đổi mật khẩu thành công!");
	    return "redirect:/profile";
	}
	@PostMapping("/upload-avatar")
	public String uploadAvatar(@RequestParam("avatar") MultipartFile file, RedirectAttributes redirect) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();

		TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

		if (file.isEmpty()) {
			redirect.addFlashAttribute("error", "Vui lòng chọn file ảnh!");
			return "redirect:/profile/settings";
		}

		try {
			// Tạo tên file unique
			String originalFilename = file.getOriginalFilename();
			String extension = "";
			if (originalFilename != null && originalFilename.contains(".")) {
				extension = originalFilename.substring(originalFilename.lastIndexOf("."));
			}
			String fileName = UUID.randomUUID().toString() + extension;

			// === QUAN TRỌNG: Lưu vào cả 2 thư mục ===
			// 1. Lưu vào src
			String srcDir = System.getProperty("user.dir") + "/src/main/resources/static/img/avatar/";
			File srcDirFile = new File(srcDir);
			if (!srcDirFile.exists())
				srcDirFile.mkdirs();
			Files.write(Paths.get(srcDir + fileName), file.getBytes());

			// 2. Lưu vào target (để hiển thị ngay)
			String targetDir = System.getProperty("user.dir") + "/target/classes/static/img/avatar/";
			File targetDirFile = new File(targetDir);
			if (!targetDirFile.exists())
				targetDirFile.mkdirs();
			Files.write(Paths.get(targetDir + fileName), file.getBytes());

			// Xóa ảnh cũ nếu có
			if (taiKhoan.getAvatar() != null && !taiKhoan.getAvatar().contains("ui-avatars.com")) {
				try {
					String oldAvatar = taiKhoan.getAvatar().replace("/img/avatar/", "");
					Files.deleteIfExists(Paths.get(srcDir + oldAvatar));
					Files.deleteIfExists(Paths.get(targetDir + oldAvatar));
				} catch (IOException e) {
					// Bỏ qua lỗi xóa file cũ
				}
			}

			// Cập nhật avatar trong database
			taiKhoan.setAvatar("/img/avatar/" + fileName);
			taiKhoanRepository.save(taiKhoan);

			redirect.addFlashAttribute("success", "Cập nhật ảnh đại diện thành công!");
		} catch (IOException e) {
			redirect.addFlashAttribute("error", "Lỗi upload ảnh: " + e.getMessage());
			e.printStackTrace();
		}

		return "redirect:/profile/settings";
	}

	@GetMapping("/remove-avatar")
	public String removeAvatar(RedirectAttributes redirect) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();

		TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

		// Xóa file ảnh cũ
		if (taiKhoan.getAvatar() != null && !taiKhoan.getAvatar().contains("ui-avatars.com")) {
			try {
				String oldAvatar = taiKhoan.getAvatar().replace("/img/avatar/", "");
				String srcDir = System.getProperty("user.dir") + "/src/main/resources/static/img/avatar/";
				String targetDir = System.getProperty("user.dir") + "/target/classes/static/img/avatar/";
				Files.deleteIfExists(Paths.get(srcDir + oldAvatar));
				Files.deleteIfExists(Paths.get(targetDir + oldAvatar));
			} catch (IOException e) {
				// Bỏ qua lỗi
			}
		}

		// Đặt avatar về null
		taiKhoan.setAvatar(null);
		taiKhoanRepository.save(taiKhoan);

		redirect.addFlashAttribute("success", "Đã xóa ảnh đại diện!");
		return "redirect:/profile/settings";
	}
}