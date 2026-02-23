package com.yapp.ndgl.application.domains.admin.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.ndgl.application.domains.travel.controller.dto.SaveTravelTemplateRequest;
import com.yapp.ndgl.application.domains.travel.controller.dto.SaveTravelTemplateRequest.ItineraryRequest;
import com.yapp.ndgl.application.domains.travel.controller.dto.SaveTravelTemplateRequest.ActivityRequest;
import com.yapp.ndgl.application.domains.travel.controller.dto.SaveTravelTemplateRequest.TransportationRequest;
import com.yapp.ndgl.application.domains.travel.controller.dto.SaveTravelTemplateRequest.PlanBRequest;
import com.yapp.ndgl.application.domains.travel.facade.TravelTemplateFacade;
import com.yapp.ndgl.domain.travel.type.TravelProgramType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/travel-templates")
@RequiredArgsConstructor
public class AdminTravelTemplateController {

    private final TravelTemplateFacade travelTemplateFacade;
    private final ObjectMapper objectMapper;

    @GetMapping("/new")
    public String newTravelTemplatePage(Model model) {
        model.addAttribute("programTypes", TravelProgramType.values());
        return "admin/travel-template-form";
    }

    @PostMapping("/{id}/delete")
    public String deleteTravelTemplate(
        @PathVariable Long id,
        RedirectAttributes redirectAttributes
    ) {
        try {
            travelTemplateFacade.deleteTravelTemplate(id);
            log.info("관리자 여행 템플릿 삭제 완료. templateId = {}", id);
            redirectAttributes.addFlashAttribute("successMessage", "여행 템플릿(ID: " + id + ")이 삭제되었습니다.");
        } catch (Exception e) {
            log.error("관리자 여행 템플릿 삭제 실패. templateId = {}", id, e);
            redirectAttributes.addFlashAttribute("errorMessage", "삭제 실패: " + e.getMessage());
        }
        return "redirect:/admin/travel-templates";
    }

    @PostMapping("/json")
    public String saveTravelTemplateByJson(
        @RequestParam("jsonBody") String jsonBody,
        RedirectAttributes redirectAttributes
    ) {
        try {
            SaveTravelTemplateRequest request = objectMapper.readValue(jsonBody, SaveTravelTemplateRequest.class);
            Long templateId = travelTemplateFacade.saveTravelTemplate(request);
            log.info("관리자 여행 템플릿 JSON 저장 완료. templateId = {}", templateId);
            redirectAttributes.addFlashAttribute("successMessage", "여행 템플릿이 성공적으로 저장되었습니다. (ID: " + templateId + ")");
        } catch (Exception e) {
            log.error("관리자 여행 템플릿 JSON 저장 실패", e);
            redirectAttributes.addFlashAttribute("errorMessage", "저장 실패: " + e.getMessage());
            redirectAttributes.addFlashAttribute("savedJsonBody", jsonBody);
        }
        return "redirect:/admin/travel-templates/new";
    }

    @PostMapping
    public String saveTravelTemplate(
        @RequestParam("traveler") String traveler,
        @RequestParam("summary") String summary,
        @RequestParam(value = "budgetPerPerson", required = false) Integer budgetPerPerson,
        @RequestParam(value = "continent", required = false) String continent,
        @RequestParam("country") String country,
        @RequestParam(value = "countryName", required = false) String countryName,
        @RequestParam("city") String city,
        @RequestParam("travelProgramType") TravelProgramType travelProgramType,
        @RequestParam(value = "link", required = false) String link,
        @RequestParam("dayCount") int dayCount,
        @RequestParam java.util.Map<String, String> allParams,
        RedirectAttributes redirectAttributes
    ) {
        try {
            List<ItineraryRequest> itineraryList = parseItinerary(allParams, dayCount);

            SaveTravelTemplateRequest request = new SaveTravelTemplateRequest(
                traveler, summary, budgetPerPerson, continent,
                country, countryName, city, travelProgramType, link, itineraryList
            );

            Long templateId = travelTemplateFacade.saveTravelTemplate(request);
            log.info("관리자 여행 템플릿 저장 완료. templateId = {}", templateId);
            redirectAttributes.addFlashAttribute("successMessage", "여행 템플릿이 성공적으로 저장되었습니다. (ID: " + templateId + ")");
            return "redirect:/admin/travel-templates/new";
        } catch (Exception e) {
            log.error("관리자 여행 템플릿 저장 실패", e);
            redirectAttributes.addFlashAttribute("errorMessage", "저장 실패: " + e.getMessage());
            return "redirect:/admin/travel-templates/new";
        }
    }

    private List<ItineraryRequest> parseItinerary(java.util.Map<String, String> params, int dayCount) {
        List<ItineraryRequest> itineraryList = new ArrayList<>();

        for (int d = 0; d < dayCount; d++) {
            String prefix = "day[" + d + "]";
            String transportationTip = params.get(prefix + ".transportationTip");

            // 활동 개수 파악
            int activityCount = 0;
            while (params.containsKey(prefix + ".activity[" + activityCount + "].placeName")) {
                activityCount++;
            }

            List<ActivityRequest> activities = new ArrayList<>();
            for (int a = 0; a < activityCount; a++) {
                String actPrefix = prefix + ".activity[" + a + "]";

                String placeName = params.get(actPrefix + ".placeName");
                String cityEn = params.get(actPrefix + ".cityEn");
                String estimatedTimeStr = params.get(actPrefix + ".estimatedTime");
                String distanceKmStr = params.get(actPrefix + ".distanceKm");

                Integer estimatedTime = (estimatedTimeStr != null && !estimatedTimeStr.isBlank())
                    ? Integer.parseInt(estimatedTimeStr) : null;
                Double distanceKm = (distanceKmStr != null && !distanceKmStr.isBlank())
                    ? Double.parseDouble(distanceKmStr) : null;

                // 교통수단 파싱
                List<TransportationRequest> transportations = new ArrayList<>();
                int tIdx = 0;
                while (params.containsKey(actPrefix + ".transport[" + tIdx + "].mode")) {
                    String mode = params.get(actPrefix + ".transport[" + tIdx + "].mode");
                    String timeMinStr = params.get(actPrefix + ".transport[" + tIdx + "].timeMin");
                    Integer timeMin = (timeMinStr != null && !timeMinStr.isBlank())
                        ? Integer.parseInt(timeMinStr) : null;
                    if (mode != null && !mode.isBlank()) {
                        transportations.add(new TransportationRequest(mode, timeMin));
                    }
                    tIdx++;
                }

                // 여행자 팁 파싱
                List<String> tips = new ArrayList<>();
                int tipIdx = 0;
                while (params.containsKey(actPrefix + ".tip[" + tipIdx + "]")) {
                    String tip = params.get(actPrefix + ".tip[" + tipIdx + "]");
                    if (tip != null && !tip.isBlank()) {
                        tips.add(tip);
                    }
                    tipIdx++;
                }

                // PlanB 파싱
                List<PlanBRequest> planBList = new ArrayList<>();
                int pbIdx = 0;
                while (params.containsKey(actPrefix + ".planB[" + pbIdx + "].name")) {
                    String pbName = params.get(actPrefix + ".planB[" + pbIdx + "].name");
                    String pbCityEn = params.get(actPrefix + ".planB[" + pbIdx + "].cityEn");
                    if (pbName != null && !pbName.isBlank()) {
                        planBList.add(new PlanBRequest(pbName, pbCityEn));
                    }
                    pbIdx++;
                }

                activities.add(new ActivityRequest(
                    a + 1, placeName, cityEn, estimatedTime, distanceKm,
                    transportations.isEmpty() ? null : transportations,
                    tips.isEmpty() ? null : tips,
                    planBList.isEmpty() ? null : planBList
                ));
            }

            itineraryList.add(new ItineraryRequest(d + 1, activities, transportationTip));
        }

        return itineraryList;
    }
}
