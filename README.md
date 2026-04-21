# 📚 HỆ THỐNG QUẢN LÝ THƯ VIỆN

![version](https://img.shields.io/badge/version-1.9.0-purple.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.2-green.svg)
![Java](https://img.shields.io/badge/Java-17-orange.svg)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)
![license](https://img.shields.io/badge/license-MIT-yellow.svg)

## 📋 MỤC LỤC

1. [Giới thiệu](#-giới-thiệu)
2. [Tính năng](#-tính-năng)
3. [Công nghệ sử dụng](#-công-nghệ-sử-dụng)
4. [Cài đặt và Chạy](#-cài-đặt-và-chạy)
5. [Cấu trúc dự án](#-cấu-trúc-dự-án)
6. [Tài khoản mặc định](#-tài-khoản-mặc-định)
7. [API Endpoints](#-api-endpoints)
8. [Cơ sở dữ liệu](#-cơ-sở-dữ-liệu)
9. [Hướng dẫn sử dụng](#-hướng-dẫn-sử-dụng)
10. [Giấy phép](#-giấy-phép)

---

## 📸 GIAO DIỆN PREMIUM (v1.9.0)

### 🚀 UI/UX Master Polish (v1.9.0)
Hệ thống đã được tinh chỉnh toàn diện về mặt thẩm mỹ và trải nghiệm người dùng:
- **Layout Overlap Fix**: Xử lý triệt để lỗi thẻ nội dung đè lên menu điều hướng bằng hệ thống Z-index phân lớp.
- **Accessibility Master**: Khắc phục hiện tượng chữ bị mờ khi hoạt ảnh fade-in và tăng độ tương phản cho toàn bộ nút bấm.
- **Security Nav**: Tự động ẩn các thành phần quản trị (Dashboard) đối với người dùng phổ thông trên toàn hệ thống.

### 📸 Admin QR Scanner (v1.8.0)
Thủ thư có thể dùng camera để quét mã thẻ độc giả, tự động điền thông tin mượn sách.

![Admin QR Scanner](file:///C:/Users/Lenovo/.gemini/antigravity/brain/b784ee68-adbe-4596-8357-2be46508223e/qr_scanner_modal_active_1776737182694.png)
_Giao diện quét mã QR tích hợp trực tiếp trong form tạo phiếu mượn._

---

## 📖 GIỚI THIỆU

Hệ thống Quản lý Thư viện là một ứng dụng web hiện đại được xây dựng bằng **Spring Boot 3** và **Thymeleaf**, cung cấp giải pháp quản lý toàn diện cho thư viện với giao diện Premium và trải nghiệm người dùng tối ưu.

---

## ✨ TÍNH NĂNG

### 🔐 Xác thực & Phân quyền (Updated)

- **Public Pages:** Cho phép khách truy cập không cần đăng nhập vào **Trang chủ (`/`)**, **Danh sách sách (`/sach`, `/products`)**, và **Trang giới thiệu (`/about`)**.
- **Unauthenticated Routes:** Route dành riêng cho người dùng chưa đăng nhập (Login/Register).
- **Smart Redirect:** Tự động đưa người dùng đã đăng nhập thoát khỏi trang login về trang chủ.
- **Phân quyền 4 cấp:** Admin, Thủ thư, Nhân viên, Khách hàng.

### 📚 Quản lý Sách

- CRUD sách với ảnh bìa (Hệ thống upload ảnh tối ưu).
- **Tính năng Đặt trước (Book Reservation):**
  - Cho phép chọn ngày hẹn lấy sách.
  - **Mới:** Cho phép người dùng nhập **số lượng** quyển sách muốn đặt trước.
  - Tự động kiểm tra tồn kho và giới hạn số lượng đặt.

### 👥 Quản lý Độc giả

- Quản lý thông tin độc giả chi tiết, tự động tạo bản ghi cho khách hàng mới.
- **Bảo mật Dữ liệu Độc giả (Privacy Protection):** 
  - Ẩn số điện thoại và địa chỉ của độc giả khác đối với cấp độ Khách hàng. 
  - Chỉ Admin/Thủ thư/Nhân viên mới thấy thông tin liên hệ đầy đủ.
- **Hệ thống Xếp hạng Độc giả (Tu Tiên Ranking):**
  - Tự động xếp hạng dựa trên 6 cấp bậc tu luyện (Phàm Nhân -> Hóa Thần).
  - **Bảng Xếp Hạng (Leaderboard):** Trang vinh danh những người mượn sách nhiều nhất.
- Xem lịch sử mượn và đặt trước sách (Hiển thị chi tiết số lượng).
- **Thẻ Thư Viện Kỹ Thuật Số (Digital Library Card):**
  - Mỗi độc giả có một mã QR định danh riêng biệt.
  - Hỗ trợ tải mã QR về thiết bị để sử dụng offline.

---

## 🛠 LỊCH SỬ SỬA LỖI & BẢO TRÌ (BUG FIX LOG)

| Ngày | Vấn đề (Bug) | Giải pháp (Fix) | Ghi chú kỹ thuật |
| :--- | :--- | :--- | :--- |
| 21/04/2026 | **UI/UX Master Polish (v1.9.0)** | Sửa lỗi overlap header, tăng tương phản nút bấm và tối ưu hiệu ứng fade-in. | Nâng cấp Z-index lên 2000 cho header và 3000 cho dropdown content. |
| 21/04/2026 | **Security Audit: Dashboard Nav** | Phân quyền nghiêm ngặt nút Dashboard trên toàn bộ list templates. | Sử dụng `sec:authorize` để ẩn nút quản trị đối với ROLE_READER. |
| 21/04/2026 | **Lỗi tàng hình icon "Tất Cả"** | Sửa quy tắc CSS tranh chấp khiến icon cùng màu nền khi active. | Thêm bộ chọn `.btn-primary.btn-outline i { color: white !important; }`. |
| 21/04/2026 | **Admin QR Scanning (v1.8.0)** | Tích hợp máy quét QR vào form mượn sách và tìm kiếm theo ID Độc giả. | Sử dụng `html5-qrcode` cho frontend và sửa lỗi ánh xạ ID giữa Tài khoản/Độc giả. |
| 21/04/2026 | **Digital Library Card (v1.7.0)** | Triển khai mã QR định danh độc giả tích hợp trong Profile. | Áp dụng công nghệ Zxing để tạo mã QR dạng `READER:{id}` phục vụ quét tại quầy. |
| 20/04/2026 | **Global Header System (v1.6.0)** | Trung tâm hóa thanh điều hướng bằng Fragment, đồng bộ Linh Thạch & Thông báo. | Xóa bỏ sự không nhất quán giữa các trang (Home vs Sách). |
| 20/04/2026 | **Hệ thống Linh Thạch (v1.5.0)** | Triển khai loyalty points, thưởng mượn/trả sách & bình luận. | Biến thư viện thành thế giới Tu Tiên đích thực. |
| 20/04/2026 | **Hệ thống Thông báo (v1.4.0)** | Triển khai Automated Notifications, chuông thông báo toàn cục. | Tăng tính tương tác giữa thư viện và độc giả. |

---

## 📄 GIẤY PHÉP

Phát hành dưới giấy phép **MIT**.

<p align="center"> 
  <b>Made with ❤️ by Antigravity AI & You</b><br> 
  <sub>© 2026 Library Management System. All rights reserved.</sub> 
</p>
