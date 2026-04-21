package com.example.library.controller;

import com.example.library.entity.TaiKhoan;
import com.example.library.entity.DocGia;
import com.example.library.repository.TaiKhoanRepository;
import com.example.library.repository.DocGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.library.service.QRCodeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.google.zxing.WriterException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/profile")
public class TaiKhoanController {

	@Autowired
	private TaiKhoanRepository taiKhoanRepository;

	@Autowired
	private DocGiaRepository docGiaRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private QRCodeService qrCodeService;

	// Đường dẫn lưu ảnh
	private static final String UPLOAD_DIR = "src/main/resources/static/img/avatar/";

	@GetMapping
	public String profile(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();

		TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

		// Tìm thông tin Độc giả để lấy Cấp bậc
		java.util.Optional<DocGia> docGiaOpt = java.util.Optional.empty();
		
		// Ưu tiên khớp theo Số điện thoại
		if (taiKhoan.getSoDienThoai() != null && !taiKhoan.getSoDienThoai().isEmpty()) {
			docGiaOpt = docGiaRepository.findBySoDienThoai(taiKhoan.getSoDienThoai());
		}
		
		// Fallback: Khớp theo Họ tên (nếu chưa tìm thấy)
		if (docGiaOpt.isEmpty() && taiKhoan.getHoTen() != null && !taiKhoan.getHoTen().isEmpty()) {
			List<DocGia> listByHoTen = docGiaRepository.findByHoTenContainingIgnoreCaseOrSoDienThoaiContaining(taiKhoan.getHoTen(), "---NOT-FIND---");
			if (!listByHoTen.isEmpty()) {
				// Lấy người đầu tiên trùng tên hoàn toàn (hoặc gần đúng nhất)
				docGiaOpt = listByHoTen.stream()
						.filter(d -> d.getHoTen().equalsIgnoreCase(taiKhoan.getHoTen()))
						.findFirst();
			}
		}

		docGiaOpt.ifPresent(dg -> model.addAttribute("docGia", dg));

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
			String extension = ".jpg"; // Mặc định
			if (originalFilename != null && originalFilename.contains(".")) {
				extension = originalFilename.substring(originalFilename.lastIndexOf("."));
			}
			String fileName = UUID.randomUUID().toString() + extension;
			System.out.println(">>> Bắt đầu upload avatar: " + fileName + " (" + file.getSize() + " bytes)");

			// === QUAN TRỌNG: Lưu vào cả 2 thư mục ===
			String userDir = System.getProperty("user.dir");
			
			// 1. Lưu vào src
			String srcPath = userDir + "/src/main/resources/static/img/avatar/" + fileName;
			File srcFile = new File(srcPath);
			srcFile.getParentFile().mkdirs();
			file.transferTo(srcFile);
			System.out.println("✅ Đã lưu vào src: " + srcPath);

			// 2. Lưu vào target (để hiển thị ngay)
			String targetPath = userDir + "/target/classes/static/img/avatar/" + fileName;
			File targetFile = new File(targetPath);
			targetFile.getParentFile().mkdirs();
			Files.copy(srcFile.toPath(), targetFile.toPath());
			System.out.println("✅ Đã lưu vào target: " + targetPath);

			// Xóa ảnh cũ nếu có
			if (taiKhoan.getAvatar() != null && !taiKhoan.getAvatar().contains("ui-avatars.com")) {
				try {
					String oldAvatarName = taiKhoan.getAvatar().replace("/img/avatar/", "");
					Files.deleteIfExists(Paths.get(userDir + "/src/main/resources/static/img/avatar/" + oldAvatarName));
					Files.deleteIfExists(Paths.get(userDir + "/target/classes/static/img/avatar/" + oldAvatarName));
					System.out.println("🗑️ Đã xóa ảnh cũ: " + oldAvatarName);
				} catch (IOException e) {
					System.err.println("⚠️ Lỗi xóa ảnh cũ: " + e.getMessage());
				}
			}

			// Cập nhật avatar trong database
			taiKhoan.setAvatar("/img/avatar/" + fileName);
			taiKhoanRepository.save(taiKhoan);

			redirect.addFlashAttribute("success", "Cập nhật ảnh đại diện thành công!");
		} catch (IOException e) {
			System.err.println("❌ Lỗi upload ảnh: " + e.getMessage());
			e.printStackTrace();
			redirect.addFlashAttribute("error", "Lỗi upload ảnh: " + e.getMessage());
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

	@GetMapping("/qr-code")
	public ResponseEntity<byte[]> getQRCode() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();

		TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

		try {
			// Nội dung mã QR mặc định là ID tài khoản
			Long idToEncode = taiKhoan.getId();
			
			// Tìm thông tin Độc giả để lấy ID thực tế của thẻ thư viện
			java.util.Optional<DocGia> docGiaOpt = java.util.Optional.empty();
			if (taiKhoan.getSoDienThoai() != null && !taiKhoan.getSoDienThoai().isEmpty()) {
				docGiaOpt = docGiaRepository.findBySoDienThoai(taiKhoan.getSoDienThoai());
			}
			
			if (docGiaOpt.isEmpty() && taiKhoan.getHoTen() != null && !taiKhoan.getHoTen().isEmpty()) {
				List<DocGia> listByHoTen = docGiaRepository.findByHoTenContainingIgnoreCaseOrSoDienThoaiContaining(taiKhoan.getHoTen(), "---NOT-FIND---");
				docGiaOpt = listByHoTen.stream()
						.filter(d -> d.getHoTen().equalsIgnoreCase(taiKhoan.getHoTen()))
						.findFirst();
			}

			// Nếu tìm thấy Độc giả, sử dụng ID độc giả (đây mới là ID đúng để quét mượn sách)
			if (docGiaOpt.isPresent()) {
				idToEncode = docGiaOpt.get().getId();
			}

			String qrContent = "READER:" + idToEncode;
			byte[] qrImage = qrCodeService.generateQRCodeImage(qrContent, 300, 300);

			return ResponseEntity.ok()
					.contentType(MediaType.IMAGE_PNG)
					.body(qrImage);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
}