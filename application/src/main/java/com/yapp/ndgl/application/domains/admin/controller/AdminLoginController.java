package com.yapp.ndgl.application.domains.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yapp.ndgl.application.config.AdminAuthInterceptor;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminLoginController {

    private final AdminAuthInterceptor adminAuthInterceptor;

    @GetMapping
    public String home(HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("adminAuthenticated"))) {
            return "redirect:/admin/travel-templates";
        }
        return "redirect:/admin/login";
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("adminAuthenticated"))) {
            return "redirect:/admin/travel-templates";
        }
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("token") String token, HttpSession session, Model model) {
        if (adminAuthInterceptor.validateToken(token)) {
            session.setAttribute("adminAuthenticated", true);
            log.info("관리자 인증 성공");
            return "redirect:/admin/travel-templates";
        }
        log.warn("관리자 인증 실패: 잘못된 토큰");
        model.addAttribute("error", "인증 토큰이 올바르지 않습니다.");
        return "admin/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        log.info("관리자 로그아웃");
        return "redirect:/admin/login";
    }
}
