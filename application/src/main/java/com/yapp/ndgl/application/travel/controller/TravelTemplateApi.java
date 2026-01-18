package com.yapp.ndgl.application.travel.controller;

import com.yapp.ndgl.application.travel.controller.dto.TravelTemplateResponse;
import com.yapp.ndgl.common.response.ErrorResponse;
import com.yapp.ndgl.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Travel Template", description = "여행 템플릿 관련 API")
@RequestMapping("/api/v1/travel-templates")
public interface TravelTemplateApi {

    @Operation(
        summary = "여행 템플릿 상세 조회",
        description = "ID로 여행 템플릿 상세 정보를 조회합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(
                schema = @Schema(implementation = SuccessResponse.class),
                examples = @ExampleObject(
                    name = "SUCCESS",
                    value = """
                        {
                           "code": "2000",
                           "message": "요청에 성공하였습니다.",
                           "data": {
                             "travelId": "TRAVEL_001",
                             "traveler": "도쿄여행러버",
                             "country": "일본",
                             "city": "도쿄",
                             "weatherInfo": "여름철 고온다습, 가벼운 옷차림 권장. 우산 필수.",
                             "cultureInfo": "식당에서 팁 불필요, 조용히 식사하는 것이 예의. 신발 벗는 곳이 많음.",
                             "foodInfo": "라멘과 초밥이 유명하며, 현지 식당에서 현금 결제가 일반적. 편의점 음식도 훌륭함.",
                             "thumbnail": "https://example.com/thumbnail/tokyo.jpg",
                             "link": "https://www.youtube.com/watch?v=tokyo-travel",
                             "budgetPerPerson": 1200000,
                             "summary": "도쿄 3박 4일 여행의 모든 것. 유튜버가 직접 다녀온 코스로 구성된 완벽한 가이드.",
                             "title": "도쿄 3박 4일 완벽 여행 가이드",
                             "nights": 3,
                             "days": 4,
                             "places": [
                               {
                                 "sequence": 1,
                                 "day": 1,
                                 "travelerTip": "도쿄 타워는 저녁 시간대 방문하는 것이 좋습니다. 야경이 아름답습니다.",
                                 "place": {
                                   "placeId": "ChIJSc8jdZORQTURu6BMwxrKbGg",
                                   "formattedAddress": "일본 〒105-0011 Tokyo, Minato City, Shiba-koen, 4 Chome−2−8",
                                   "latitude": 35.6585805,
                                   "longitude": 139.7454329,
                                   "rating": 4.5,
                                   "nationalPhoneNumber": "03-3433-5111",
                                   "internationalPhoneNumber": "+81 3-3433-5111",
                                   "websiteUri": "https://www.tokyotower.co.jp/",
                                   "googleMapsUri": "https://maps.google.com/?cid=10281119591005088802",
                                   "userRatingCount": 10000
                                 }
                               },
                               {
                                 "sequence": 2,
                                 "day": 1,
                                 "travelerTip": "메이지 신궁은 조용한 분위기로 유명합니다. 아침 일찍 방문하면 더욱 좋습니다.",
                                 "place": {
                                   "placeId": "ChIJN1t_tDeuEmsRUsoyG83frY4",
                                   "formattedAddress": "일본 〒150-0001 Tokyo, Shibuya City, Jingumae, 4 Chome−2−8",
                                   "latitude": 35.6592606,
                                   "longitude": 139.7002586,
                                   "rating": 4.6,
                                   "nationalPhoneNumber": "03-3409-4811",
                                   "internationalPhoneNumber": "+81 3-3409-4811",
                                   "websiteUri": "https://www.meijijingu.or.jp/",
                                   "googleMapsUri": "https://maps.google.com/?cid=123456789",
                                   "userRatingCount": 15000
                                 }
                               },
                               {
                                 "sequence": 1,
                                 "day": 2,
                                 "travelerTip": "시부야 스크램블 스퀘어는 쇼핑과 식사 모두 즐길 수 있는 곳입니다. 옥상 전망대도 추천합니다.",
                                 "place": {
                                   "placeId": "ChIJ_xkgOmOuEmsR8FhZz3qJN1I",
                                   "formattedAddress": "일본 〒150-0043 Tokyo, Shibuya City, Dogenzaka, 2 Chome−1",
                                   "latitude": 35.6580339,
                                   "longitude": 139.7016358,
                                   "rating": 4.4,
                                   "nationalPhoneNumber": "03-3461-5111",
                                   "internationalPhoneNumber": "+81 3-3461-5111",
                                   "websiteUri": "https://www.shibuya-scramble-square.com/",
                                   "googleMapsUri": "https://maps.google.com/?cid=987654321",
                                   "userRatingCount": 8000
                                 }
                               }
                             ]
                           }
                         }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "여행 템플릿을 찾을 수 없음",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "NOT_FOUND_TRAVEL_TEMPLATE",
                    value = """
                        {
                          "code": "TRAVEL-02-001",
                          "message": "여행 템플릿을 찾을 수 없습니다",
                          "errors": []
                        }
                        """
                )
            )
        )
    })
    @GetMapping("/{id}")
    ResponseEntity<SuccessResponse<TravelTemplateResponse>> getTravelTemplate(
        @Parameter(description = "여행 템플릿 ID", example = "1", required = true)
        @PathVariable Long id
    );
}
