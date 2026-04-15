package com.example.library.controller;

import com.example.library.entity.BinhLuan;
import com.example.library.entity.Sach;
import com.example.library.entity.TaiKhoan;
import com.example.library.repository.BinhLuanRepository;
import com.example.library.repository.SachRepository;
import com.example.library.repository.TaiKhoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/dien-dan")
public class DienDanController {

    @Autowired
    private BinhLuanRepository binhLuanRepository;
    
    @Autowired
    private TaiKhoanRepository taiKhoanRepository;
    
    @Autowired
    private SachRepository sachRepository;

    // Trang diễn đàn chính (góp ý chung)
    @GetMapping
    public String index(Model model) {
        // === THÊM currentUser VÀO MODEL ===
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            TaiKhoan currentUser = taiKhoanRepository.findByUsername(username).orElse(null);
            model.addAttribute("currentUser", currentUser);
        }
        
        model.addAttribute("binhLuanList", binhLuanRepository.findGopYChung());
        model.addAttribute("binhLuanMoi", new BinhLuan());
        model.addAttribute("binhLuanMoiNhat", binhLuanRepository.findTop10ByTrangThaiOrderByNgayDangDesc(1));
        return "dien-dan/index";
    }

    // Xem bình luận của một sách
    @GetMapping("/sach/{sachId}")
    public String binhLuanSach(@PathVariable Long sachId, Model model) {
        // === THÊM currentUser VÀO MODEL ===
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            TaiKhoan currentUser = taiKhoanRepository.findByUsername(username).orElse(null);
            model.addAttribute("currentUser", currentUser);
        }
        
        Sach sach = sachRepository.findById(sachId).orElse(null);
        if (sach == null) {
            return "redirect:/dien-dan";
        }
        
        model.addAttribute("sach", sach);
        model.addAttribute("binhLuanList", binhLuanRepository.findBySachIdAndTrangThaiOrderByNgayDangDesc(sachId, 1));
        model.addAttribute("binhLuanMoi", new BinhLuan());
        model.addAttribute("soBinhLuan", binhLuanRepository.countBySachIdAndTrangThai(sachId, 1));
        
        return "dien-dan/sach";
    }

    // Đăng bình luận mới
    @PostMapping("/dang")
    public String dangBinhLuan(@ModelAttribute BinhLuan binhLuan,
                               @RequestParam(value = "sachId", required = false) Long sachId,
                               RedirectAttributes redirect) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            
            TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));
            
            binhLuan.setTaiKhoan(taiKhoan);
            binhLuan.setNgayDang(LocalDateTime.now());
            binhLuan.setTrangThai(1);
            
            if (sachId != null) {
                Sach sach = sachRepository.findById(sachId).orElse(null);
                binhLuan.setSach(sach);
                binhLuan.setLoai("SACH");
            } else {
                binhLuan.setLoai("GOP_Y");
            }
            
            binhLuanRepository.save(binhLuan);
            redirect.addFlashAttribute("success", "Đăng bình luận thành công!");
            
            if (sachId != null) {
                return "redirect:/dien-dan/sach/" + sachId;
            }
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        
        return "redirect:/dien-dan";
    }

    // Xóa bình luận (chỉ admin hoặc chủ bình luận)
    @GetMapping("/xoa/{id}")
    public String xoaBinhLuan(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(username).orElse(null);
            
            BinhLuan binhLuan = binhLuanRepository.findById(id).orElse(null);
            if (binhLuan != null) {
                if (taiKhoan != null && (taiKhoan.getQuyen() == 1 || 
                    binhLuan.getTaiKhoan().getId().equals(taiKhoan.getId()))) {
                    binhLuanRepository.delete(binhLuan);
                    redirect.addFlashAttribute("success", "Xóa bình luận thành công!");
                } else {
                    redirect.addFlashAttribute("error", "Bạn không có quyền xóa bình luận này!");
                }
            }
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        
        return "redirect:/dien-dan";
    }

    // Admin: Quản lý bình luận
    @GetMapping("/admin")
    public String admin(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            TaiKhoan currentUser = taiKhoanRepository.findByUsername(username).orElse(null);
            model.addAttribute("currentUser", currentUser);
        }
        model.addAttribute("binhLuanList", binhLuanRepository.findAll());
        return "dien-dan/admin";
    }

    // Admin: Ẩn/Hiện bình luận
    @GetMapping("/admin/toggle/{id}")
    public String toggleTrangThai(@PathVariable Long id, RedirectAttributes redirect) {
        BinhLuan binhLuan = binhLuanRepository.findById(id).orElse(null);
        if (binhLuan != null) {
            binhLuan.setTrangThai(binhLuan.getTrangThai() == 1 ? 0 : 1);
            binhLuanRepository.save(binhLuan);
            redirect.addFlashAttribute("success", "Cập nhật trạng thái thành công!");
        }
        return "redirect:/dien-dan/admin";
    }

    // Form chỉnh sửa bình luận
    @GetMapping("/sua/{id}")
    public String suaBinhLuanForm(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(username).orElse(null);
        
        BinhLuan binhLuan = binhLuanRepository.findById(id).orElse(null);
        
        if (binhLuan == null) {
            redirect.addFlashAttribute("error", "Không tìm thấy bình luận!");
            return "redirect:/dien-dan";
        }
        
        if (taiKhoan == null || (!taiKhoan.getId().equals(binhLuan.getTaiKhoan().getId()) && taiKhoan.getQuyen() != 1)) {
            redirect.addFlashAttribute("error", "Bạn không có quyền chỉnh sửa bình luận này!");
            return "redirect:/dien-dan";
        }
        
        model.addAttribute("currentUser", taiKhoan);
        model.addAttribute("binhLuan", binhLuan);
        
        if (binhLuan.getSach() != null) {
            return "dien-dan/sua-sach";
        }
        return "dien-dan/sua";
    }
    
    // Cập nhật bình luận
    @PostMapping("/cap-nhat/{id}")
    public String capNhatBinhLuan(@PathVariable Long id,
                                  @RequestParam("noiDung") String noiDung,
                                  RedirectAttributes redirect) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(username).orElse(null);
            
            BinhLuan binhLuan = binhLuanRepository.findById(id).orElse(null);
            
            if (binhLuan == null) {
                redirect.addFlashAttribute("error", "Không tìm thấy bình luận!");
                return "redirect:/dien-dan";
            }
            
            if (taiKhoan == null || (!taiKhoan.getId().equals(binhLuan.getTaiKhoan().getId()) && taiKhoan.getQuyen() != 1)) {
                redirect.addFlashAttribute("error", "Bạn không có quyền chỉnh sửa bình luận này!");
                return "redirect:/dien-dan";
            }
            
            if (noiDung == null || noiDung.trim().isEmpty()) {
                redirect.addFlashAttribute("error", "Nội dung không được để trống!");
                return "redirect:/dien-dan/sua/" + id;
            }
            
            binhLuan.setNoiDung(noiDung.trim());
            binhLuan.setNgayDang(LocalDateTime.now());
            binhLuanRepository.save(binhLuan);
            
            redirect.addFlashAttribute("success", "Cập nhật bình luận thành công!");
            
            if (binhLuan.getSach() != null) {
                return "redirect:/dien-dan/sach/" + binhLuan.getSach().getId();
            }
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        
        return "redirect:/dien-dan";
    }

    // Thu hồi bình luận
    @GetMapping("/thu-hoi/{id}")
    public String thuHoiBinhLuan(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(username).orElse(null);
            
            BinhLuan binhLuan = binhLuanRepository.findById(id).orElse(null);
            
            if (binhLuan == null) {
                redirect.addFlashAttribute("error", "Không tìm thấy bình luận!");
                return "redirect:/dien-dan";
            }
            
            if (taiKhoan == null || (!taiKhoan.getId().equals(binhLuan.getTaiKhoan().getId()) && taiKhoan.getQuyen() != 1)) {
                redirect.addFlashAttribute("error", "Bạn không có quyền thu hồi bình luận này!");
                return "redirect:/dien-dan";
            }
            
            binhLuan.setTrangThai(2);
            binhLuanRepository.save(binhLuan);
            
            redirect.addFlashAttribute("success", "Đã thu hồi bình luận!");
            
            if (binhLuan.getSach() != null) {
                return "redirect:/dien-dan/sach/" + binhLuan.getSach().getId();
            }
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        
        return "redirect:/dien-dan";
    }

    // Khôi phục bình luận (Admin)
    @GetMapping("/khoi-phuc/{id}")
    public String khoiPhucBinhLuan(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(username).orElse(null);
            
            if (taiKhoan == null || taiKhoan.getQuyen() != 1) {
                redirect.addFlashAttribute("error", "Bạn không có quyền khôi phục bình luận!");
                return "redirect:/dien-dan";
            }
            
            BinhLuan binhLuan = binhLuanRepository.findById(id).orElse(null);
            if (binhLuan != null) {
                binhLuan.setTrangThai(1);
                binhLuanRepository.save(binhLuan);
                redirect.addFlashAttribute("success", "Đã khôi phục bình luận!");
            }
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        
        return "redirect:/dien-dan/admin";
    }

    // Xóa vĩnh viễn (Admin)
    @GetMapping("/xoa-vinh-vien/{id}")
    public String xoaVinhVien(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(username).orElse(null);
            
            if (taiKhoan == null || taiKhoan.getQuyen() != 1) {
                redirect.addFlashAttribute("error", "Bạn không có quyền xóa vĩnh viễn!");
                return "redirect:/dien-dan";
            }
            
            binhLuanRepository.deleteById(id);
            redirect.addFlashAttribute("success", "Đã xóa vĩnh viễn bình luận!");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        
        return "redirect:/dien-dan/admin";
    }

    // Xem bình luận đã thu hồi
    @GetMapping("/da-thu-hoi")
    public String binhLuanDaThuHoi(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            TaiKhoan currentUser = taiKhoanRepository.findByUsername(username).orElse(null);
            model.addAttribute("currentUser", currentUser);
            
            if (currentUser != null) {
                model.addAttribute("binhLuanList", 
                    binhLuanRepository.findByTaiKhoanIdAndTrangThaiOrderByNgayDangDesc(currentUser.getId(), 2));
            }
        }
        
        return "dien-dan/da-thu-hoi";
    }
}