package com.example.library.config;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.library.entity.ChiTietPhieuMuon;
import com.example.library.entity.ChiTietPhieuNhap;
import com.example.library.entity.DocGia;
import com.example.library.entity.KeSach;
import com.example.library.entity.LoaiSach;
import com.example.library.entity.NhaXuatBan;
import com.example.library.entity.PhieuMuon;
import com.example.library.entity.PhieuNhap;
import com.example.library.entity.Sach;
import com.example.library.entity.TacGia;
import com.example.library.entity.TaiKhoan;
import com.example.library.repository.ChiTietPhieuMuonRepository;
import com.example.library.repository.ChiTietPhieuNhapRepository;
import com.example.library.repository.DocGiaRepository;
import com.example.library.repository.KeSachRepository;
import com.example.library.repository.LoaiSachRepository;
import com.example.library.repository.NhaXuatBanRepository;
import com.example.library.repository.PhieuMuonRepository;
import com.example.library.repository.PhieuNhapRepository;
import com.example.library.repository.SachRepository;
import com.example.library.repository.TacGiaRepository;
import com.example.library.repository.TaiKhoanRepository;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(
            SachRepository sachRepository,
            DocGiaRepository docGiaRepository,
            PhieuMuonRepository phieuMuonRepository,
            PhieuNhapRepository phieuNhapRepository,
            TacGiaRepository tacGiaRepository,
            NhaXuatBanRepository nhaXuatBanRepository,
            LoaiSachRepository loaiSachRepository,
            KeSachRepository keSachRepository,
            ChiTietPhieuMuonRepository chiTietPhieuMuonRepository,
            ChiTietPhieuNhapRepository chiTietPhieuNhapRepository,
            TaiKhoanRepository taiKhoanRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            // ===== TẠO TÀI KHOẢN MẪU =====
            if (taiKhoanRepository.count() == 0) {
                System.out.println(">>> Đang tạo tài khoản mẫu...");
                
                // Tài khoản Admin (quyền = 1)
             // Tài khoản Admin
                TaiKhoan admin = new TaiKhoan();
                admin.setUsername("admin");
                admin.setPassword("admin123");  // KHÔNG MÃ HÓA
                admin.setHoTen("Quản trị viên");
                admin.setEmail("admin@library.com");
                admin.setQuyen(1);
                admin.setTrangThai(1);
                taiKhoanRepository.save(admin);
                
                // Tài khoản Thủ thư (quyền = 2)
                TaiKhoan thuthu = new TaiKhoan();
                thuthu.setUsername("thuthu");
                thuthu.setPassword(passwordEncoder.encode("thuthu123"));
                thuthu.setHoTen("Thủ thư");
                thuthu.setEmail("thuthu@library.com");
                thuthu.setQuyen(2);
                thuthu.setTrangThai(1);
                taiKhoanRepository.save(thuthu);
                
                // Tài khoản Nhân viên (quyền = 3)
                TaiKhoan nhanvien = new TaiKhoan();
                nhanvien.setUsername("nhanvien");
                nhanvien.setPassword(passwordEncoder.encode("nhanvien123"));
                nhanvien.setHoTen("Nhân viên");
                nhanvien.setEmail("nhanvien@library.com");
                nhanvien.setQuyen(3);
                nhanvien.setTrangThai(1);
                taiKhoanRepository.save(nhanvien);
                
                System.out.println("✅ Đã tạo 3 tài khoản mẫu (admin/admin123, thuthu/thuthu123, nhanvien/nhanvien123)");
            } else {
                System.out.println("ℹ️ Tài khoản đã tồn tại, bỏ qua tạo tài khoản mẫu.");
            }

            // ===== TẠO DỮ LIỆU MẪU KHÁC =====
            if (sachRepository.count() == 0) {
                System.out.println(">>> Đang tạo dữ liệu mẫu...");
                
                // Tạo tác giả
                TacGia tacGia1 = new TacGia("Nguyễn Du", "Việt Nam");
                TacGia tacGia2 = new TacGia("William Shakespeare", "Anh");
                tacGiaRepository.save(tacGia1);
                tacGiaRepository.save(tacGia2);

                // Tạo nhà xuất bản
                NhaXuatBan nxb1 = new NhaXuatBan("NXB Giáo dục", "Hà Nội");
                NhaXuatBan nxb2 = new NhaXuatBan("NXB Văn học", "TP.HCM");
                nhaXuatBanRepository.save(nxb1);
                nhaXuatBanRepository.save(nxb2);

                // Tạo loại sách
                LoaiSach loai1 = new LoaiSach("Văn học");
                LoaiSach loai2 = new LoaiSach("Khoa học");
                loaiSachRepository.save(loai1);
                loaiSachRepository.save(loai2);

                // Tạo kệ sách
                KeSach ke1 = new KeSach("Kệ A1", 100);
                KeSach ke2 = new KeSach("Kệ B2", 80);
                keSachRepository.save(ke1);
                keSachRepository.save(ke2);

                // Tạo sách
                Sach sach1 = new Sach("Truyện Kiều", tacGia1, nxb1, loai1, ke1, "truyen-kieu.jpg");
                Sach sach2 = new Sach("Romeo và Juliet", tacGia2, nxb2, loai1, ke2, "romeo-juliet.jpg");
                sachRepository.save(sach1);
                sachRepository.save(sach2);

                // Tạo độc giả
                DocGia docGia1 = new DocGia("Nguyễn Văn A", "nguyenvana@example.com", "0123456789");
                DocGia docGia2 = new DocGia("Trần Thị B", "tranthib@example.com", "0987654321");
                docGiaRepository.save(docGia1);
                docGiaRepository.save(docGia2);

                // Tạo phiếu mượn
                PhieuMuon phieuMuon1 = new PhieuMuon(docGia1, LocalDate.now(), LocalDate.now().plusDays(7));
                phieuMuonRepository.save(phieuMuon1);

                // Tạo chi tiết phiếu mượn
                ChiTietPhieuMuon chiTiet1 = new ChiTietPhieuMuon(phieuMuon1, sach1, 1);
                chiTietPhieuMuonRepository.save(chiTiet1);

                // Tạo phiếu nhập
                PhieuNhap phieuNhap1 = new PhieuNhap(LocalDate.now(), "Công ty sách ABC", "Nhập sách mới");
                phieuNhapRepository.save(phieuNhap1);

                // Tạo chi tiết phiếu nhập
                ChiTietPhieuNhap chiTietNhap1 = new ChiTietPhieuNhap(phieuNhap1, sach1, 10, 50000.0);
                chiTietPhieuNhapRepository.save(chiTietNhap1);

                System.out.println("✅ Dữ liệu mẫu đã được tạo thành công!");
            } else {
                System.out.println("ℹ️ Dữ liệu sách đã tồn tại, bỏ qua tạo dữ liệu mẫu.");
            }
        };
    }
}