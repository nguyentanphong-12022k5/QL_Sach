package com.example.library.service;

import com.example.library.entity.TaiKhoan;
import com.example.library.repository.TaiKhoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(">>> Đang tìm tài khoản: " + username);

        TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println(">>> KHÔNG TÌM THẤY tài khoản: " + username);
                    return new UsernameNotFoundException("Không tìm thấy tài khoản: " + username);
                });

        System.out.println(">>> Tìm thấy tài khoản: " + taiKhoan.getUsername());
        System.out.println(">>> Quyền số: " + taiKhoan.getQuyen());

        // Chuyển đổi quyền số thành role
        String role;
        switch (taiKhoan.getQuyen()) {
            case 1:
                role = "ROLE_ADMIN";
                break;
            case 2:
                role = "ROLE_THUTHU";
                break;
            case 3:
                role = "ROLE_NHANVIEN";
                break;
            default:
                role = "ROLE_KHACHHANG";
                break;
        }
        
        System.out.println(">>> Role gán: " + role);

        return User.builder()
                .username(taiKhoan.getUsername())
                .password(taiKhoan.getPassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(role)))
                .accountLocked(taiKhoan.getTrangThai() != 1)
                .build();
    }
}