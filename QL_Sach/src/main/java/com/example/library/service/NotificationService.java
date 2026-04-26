package com.example.library.service;

import com.example.library.entity.DocGia;
import com.example.library.entity.TaiKhoan;
import com.example.library.entity.ThongBao;
import com.example.library.repository.DocGiaRepository;
import com.example.library.repository.TaiKhoanRepository;
import com.example.library.repository.ThongBaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private ThongBaoRepository thongBaoRepository;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private DocGiaRepository docGiaRepository;

    /**
     * Gửi thông báo trực tiếp cho tài khoản
     */
    public void notify(Long taiKhoanId, String tieuDe, String noiDung, String loai) {
        ThongBao tb = new ThongBao(taiKhoanId, tieuDe, noiDung, loai);
        thongBaoRepository.save(tb);
    }

    /**
     * Gửi thông báo cho Độc giả (Tự động tìm tài khoản liên kết)
     */
    public void notifyReader(DocGia docGia, String tieuDe, String noiDung, String loai) {
        if (docGia == null) return;

        Optional<TaiKhoan> targetAccount = Optional.empty();

        // 1. Tìm theo số điện thoại
        if (docGia.getSoDienThoai() != null && !docGia.getSoDienThoai().isEmpty()) {
            targetAccount = taiKhoanRepository.findBySoDienThoai(docGia.getSoDienThoai());
        }

        // 2. Fallback: Tìm theo họ tên
        if (targetAccount.isEmpty()) {
            List<TaiKhoan> accounts = taiKhoanRepository.findAll(); // Dataset nhỏ, filter in memory cho nhanh
            targetAccount = accounts.stream()
                    .filter(tk -> tk.getHoTen() != null && tk.getHoTen().equalsIgnoreCase(docGia.getHoTen()))
                    .findFirst();
        }

        targetAccount.ifPresent(tk -> notify(tk.getId(), tieuDe, noiDung, loai));
    }

    public List<ThongBao> getUnread(Long taiKhoanId) {
        return thongBaoRepository.findByTaiKhoanIdOrderByNgayTaoDesc(taiKhoanId).stream()
                .filter(tb -> !tb.isDaDoc())
                .toList();
    }

    public List<ThongBao> getAll(Long taiKhoanId) {
        return thongBaoRepository.findByTaiKhoanIdOrderByNgayTaoDesc(taiKhoanId);
    }

    public long getUnreadCount(Long taiKhoanId) {
        return thongBaoRepository.countByTaiKhoanIdAndDaDocFalse(taiKhoanId);
    }

    public void markAsRead(Long thongBaoId) {
        thongBaoRepository.findById(thongBaoId).ifPresent(tb -> {
            tb.setDaDoc(true);
            thongBaoRepository.save(tb);
        });
    }

    public void markAllAsRead(Long taiKhoanId) {
        List<ThongBao> unread = thongBaoRepository.findByTaiKhoanIdOrderByNgayTaoDesc(taiKhoanId);
        unread.forEach(tb -> tb.setDaDoc(true));
        thongBaoRepository.saveAll(unread);
    }
}
