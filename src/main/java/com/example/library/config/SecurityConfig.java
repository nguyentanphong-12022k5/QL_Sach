package com.example.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/img/**", "/webjars/**").permitAll()
                .requestMatchers("/login", "/register", "/dangky", "/forgot-password", "/reset-password").permitAll()
                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                
                // Cấm khách hàng thao tác Xóa, Sửa, Thêm các dữ liệu quan trọng
                .requestMatchers("/docgia/add", "/docgia/edit/**", "/docgia/delete/**", "/docgia/save").hasAnyAuthority("ROLE_ADMIN", "ROLE_THUTHU", "ROLE_NHANVIEN")
                .requestMatchers("/phieumuon/add", "/phieumuon/edit/**", "/phieumuon/delete/**", "/phieumuon/save", "/phieumuon/thanh-toan/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_THUTHU", "ROLE_NHANVIEN")
                .requestMatchers("/phieunhap/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_THUTHU", "ROLE_NHANVIEN")
                
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // KHÔNG MÃ HÓA - Mật khẩu plain text
        return NoOpPasswordEncoder.getInstance();
    }
}