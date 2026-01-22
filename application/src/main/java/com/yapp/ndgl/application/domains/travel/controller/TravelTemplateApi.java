package com.yapp.ndgl.application.domains.travel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateHighlightsResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateItineraryResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateResponse;
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
import jakarta.validation.constraints.Min;

@Tag(name = "Travel Template", description = "여행 템플릿 관련 API")
public interface TravelTemplateApi {

    @Operation(
        summary = "여행 템플릿 상세 조회",
        description = "ID로 여행 템플릿 상세 정보를 조회합니다.",
        deprecated = true
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(
                schema = @Schema(implementation = TravelTemplateResponse.class),
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
    ResponseEntity<SuccessResponse<TravelTemplateResponse>> getTravelTemplate(
        @Parameter(description = "여행 템플릿 ID", example = "1", required = true)
        @PathVariable Long id
    );

    @Operation(
        summary = "여행 템플릿 상세 조회",
        description = "ID로 여행 템플릿 상세 정보를 조회합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(
                schema = @Schema(implementation = TravelTemplateHighlightsResponse.class),
                examples = @ExampleObject(
                    name = "SUCCESS",
                    value = """
                        {
                            "code": "2000",
                            "message": "요청에 성공하였습니다.",
                            "data": {
                                "travelId": "TRAVEL_001",
                                "country": "태국",
                                "city": "방콕",
                                "budgetPerPerson": 1200000,
                                "nights": 3,
                                "days": 4,
                                "youtube": {
                                    "title": "방콕 풀코스, 동남아 안 가본 곽튜브와 함께 【방콕】",
                                    "youtuber": "빠니보틀",
                                    "thumbnail": "https://i.ytimg.com/vi/F2utz6L76D0/mqdefault.jpg",
                                    "profileImage": "프로필 이미지",
                                    "link": "https://www.youtube.com/watch?v=F2utz6L76D0",
                                    "summary": "빠니보틀은 주말을 이용해 직장인들도 충분히 다녀올 수 있는 '금요일 퇴근 후 방콕 여행'의 가능성을 보여주며, 곽튜브와의 티격태격 케미를 통해 방콕의 매력을 소개합니다"
                                }
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
    ResponseEntity<SuccessResponse<TravelTemplateHighlightsResponse>> readTravelTemplateHighlights(
        @PathVariable("id") final Long id
    );

	@Operation(
		summary = "여행 템플릿 일정 조회",
		description = "여행 템플릿의 일정(장소 목록)을 조회합니다. day 파라미터로 특정 일차의 일정만 필터링할 수 있습니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "성공",
			content = @Content(
				schema = @Schema(implementation = TravelTemplateItineraryResponse.class),
				examples = @ExampleObject(
					name = "SUCCESS",
					value = """
						{
							"code": "2000",
							"message": "요청에 성공하였습니다.",
							"data": {
								"places": [
									{
										"id": 1,
										"day": 1,
										"sequence": 1,
										"travelerTip": "도쿄 타워는 저녁 시간대 방문하는 것이 좋습니다. 야경이 아름답습니다.",
										"estimatedDuration": 60,
										"place": {
											"placeId": "ChIJSc8jdZORQTURu6BMwxrKbGg",
											"latitude": 35.6585805,
											"longitude": 139.7454329,
											"name": "Tokyo Tower",
											"regularOpeningHours": "09:00~23:00"
										}
									},
									{
										"id": 2,
										"day": 1,
										"sequence": 2,
										"travelerTip": "메이지 신궁은 조용한 분위기로 유명합니다. 아침 일찍 방문하면 더욱 좋습니다.",
										"estimatedDuration": 90,
										"place": {
											"placeId": "ChIJN1t_tDeuEmsRUsoyG83frY4",
											"latitude": 35.6592606,
											"longitude": 139.7002586,
											"name": "Meiji Jingu",
											"regularOpeningHours": "06:00~18:00"
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
	ResponseEntity<SuccessResponse<TravelTemplateItineraryResponse>> readTravelTemplateItinerary(
		@Parameter(description = "여행 템플릿 ID", example = "1", required = true)
		@PathVariable("id") final Long id,
		@Parameter(description = "조회할 일차 (1부터 시작, 지정하지 않으면 모든 일정 조회)", example = "1", required = false)
		@RequestParam(value = "day", required = false)
		@Min(value = 1, message = "day는 항상 1 이상 입니다.") final Integer day
	);
}
