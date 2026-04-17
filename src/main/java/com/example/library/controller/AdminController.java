package com.example.library.controller;

import com.example.library.entity.*;
import com.example.library.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private SachRepository sachRepository;
	@Autowired
	private DocGiaRepository docGiaRepository;
	@Autowired
	private TacGiaRepository tacGiaRepository;
	@Autowired
	private NhaXuatBanRepository nhaXuatBanRepository;
	@Autowired
	private LoaiSachRepository loaiSachRepository;
	@Autowired
	private KeSachRepository keSachRepository;
	@Autowired
	private TaiKhoanRepository taiKhoanRepository;
	@Autowired
	private ThanhToanRepository thanhToanRepository; // ← THÊM DÒNG NÀY

	@org.springframework.beans.factory.annotation.Value("${app.upload.dir}")
	private String uploadDir;

	// ===== DASHBOARD =====
	@GetMapping("/dashboard")
	public String dashboard(Model model) {
	    model.addAttribute("totalSach", sachRepository.count());
	    model.addAttribute("totalDocGia", docGiaRepository.count());
	    model.addAttribute("totalTaiKhoan", taiKhoanRepository.count());
	    model.addAttribute("totalTacGia", tacGiaRepository.count());
	    model.addAttribute("totalNXB", nhaXuatBanRepository.count());
	    model.addAttribute("totalLoaiSach", loaiSachRepository.count());  // ← THÊM
	    model.addAttribute("totalKeSach", keSachRepository.count());      // ← THÊM

	    // Thống kê thanh toán
	    Double tongDoanhThu = thanhToanRepository.getTongDoanhThu();
	    Double doanhThuHomNay = thanhToanRepository.getDoanhThuHomNay();
	    model.addAttribute("tongDoanhThu", tongDoanhThu != null ? tongDoanhThu : 0);
	    model.addAttribute("doanhThuHomNay", doanhThuHomNay != null ? doanhThuHomNay : 0);

	    return "admin/dashboard";
	}

	@GetMapping("/thanh-toan")
	public String listThanhToan(Model model) {
		model.addAttribute("thanhToanList", thanhToanRepository.findAll());
		model.addAttribute("tongDoanhThu", thanhToanRepository.getTongDoanhThu());
		model.addAttribute("doanhThuHomNay", thanhToanRepository.getDoanhThuHomNay());
		return "admin/thanh-toan-list";
	}

	// ===== QUẢN LÝ SÁCH =====
	@GetMapping("/sach")
	public String listSach(Model model) {
		model.addAttribute("sachList", sachRepository.findAll());
		model.addAttribute("tacGiaList", tacGiaRepository.findAll());
		model.addAttribute("nxbList", nhaXuatBanRepository.findAll());
		model.addAttribute("loaiSachList", loaiSachRepository.findAll());
		model.addAttribute("keSachList", keSachRepository.findAll());
		return "admin/sach-list";
	}

	@GetMapping("/sach/add")
	public String addSachForm(Model model) {
		model.addAttribute("sach", new Sach());
		model.addAttribute("tacGiaList", tacGiaRepository.findAll());
		model.addAttribute("nxbList", nhaXuatBanRepository.findAll());
		model.addAttribute("loaiSachList", loaiSachRepository.findAll());
		model.addAttribute("keSachList", keSachRepository.findAll());
		return "admin/sach-form";
	}

	@PostMapping("/sach/save")
	public String saveSach(@ModelAttribute Sach sach,
			@RequestParam(value = "imageFile", required = false) MultipartFile imageFile, RedirectAttributes redirect) {
		try {
			if (imageFile != null && !imageFile.isEmpty()) {
				String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
				
				// Thư mục lưu ảnh cấp 1: Trong project root (Cho ResourceHandler)
				File uploadFolder = new File(uploadDir);
				if (!uploadFolder.exists()) {
					uploadFolder.mkdirs();
				}

				Path path = Paths.get(uploadFolder.getAbsolutePath() + File.separator + fileName);
				Files.write(path, imageFile.getBytes());
				System.out.println("✅ Image uploaded to: " + path);

				// Thư mục lưu ảnh cấp 2: Trong src (Để lưu lại source code - Optional)
				try {
					String srcDir = System.getProperty("user.dir") + "/src/main/resources/static/img/sach/";
					File srcFolder = new File(srcDir);
					if (srcFolder.exists()) {
						Path srcPath = Paths.get(srcFolder.getAbsolutePath() + File.separator + fileName);
						Files.write(srcPath, imageFile.getBytes());
						System.out.println("✅ Image copied to src: " + srcPath);
					}
				} catch (Exception e) {
					System.out.println("⚠️ Could not copy to src (not critical): " + e.getMessage());
				}

				sach.setImageUrl(fileName);
			} else {
				if (sach.getId() != null) {
					Sach existing = sachRepository.findById(sach.getId()).orElse(null);
					if (existing != null && existing.getImageUrl() != null) {
						sach.setImageUrl(existing.getImageUrl());
					}
				}
			}

			if (sach.getSoLuong() == null)
				sach.setSoLuong(10);
			if (sach.getTrangThai() == null)
				sach.setTrangThai("1");

			sachRepository.save(sach);
			redirect.addFlashAttribute("success", "Lưu sách thành công!");

		} catch (IOException e) {
			e.printStackTrace();
			redirect.addFlashAttribute("error", "Lỗi upload ảnh: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			redirect.addFlashAttribute("error", "Lỗi: " + e.getMessage());
		}
		return "redirect:/admin/sach";
	}

	@GetMapping("/sach/edit/{id}")
	public String editSach(@PathVariable Long id, Model model) {
		Sach sach = sachRepository.findById(id).orElseThrow();
		model.addAttribute("sach", sach);
		model.addAttribute("tacGiaList", tacGiaRepository.findAll());
		model.addAttribute("nxbList", nhaXuatBanRepository.findAll());
		model.addAttribute("loaiSachList", loaiSachRepository.findAll());
		model.addAttribute("keSachList", keSachRepository.findAll());
		return "admin/sach-form";
	}

	@GetMapping("/sach/delete/{id}")
	public String deleteSach(@PathVariable Long id, RedirectAttributes redirect) {
		sachRepository.deleteById(id);
		redirect.addFlashAttribute("success", "Xóa sách thành công!");
		return "redirect:/admin/sach";
	}

	// ===== QUẢN LÝ TÁC GIẢ =====
	@GetMapping("/tacgia")
	public String listTacGia(Model model) {
		model.addAttribute("tacGiaList", tacGiaRepository.findAll());
		return "admin/tacgia-list";
	}

	@GetMapping("/tacgia/add")
	public String addTacGiaForm(Model model) {
		model.addAttribute("tacGia", new TacGia());
		return "admin/tacgia-form";
	}

	@PostMapping("/tacgia/save")
	public String saveTacGia(@ModelAttribute TacGia tacGia, RedirectAttributes redirect) {
		tacGia.setTrangThai(1);
		tacGiaRepository.save(tacGia);
		redirect.addFlashAttribute("success", "Lưu tác giả thành công!");
		return "redirect:/admin/tacgia";
	}

	@GetMapping("/tacgia/edit/{id}")
	public String editTacGia(@PathVariable Long id, Model model) {
		model.addAttribute("tacGia", tacGiaRepository.findById(id).orElseThrow());
		return "admin/tacgia-form";
	}

	@GetMapping("/tacgia/delete/{id}")
	public String deleteTacGia(@PathVariable Long id, RedirectAttributes redirect) {
		tacGiaRepository.deleteById(id);
		redirect.addFlashAttribute("success", "Xóa tác giả thành công!");
		return "redirect:/admin/tacgia";
	}

	// ===== QUẢN LÝ NHÀ XUẤT BẢN =====
	@GetMapping("/nhaxuatban")
	public String listNhaXuatBan(Model model) {
		model.addAttribute("nxbList", nhaXuatBanRepository.findAll());
		return "admin/nhaxuatban-list";
	}

	@GetMapping("/nhaxuatban/add")
	public String addNhaXuatBanForm(Model model) {
		model.addAttribute("nxb", new NhaXuatBan());
		return "admin/nhaxuatban-form";
	}

	@PostMapping("/nhaxuatban/save")
	public String saveNhaXuatBan(@ModelAttribute NhaXuatBan nxb, RedirectAttributes redirect) {
		nxb.setTrangThai(1);
		nhaXuatBanRepository.save(nxb);
		redirect.addFlashAttribute("success", "Lưu NXB thành công!");
		return "redirect:/admin/nhaxuatban";
	}

	@GetMapping("/nhaxuatban/edit/{id}")
	public String editNhaXuatBan(@PathVariable Long id, Model model) {
		model.addAttribute("nxb", nhaXuatBanRepository.findById(id).orElseThrow());
		return "admin/nhaxuatban-form";
	}

	@GetMapping("/nhaxuatban/delete/{id}")
	public String deleteNhaXuatBan(@PathVariable Long id, RedirectAttributes redirect) {
		nhaXuatBanRepository.deleteById(id);
		redirect.addFlashAttribute("success", "Xóa NXB thành công!");
		return "redirect:/admin/nhaxuatban";
	}

	// ===== QUẢN LÝ LOẠI SÁCH =====
	@GetMapping("/loaisach")
	public String listLoaiSach(Model model) {
		model.addAttribute("loaiSachList", loaiSachRepository.findAll());
		return "admin/loaisach-list";
	}

	@GetMapping("/loaisach/add")
	public String addLoaiSachForm(Model model) {
		model.addAttribute("loaiSach", new LoaiSach());
		return "admin/loaisach-form";
	}

	@PostMapping("/loaisach/save")
	public String saveLoaiSach(@ModelAttribute LoaiSach loaiSach, RedirectAttributes redirect) {
		loaiSach.setTrangThai(1);
		loaiSachRepository.save(loaiSach);
		redirect.addFlashAttribute("success", "Lưu loại sách thành công!");
		return "redirect:/admin/loaisach";
	}

	@GetMapping("/loaisach/edit/{id}")
	public String editLoaiSach(@PathVariable Long id, Model model) {
		model.addAttribute("loaiSach", loaiSachRepository.findById(id).orElseThrow());
		return "admin/loaisach-form";
	}

	@GetMapping("/loaisach/delete/{id}")
	public String deleteLoaiSach(@PathVariable Long id, RedirectAttributes redirect) {
		loaiSachRepository.deleteById(id);
		redirect.addFlashAttribute("success", "Xóa loại sách thành công!");
		return "redirect:/admin/loaisach";
	}

	// ===== QUẢN LÝ KỆ SÁCH =====
	@GetMapping("/kesach")
	public String listKeSach(Model model) {
		model.addAttribute("keSachList", keSachRepository.findAll());
		return "admin/kesach-list";
	}

	@GetMapping("/kesach/add")
	public String addKeSachForm(Model model) {
		model.addAttribute("keSach", new KeSach());
		return "admin/kesach-form";
	}

	@PostMapping("/kesach/save")
	public String saveKeSach(@ModelAttribute KeSach keSach, RedirectAttributes redirect) {
		keSach.setTrangThai(1);
		keSachRepository.save(keSach);
		redirect.addFlashAttribute("success", "Lưu kệ sách thành công!");
		return "redirect:/admin/kesach";
	}

	@GetMapping("/kesach/edit/{id}")
	public String editKeSach(@PathVariable Long id, Model model) {
		model.addAttribute("keSach", keSachRepository.findById(id).orElseThrow());
		return "admin/kesach-form";
	}

	@GetMapping("/kesach/delete/{id}")
	public String deleteKeSach(@PathVariable Long id, RedirectAttributes redirect) {
		keSachRepository.deleteById(id);
		redirect.addFlashAttribute("success", "Xóa kệ sách thành công!");
		return "redirect:/admin/kesach";
	}

	// ===== QUẢN LÝ TÀI KHOẢN =====
	@GetMapping("/taikhoan")
	public String listTaiKhoan(Model model) {
		model.addAttribute("taiKhoanList", taiKhoanRepository.findAll());
		return "admin/taikhoan-list";
	}

	@GetMapping("/taikhoan/add")
	public String addTaiKhoanForm(Model model) {
		model.addAttribute("taiKhoan", new TaiKhoan());
		return "admin/taikhoan-form";
	}

	@PostMapping("/taikhoan/save")
	public String saveTaiKhoan(@ModelAttribute TaiKhoan taiKhoan, RedirectAttributes redirect) {
	    // Nếu là sửa tài khoản và không nhập mật khẩu mới
	    if (taiKhoan.getId() != null && (taiKhoan.getPassword() == null || taiKhoan.getPassword().isEmpty())) {
	        TaiKhoan existing = taiKhoanRepository.findById(taiKhoan.getId()).orElse(null);
	        if (existing != null) {
	            taiKhoan.setPassword(existing.getPassword());
	        }
	    }
	    
	    // Nếu là thêm mới hoặc có nhập mật khẩu mới - KHÔNG MÃ HÓA
	    // (Phù hợp với NoOpPasswordEncoder)
	    
	    taiKhoanRepository.save(taiKhoan);
	    redirect.addFlashAttribute("success", "Lưu tài khoản thành công!");
	    return "redirect:/admin/taikhoan";
	}
	@GetMapping("/taikhoan/edit/{id}")
	public String editTaiKhoan(@PathVariable Long id, Model model) {
	    TaiKhoan taiKhoan = taiKhoanRepository.findById(id).orElseThrow();
	    taiKhoan.setPassword(""); // Không hiển thị mật khẩu cũ
	    model.addAttribute("taiKhoan", taiKhoan);
	    return "admin/taikhoan-form";
	}

	@GetMapping("/taikhoan/delete/{id}")
	public String deleteTaiKhoan(@PathVariable Long id, RedirectAttributes redirect) {
		taiKhoanRepository.deleteById(id);
		redirect.addFlashAttribute("success", "Xóa tài khoản thành công!");
		return "redirect:/admin/taikhoan";
	}
}