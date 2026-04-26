package com.example.library.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerateHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        String adminPass = encoder.encode("admin123");
        String thuthuPass = encoder.encode("thuthu123");
        String nhanvienPass = encoder.encode("nhanvien123");
        
        System.out.println("=== COPY CÁC DÒNG NÀY VÀO MYSQL ===");
        System.out.println("UPDATE taikhoan SET password = '" + adminPass + "' WHERE username = 'admin';");
        System.out.println("UPDATE taikhoan SET password = '" + thuthuPass + "' WHERE username = 'thuthu';");
        System.out.println("UPDATE taikhoan SET password = '" + nhanvienPass + "' WHERE username = 'nhanvien';");
        System.out.println();
        System.out.println("=== HOẶC DÙNG TRONG INSERT ===");
        System.out.println("admin123    -> " + adminPass);
        System.out.println("thuthu123   -> " + thuthuPass);
        System.out.println("nhanvien123 -> " + nhanvienPass);
    }
}