# 📚 HỆ THỐNG QUẢN LÝ THƯ VIỆN
![version](https://img.shields.io/badge/version-1.1.0-blue.svg)
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
### 🔐 Xác thực & Phân quyền
- Đăng nhập / Đăng ký / Quên mật khẩu.
- Phân quyền 4 cấp: **Admin (1)**, **Thủ thưu (2)**, **Nhân viên (3)**, **Khách hàng (4)**.
- Mật khẩu lưu trữ an toàn (NoOp hoặc BCrypt tùy cấu hình).

### 📚 Quản lý Sách
- CRUD sách với ảnh bìa (Hệ thống upload ảnh tối ưu).
- Tìm kiếm sách theo tên, tác giả, NXB.
- **Tính năng Đặt trước (Book Reservation)**: Cho phép độc giả đặt trước sách khi chưa có điều kiện đến thư viện ngay.
- Quản lý danh mục: Tác giả, NXB, Loại sách, Kệ sách.

### 👥 Quản lý Độc giả
- Quản lý thông tin độc giả chi tiết.
- Tự động tạo bản ghi độc giả cho tài khoản khách hàng mới.
- Xem lịch sử mượn và đặt trước sách.

### 📖 Mượn - Trả Sách
- Quy trình mượn/trả chuyên nghiệp.
- Tự động kiểm tra và cập nhật số lượng sách trong kho.
- Tính phí quá hạn tự động (5,000đ/ngày).

### 💬 Diễn đàn & Bình luận
- Hệ thống diễn đàn trao đổi chung.
- Bình luận và đánh giá (Rating) trực tiếp trên từng đầu sách.
- Giao diện bình luận theo phong cách hiện đại.

### 📊 Thống kê & Dashboard
- Dashboard trực quan cho Admin với các chỉ số: Tổng sách, Độc giả, Doanh thu.
- Biểu đồ thống kê chi tiết (Chart.js).

---

## 🛠 CÔNG NGHỆ SỬ DỤNG
| Công nghệ | Phiên bản | Mục đích |
| :--- | :--- | :--- |
| **Java** | 17 | Ngôn ngữ lập trình chính |
| **Spring Boot** | 3.2.2 | Framework ứng dụng |
| **Spring Security** | 6.x | Bảo mật và Phân quyền |
| **Spring Data JPA** | 3.x | Quản lý cơ sở dữ liệu (ORM) |
| **Thymeleaf** | 3.1 | Template Engine |
| **MySQL** | 8.0 | Cơ sở dữ liệu quan hệ |
| **Vanilla CSS** | - | Giao diện tùy chỉnh (Custom Premium UI) |
| **Maven** | 3.9 | Quản lý dự án và build |
| **DevTools** | - | Tự động restart và hot-reload |

---

## 🚀 CÀI ĐẶT VÀ CHẠY
### Yêu cầu hệ thống
- JDK 17+
- Maven 3.9+
- MySQL 8.0+

### Bước 1: Cấu hình Database
Tạo database tên `library_db` (hoặc tên tùy chọn trong `application.properties`).

### Bước 2: Cài đặt Properties
Cập nhật `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/qltv?useSSL=false
spring.datasource.username=root
spring.datasource.password=your_password
app.upload.dir=uploads/img/sach/
```

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
│   │   ├── config/      # Cấu hình Security, Web, DataLoader
│   │   ├── controller/  # Xử lý Request (Admin, User, Forum...)
│   │   ├── entity/      # Các thực thể dữ liệu (Sach, DocGia, DatTruoc...)
│   │   ├── repository/  # Giao tiếp Database (Spring Data JPA)
│   └── resources/
│       ├── static/      # CSS, JS, Image (Logo, UI)
│       ├── templates/   # Giao diện Thymeleaf (HTML)
│       └── uploads/     # Thư mục chứa ảnh bìa sách tải lên
```

---

## 📖 HƯỚNG DẪN SỬ DỤNG
1. **Tìm kiếm sách**: Sử dụng thanh tìm kiếm thông minh tại trang chủ.
2. **Đặt trước sách**: Click nút "Đặt trước" tại trang danh sách sách, chọn ngày dự kiến lấy sách.
3. **Quản lý (Admin)**: Truy cập Dashboard để quản duyệt các yêu cầu đặt trước và chuyển đổi thành phiếu mượn chỉ với 1 click.

---

## 📄 GIẤY PHÉP
Phát hành dưới giấy phép **MIT**.

<p align="center"> 
  <b>Made with ❤️ by Antigravity AI & You</b><br> 
  <sub>© 2026 Library Management System. All rights reserved.</sub> 
</p>
