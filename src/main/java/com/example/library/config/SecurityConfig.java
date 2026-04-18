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
                // 1. Static Resources (Public)
                .requestMatchers("/css/**", "/js/**", "/img/**", "/webjars/**").permitAll()
                
                // 2. Public Pages (Access without login)
                .requestMatchers("/", "/sach/**", "/products/**", "/about", "/error").permitAll()
                
                // 3. Unauthenticated Routes (Auth-related pages)
                .requestMatchers("/login", "/register", "/dangky", "/forgot-password", "/reset-password").permitAll()
                
                // 4. Staff & Admin Routes
                .requestMatchers("/admin/dat-truoc/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_THUTHU", "ROLE_NHANVIEN")
                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/phieunhap/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_THUTHU", "ROLE_NHANVIEN")
                
                // 5. Restricted Actions (Staff only)
                .requestMatchers("/docgia/add", "/docgia/edit/**", "/docgia/delete/**", "/docgia/save").hasAnyAuthority("ROLE_ADMIN", "ROLE_THUTHU", "ROLE_NHANVIEN")
                .requestMatchers("/phieumuon/add", "/phieumuon/edit/**", "/phieumuon/delete/**", "/phieumuon/save", "/phieumuon/thanh-toan/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_THUTHU", "ROLE_NHANVIEN")
                
                // 6. Everything else requires login
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