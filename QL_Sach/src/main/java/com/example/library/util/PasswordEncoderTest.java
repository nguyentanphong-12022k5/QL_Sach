package com.example.library.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Tạo mật khẩu mới
        String rawPassword = "123456";
        String encodedPassword = encoder.encode(rawPassword);

        System.out.println("Mật khẩu gốc: " + rawPassword);
        System.out.println("Mật khẩu mã hóa: " + encodedPassword);
        System.out.println("Copy dòng này vào database: " + encodedPassword);

        // Kiểm tra mật khẩu
        boolean matches = encoder.matches(rawPassword, encodedPassword);
        System.out.println("Kiểm tra mật khẩu: " + matches);

        // Tạo nhiều mật khẩu khác
        System.out.println("\n=== CÁC MẬT KHẨU MÃ HÓA ===");
        System.out.println("admin123 -> " + encoder.encode("admin123"));
        System.out.println("123456 -> " + encoder.encode("123456"));
        System.out.println("password -> " + encoder.encode("password"));
    }
}