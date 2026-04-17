package com.example.library;

import com.example.library.controller.DatTruocController;
import com.example.library.entity.DatTruoc;
import com.example.library.entity.DocGia;
import com.example.library.entity.Sach;
import com.example.library.entity.TaiKhoan;
import com.example.library.repository.DatTruocRepository;
import com.example.library.repository.DocGiaRepository;
import com.example.library.repository.SachRepository;
import com.example.library.repository.TaiKhoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DatTruocTest {

    @InjectMocks
    private DatTruocController datTruocController;

    @Mock private SachRepository sachRepository;
    @Mock private DocGiaRepository docGiaRepository;
    @Mock private DatTruocRepository datTruocRepository;
    @Mock private TaiKhoanRepository taiKhoanRepository;
    @Mock private Model model;
    @Mock private RedirectAttributes redirectAttributes;
    @Mock private Authentication authentication;
    @Mock private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
    }

    @Test
    void testDatTruocForm() {
        Sach sach = new Sach();
        sach.setId(1L);
        sach.setTenSach("Test Book");
        
        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setUsername("testuser");
        taiKhoan.setHoTen("Test User");

        when(sachRepository.findById(1L)).thenReturn(Optional.of(sach));
        when(taiKhoanRepository.findByUsername("testuser")).thenReturn(Optional.of(taiKhoan));
        when(docGiaRepository.findAll()).thenReturn(new ArrayList<>());

        String viewName = datTruocController.datTruocForm(1L, model);

        assertEquals("dat-truoc/form", viewName);
        verify(model).addAttribute(eq("sach"), any(Sach.class));
    }

    @Test
    void testSubmitDatTruocSuccess() {
        Sach sach = new Sach();
        sach.setId(1L);
        DocGia docGia = new DocGia();
        docGia.setId(1L);

        when(sachRepository.findById(1L)).thenReturn(Optional.of(sach));
        when(docGiaRepository.findById(1L)).thenReturn(Optional.of(docGia));

        String dateStr = LocalDate.now().plusDays(1).toString();
        String viewName = datTruocController.submitDatTruoc(1L, 1L, dateStr, "Ghi chu", redirectAttributes);

        assertEquals("redirect:/dat-truoc/lich-su", viewName);
        verify(datTruocRepository).save(any(DatTruoc.class));
        verify(redirectAttributes).addFlashAttribute(eq("success"), anyString());
    }

    @Test
    void testSubmitDatTruocPastDate() {
        Sach sach = new Sach();
        sach.setId(1L);
        DocGia docGia = new DocGia();
        docGia.setId(1L);

        when(sachRepository.findById(1L)).thenReturn(Optional.of(sach));
        when(docGiaRepository.findById(1L)).thenReturn(Optional.of(docGia));

        String dateStr = LocalDate.now().minusDays(1).toString();
        String viewName = datTruocController.submitDatTruoc(1L, 1L, dateStr, "Ghi chu", redirectAttributes);

        assertEquals("redirect:/dat-truoc/1", viewName);
        verify(redirectAttributes).addFlashAttribute(eq("error"), contains("quá khứ"));
        verify(datTruocRepository, never()).save(any(DatTruoc.class));
    }
}
