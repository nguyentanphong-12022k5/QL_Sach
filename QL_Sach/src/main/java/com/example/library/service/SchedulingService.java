package com.example.library.service;

import com.example.library.entity.PhieuMuon;
import com.example.library.repository.PhieuMuonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SchedulingService {

    @Autowired
    private PhieuMuonRepository phieuMuonRepository;

    @Autowired
    private NotificationService notificationService;

    /**
     * Chạy mỗi ngày lúc 8:00 AM để kiểm tra hạn trả sách
     * Cron: giây phút giờ ngày tháng thứ
     */
    @Scheduled(cron = "0 0 8 * * *")
    public void checkOverdueBooks() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        // 1. Cảnh báo sắp đến hạn (Hết hạn vào ngày mai)
        List<PhieuMuon> dueTomorrow = phieuMuonRepository.findByNgayTraIsNullAndNgayTraDuKien(tomorrow);
        for (PhieuMuon pm : dueTomorrow) {
            notificationService.notifyReader(pm.getDocGia(), 
                "Sách sắp đến hạn trả", 
                "Bạn có sách mượn (Mã PM: #" + pm.getId() + ") sẽ hết hạn vào ngày mai (" + tomorrow + "). Vui lòng sắp xếp trả sách đúng hạn.", 
                "WARNING");
        }

        // 2. Thông báo hết hạn hôm nay
        List<PhieuMuon> dueToday = phieuMuonRepository.findByNgayTraIsNullAndNgayTraDuKien(today);
        for (PhieuMuon pm : dueToday) {
            notificationService.notifyReader(pm.getDocGia(), 
                "Sách hết hạn hôm nay", 
                "Bạn có sách mượn (Mã PM: #" + pm.getId() + ") hết hạn vào hôm nay (" + today + "). Vui lòng trả sách để tránh phát sinh phí phạt.", 
                "DANGER");
        }

        // 3. Thông báo quá hạn (Đã quá hạn từ trước)
        List<PhieuMuon> overdue = phieuMuonRepository.findByNgayTraIsNullAndNgayTraDuKienBefore(today);
        for (PhieuMuon pm : overdue) {
            notificationService.notifyReader(pm.getDocGia(), 
                "Cảnh báo: Sách quá hạn!", 
                "Bạn có sách mượn (Mã PM: #" + pm.getId() + ") đã quá hạn trả. Vui lòng hoàn trả sách ngay lập tức để không ảnh hưởng đến cấp bậc của bạn.", 
                "DANGER");
        }
    }
}
