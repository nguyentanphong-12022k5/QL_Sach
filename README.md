# 📚 HỆ THỐNG QUẢN LÝ THƯ VIỆN
![version](https://img.shields.io/badge/version-1.2.0-blue.svg)
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
- Xem lịch sử mượn và đặt trước sách (Hiển thị chi tiết số lượng).

### 📖 Mượn - Trả Sách
- Quy trình mượn/trả chuyên nghiệp, tự động cập nhật số lượng sách trong kho.
- Chuyển đổi yêu cầu đặt trước thành phiếu mượn chỉ với 1 click từ Admin.

### 💬 Diễn đàn & Bình luận
- Hệ thống diễn đàn trao đổi chung và đánh giá (Rating) trực tiếp trên từng đầu sách.

### 📊 Thống kê & Dashboard
- Dashboard trực quan cho Admin với biểu đồ xu hướng và các chỉ số vận hành.

---

## 🛠 CÔNG NGHỆ SỬ DỤNG
| Công nghệ | Phiên bản | Mục đích |
| :--- | :--- | :--- |
| **Java** | 17 | Ngôn ngữ lập trình chính |
| **Spring Boot** | 3.2.2 | Framework ứng dụng |
| **Spring Security** | 6.x | Bảo mật, Phân quyền & Public Routes |
| **Spring Data JPA** | 3.x | Quản lý cơ sở dữ liệu (ORM) |
| **Thymeleaf** | 3.1 | Template Engine |
| **MySQL** | 8.0 | Cơ sở dữ liệu quan hệ |
| **Vanilla CSS** | - | Giao diện tùy chỉnh (Custom Premium UI) |

---

## 🚀 CÀI ĐẶT VÀ CHẠY
### Bước 1: Cấu hình Database
Tạo database tên `qltv`.

### Bước 2: Cài đặt Properties
Cập nhật `src/main/resources/application.properties` các thông số kết nối DB.

### Bước 3: Chạy ứng dụng
```bash
mvn spring-boot:run
```
Truy cập: `http://localhost:8080`

---

## 🔑 TÀI KHOẢN MẶC ĐỊNH
| Username | Password | Quyền |
| :--- | :--- | :--- |
| **admin** | admin123 | Admin (Toàn quyền) |
| **thuthu** | thuthu123 | Thủ thư (Quản lý sách/mượn) |
| **nhanvien** | nhanvien123 | Nhân viên (Quản lý mượn/độc giả) |
| **Tanphong** | Tanphong | Khách hàng (Mượn/Đặt sách) |

---

## 📁 CẤU TRÚC DỰ ÁN
```text
src/
├── main/
│   ├── java/com/example/library/
│   │   ├── config/      # SecurityConfig (Public/Private Routes)
│   │   ├── controller/  # AuthController, HomeController, SachController...
│   │   ├── entity/      # Sach, DocGia, DatTruoc...
│   │   ├── repository/  # Spring Data JPA Repositories
│   └── resources/
│       ├── static/      # CSS, JS, Image
│       ├── templates/   # Thymeleaf (index, sach, about, auth...)
```

---

## 📖 HƯỚNG DẪN SỬ DỤNG
1. **Dành cho Khách (Chưa Login)**: Có thể xem danh sách sách (`/sach`), xem thông tin thư viện (`/about`) và tìm kiếm sách.
2. **Dành cho Thành viên**: Đăng nhập để sử dụng tính năng **Đặt trước sách** (được chọn số lượng quyển muốn đặt).
3. **Dành cho Quản lý**: Đăng nhập tài khoản Admin/Thủ thư để duyệt các yêu cầu đặt trước và quản lý kho sách.

---

## 📄 GIẤY PHÉP
Phát hành dưới giấy phép **MIT**.

<p align="center"> 
  <b>Made with ❤️ by Antigravity AI & You</b><br> 
  <sub>© 2026 Library Management System. All rights reserved.</sub> 
</p>

