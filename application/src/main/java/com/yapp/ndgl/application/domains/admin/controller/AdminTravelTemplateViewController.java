package com.yapp.ndgl.application.domains.admin.controller;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yapp.ndgl.application.domains.travel.facade.TravelTemplateFacade;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateHighlightsResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateItineraryResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateItineraryResponse.ItineraryPlaceResponse;
import com.yapp.ndgl.common.response.SliceResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplatePopularResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/travel-templates")
@RequiredArgsConstructor
public class AdminTravelTemplateViewController {

    private final TravelTemplateFacade travelTemplateFacade;

    @GetMapping
    public String listPage(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "20") int size,
        Model model
    ) {
        try {
            SliceResponse<TravelTemplatePopularResponse> result =
                travelTemplateFacade.readPopularTravelTemplates(null, page, size);
            model.addAttribute("templates", result.getContent());
            model.addAttribute("hasNext", result.isHasNext());
            model.addAttribute("currentPage", page);
            model.addAttribute("size", size);
        } catch (Exception e) {
            log.error("여행 템플릿 목록 조회 실패", e);
            model.addAttribute("errorMessage", "목록을 불러오는 중 오류가 발생했습니다.");
            model.addAttribute("templates", List.of());
            model.addAttribute("hasNext", false);
            model.addAttribute("currentPage", page);
            model.addAttribute("size", size);
        }
        return "admin/travel-template-list";
    }

    @GetMapping("/{id}")
    public String detailPage(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            // 어드민 전용 조회 (조회수 미증가)
            TravelTemplateHighlightsResponse highlights =
                travelTemplateFacade.readTravelTemplateHighlightsForAdmin(id);

            // null day 전달 시 Spring Data JPA가 IS NULL 쿼리를 실행하므로 day별 개별 조회
            Map<Integer, List<ItineraryPlaceResponse>> groupedItinerary = new TreeMap<>();
            for (int day = 1; day <= highlights.days(); day++) {
                TravelTemplateItineraryResponse dayItinerary =
                    travelTemplateFacade.readTravelTemplateItinerary(id, day);
                if (dayItinerary != null && dayItinerary.itineraries() != null
                    && !dayItinerary.itineraries().isEmpty()) {
                    List<ItineraryPlaceResponse> sorted = dayItinerary.itineraries().stream()
                        .sorted(Comparator.comparingInt(ItineraryPlaceResponse::sequence))
                        .collect(Collectors.toList());
                    groupedItinerary.put(day, sorted);
                }
            }

            model.addAttribute("highlights", highlights);
            model.addAttribute("groupedItinerary", groupedItinerary);
            return "admin/travel-template-detail";
        } catch (Exception e) {
            log.error("여행 템플릿 상세 조회 실패. id = {}", id, e);
            redirectAttributes.addFlashAttribute("errorMessage", "템플릿을 찾을 수 없습니다.");
            return "redirect:/admin/travel-templates";
        }
    }
}
