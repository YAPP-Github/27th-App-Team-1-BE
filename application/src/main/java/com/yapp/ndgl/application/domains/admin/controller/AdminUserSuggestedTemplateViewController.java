package com.yapp.ndgl.application.domains.admin.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yapp.ndgl.application.domains.travel.controller.dto.AdminUserSuggestedTemplateResponse;
import com.yapp.ndgl.application.domains.travel.facade.UserSuggestedTemplateFacade;
import com.yapp.ndgl.common.response.PageResponse;
import com.yapp.ndgl.common.type.SuggestionStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/user-suggested-templates")
@RequiredArgsConstructor
public class AdminUserSuggestedTemplateViewController {

    private final UserSuggestedTemplateFacade userSuggestedTemplateFacade;

    @GetMapping
    public String listPage(
        @RequestParam(value = "status", required = false) SuggestionStatus status,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "12") int size,
        Model model
    ) {
        try {
            PageResponse<AdminUserSuggestedTemplateResponse> result =
                userSuggestedTemplateFacade.readUserSuggestedTemplatesForAdmin(status, page, size);
            model.addAttribute("templates", result.getContent());
            model.addAttribute("hasNext", result.isHasNext());
            model.addAttribute("hasPrevious", result.isHasPrevious());
            model.addAttribute("totalElements", result.getTotalElements());
            model.addAttribute("totalPages", result.getTotalPages());
            model.addAttribute("currentPage", page);
            model.addAttribute("size", size);
            model.addAttribute("status", status);
            model.addAttribute("statuses", SuggestionStatus.values());
        } catch (Exception e) {
            log.error("사용자 제안 템플릿 목록 조회 실패", e);
            model.addAttribute("errorMessage", "목록을 불러오는 중 오류가 발생했습니다.");
            model.addAttribute("templates", List.of());
            model.addAttribute("hasNext", false);
            model.addAttribute("hasPrevious", false);
            model.addAttribute("totalElements", 0L);
            model.addAttribute("totalPages", 0);
            model.addAttribute("currentPage", page);
            model.addAttribute("size", size);
            model.addAttribute("status", status);
            model.addAttribute("statuses", SuggestionStatus.values());
        }
        return "admin/user-suggested-template-list";
    }
}
