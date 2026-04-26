package com.example.library.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Tạo mật khẩu cho admin
        String adminPass = encoder.encode("admin123");
        System.out.println("Admin password: " + adminPass);

        // Tạo mật khẩu cho thuthu
        String thuthuPass = encoder.encode("thuthu123");
        System.out.println("Thuthu password: " + thuthuPass);
    }
}