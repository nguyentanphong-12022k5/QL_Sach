# 📚 HỆ THỐNG QUẢN LÝ THƯ VIỆN - LUYỆN KHÍ ĐƯỜNG

<p align="center">
  <img src="https://img.shields.io/badge/version-1.9.0-purple.svg?style=for-the-badge" alt="version">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.2.2-green.svg?style=for-the-badge" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Java-17-orange.svg?style=for-the-badge" alt="Java">
  <img src="https://img.shields.io/badge/MySQL-8.0-blue.svg?style=for-the-badge" alt="MySQL">
</p>

---

## 📖 GIỚI THIỆU

**Luyện Khí Đường** không chỉ là một hệ thống quản lý thư viện thông thường. Đây là một nền tảng quản trị tri thức kết hợp với các yếu tố **Tu Tiên (Cultivation)** độc đáo, mang lại trải nghiệm thú vị cho cả độc giả và quản lý. Hệ thống được xây dựng trên nền tảng **Spring Boot 3** hiện đại, tập trung vào hiệu suất cao, bảo mật chặt chẽ và giao diện người dùng Premium.

> [!TIP]
> Hệ thống tích hợp mã QR thông minh giúp việc mượn/trả sách diễn ra trong tích tắc!

---

## ✨ TÍNH NĂNG NỔI BẬT

### 🛡️ Bảo mật & Phân quyền (RBAC)
- **4 Cấp độ quyền hạn:** Admin, Thủ thư, Nhân viên, Độc giả.
- **Smart Security:** Tự động điều hướng và ẩn/hiện các thành phần giao diện dựa trên vai trò.
- **Privacy Protection:** Bảo vệ thông tin nhạy cảm của độc giả (ẩn SDT/Địa chỉ đối với vai trò thấp).

### 🏷️ Hệ thống Tu Tiên & Linh Thạch (Unique!)
- **Tu Vi Cấp Bậc:** Độc giả được xếp hạng dựa trên số lượng sách đã mượn:
  - **Phàm Nhân:** Mới bắt đầu.
  - **Luyện Khí:** 1 - 5 quyển.
  - **Trúc Cơ:** 6 - 15 quyển.
  - **Kim Đan:** 16 - 30 quyển.
  - **Nguyên Anh:** 31 - 50 quyển.
  - **Hóa Thần:** Trên 50 quyển.
- **Linh Thạch (Loyalty Points):** Đơn vị tiền tệ trong hệ thống dùng để nhận quà, thực hiện các tương tác đặc biệt. Tự động cộng Linh Thạch khi mượn/trả sách đúng hạn hoặc tham gia bình luận.

### 📱 Thẻ Thư Viện Kỹ Thuật Số
- Tích hợp mã QR định danh cho mỗi Độc giả.
- Hỗ trợ **QR Scanner** cho Thủ thư để xử lý phiếu mượn siêu tốc.

### 📅 Đặt Trước Sách (Reservation)
- Đặt lịch hẹn lấy sách với số lượng tùy chọn.
- Hệ thống tự động kiểm tra tồn kho và thông báo khi sách sẵn sàng.

---

## 🛠 CÔNG NGHỆ SỬ DỤNG

### Backend
- **Framework:** Spring Boot 3.2.2 (Java 17)
- **Security:** Spring Security 6 (RBAC, CSRF Protection)
- **Data Access:** Spring Data JPA (Hibernate 6)
- **QR Engine:** Google ZXing
- **Database:** MySQL 8.0

### Frontend
- **Template Engine:** Thymeleaf
- **Styling:** CSS3, Google Fonts (Inter, Roboto)
- **JavaScript:** Vanilla JS + jQuery
- **Scanning:** HTML5-QRCode

---

## 📂 CẤU TRÚC DỰ ÁN

```bash
f:/Project/QL_Sach
├── src/main/java/com/example/library
│   ├── config/         # Cấu hình Security, MVC, Web
│   ├── controller/     # Điều hướng Request
│   ├── entity/         # Đối tượng Cơ sở dữ liệu (Tu Tiên entities)
│   ├── repository/     # Giao tiếp Data Access
│   ├── service/        # Xử lý Logic nghiệp vụ
│   └── dto/            # Data Transfer Objects
├── src/main/resources
│   ├── templates/      # Giao diện Thymeleaf
│   ├── static/         # CSS, JS, Images
│   └── application.properties # Cấu hình hệ thống
└── uploads/            # Thư mục lưu trữ ảnh sách & avatar
```

---

## 🚀 CÀI ĐẶT VÀ CHẠY

### 1. Chuẩn bị
- JDK 17 trở lên.
- Maven 3.x.
- MySQL 8.0.

### 2. Cấu hình Cơ sở dữ liệu
```sql
CREATE DATABASE qltv;
```
Cập nhật thông tin kết nối trong `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/qltv
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

### 3. Chạy ứng dụng
```bash
mvn clean install
mvn spring-boot:run
```
Truy cập tại: `http://localhost:8080`

---

## 🔑 TÀI KHOẢN MẶC ĐỊNH

| Vai trò | Username | Password |
| :--- | :--- | :--- |
| **Quản trị viên** | admin | 123 |
| **Thủ thư** | thuthu | 123 |
| **Nhân viên** | nhanvien | 123 |

---

## 📊 CƠ SỞ DỮ LIỆU (DATABASE SCHEMA)

Hệ thống bao gồm 16 bảng chính, các bảng quan trọng nhất:
- `taikhoan`: Lưu trữ thông tin đăng nhập và Linh Thạch.
- `docgia`: Thông tin cá nhân, cấp bậc Tu Tiên và Mã QR.
- `sach`: Kho tàng tri thức (thông tin sách, tồn kho).
- `phieumuon`: Theo dõi quá trình mượn trả và tính điểm Linh Thạch.
- `thongbao`: Hệ thống thông báo đẩy real-time.

---

## 📜 BUG FIX LOG & UPDATE HISTORY

| Phiên bản | Ngày | Nội dung cập nhật |
| :--- | :--- | :--- |
| **v1.9.0** | 21/04/26 | Polish UI/UX, Z-index Fix, Accessibility improvement. |
| **v1.8.0** | 21/04/26 | Tích hợp Admin QR Scanning, tự động nhận diện độc giả. |
| **v1.7.0** | 21/04/26 | Digital Library Card (Mã QR định danh độc giả). |
| **v1.6.0** | 20/04/26 | Global Header System, đồng bộ Linh Thạch & Notify. |
| **v1.5.0** | 20/04/26 | Hệ thống Linh Thạch (Loyalty Points) & Tu Tiên Rank. |

---

<p align="center"> 
  <b>Made with ❤️ by Antigravity AI & You</b><br> 
  <sub>© 2026 Library Management System. All rights reserved.</sub> 
</p>

