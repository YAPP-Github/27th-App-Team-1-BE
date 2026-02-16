package com.yapp.ndgl.application.domains.travel.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.yapp.ndgl.application.domains.auth.annotation.CurrentUuid;
import com.yapp.ndgl.application.domains.travel.controller.dto.SaveTravelTemplateRequest;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateHighlightsResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateItineraryResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplatePopularResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateRecommendationResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateSearchResponse;
import com.yapp.ndgl.common.response.ErrorResponse;
import com.yapp.ndgl.common.response.SliceResponse;
import com.yapp.ndgl.common.response.SuccessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@Tag(name = "Travel Template", description = "여행 템플릿 관련 API")
public interface TravelTemplateApi {

	@Operation(
		summary = "여행 템플릿 저장",
		description = "여행 템플릿을 저장합니다. travel_program_type이 YOUTUBE인 경우 link 필드에 YouTube 영상 URL을 입력하면 영상 제목, 썸네일, 채널명, 채널 프로필 이미지를 자동으로 추출합니다."
	)
	@io.swagger.v3.oas.annotations.parameters.RequestBody(
		content = @Content(
			examples = @ExampleObject(
				name = "YOUTUBE 타입 요청 예시",
				value = """
					{
					    "summary": "방콕 3박 4일 여행",
					    "budget_per_person": 1200000,
					    "continent": "아시아",
					    "country": "TH",
					    "city": "방콕",
					    "travel_program_type": "YOUTUBE",
					    "link": "https://www.youtube.com/watch?v=abc123",
					    "itinerary": [
					        {
					            "day": 1,
					            "activities": [
					                {
					                    "sequence": 1,
					                    "place_name": "왓 아룬",
					                    "estimated_time": 60
					                }
					            ]
					        }
					    ]
					}
					"""
			)
		)
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "성공",
			content = @Content(
				examples = @ExampleObject(
					name = "SUCCESS",
					value = """
						{
						    "code": "2000",
						    "message": "요청에 성공하였습니다.",
						    "data": {
						        "id": 1
						    }
						}
						"""
				)
			)
		)
	})
	ResponseEntity<SuccessResponse<Map<String, Long>>> saveTravelTemplate(
		@RequestBody @Valid final SaveTravelTemplateRequest request
	);

	@Operation(
		summary = "여행 템플릿 영상 정보 조회",
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
						    "travelId": 1,
						    "country": "오스트리아",
						    "city": "빈",
						    "budgetPerPerson": 300000,
						    "nights": 0,
						    "days": 1,
						    "youtube": {
						      "title": "title",
						      "name": "뜬뜬 DdeunDdeun",
						      "profileImage": "profile Image",
						      "thumbnail": "thumbnail",
						      "link": "youtube link",
						      "summary": "오스트리아 빈에서 펼쳐지는 무계획 P들의 좌충우돌 여행기. 슈테판 대성당의 웅장함에 감탄하고, 예약 전쟁으로 유명한 슈니첼 맛집 피그뮐러를 방문했으나 실패한 뒤 프라터 놀이공원으로 향하는 하루 일정을 담고 있다."
						    },
						    "program": {
						      "title": "title",
						      "name": "뜬뜬 DdeunDdeun",
						      "profileImage": null,
						      "thumbnail": null,
						      "link": null,
						      "summary": "오스트리아 빈에서 펼쳐지는 무계획 P들의 좌충우돌 여행기. 슈테판 대성당의 웅장함에 감탄하고, 예약 전쟁으로 유명한 슈니첼 맛집 피그뮐러를 방문했으나 실패한 뒤 프라터 놀이공원으로 향하는 하루 일정을 담고 있다."
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
						    "itineraries": [
						      {
						        "id": 1,
						        "day": 1,
						        "sequence": 1,
						        "distanceKm": 0,
						        "transportation": [],
						        "travelerTip": "호텔 조식이 훌륭하니 꼭 챙겨 드세요.",
						        "travelerTips": [
						          "호텔 조식이 훌륭하니 꼭 챙겨 드세요.",
						          "커넥팅 룸이 있어 가족이나 단체 여행객에게 편리해요."
						        ],
						        "planB": [
						          {
						            "name": "Hotel Sacher Wien",
						            "feature": "비엔나의 상징적인 럭셔리 호텔"
						          }
						        ],
						        "estimatedDuration": 0,
						        "place": {
						          "googlePlaceId": "ChIJlVeR-3YHbUcRL4Fe775_f3U",
						          "thumbnail": "https://lh3.googleusercontent.com/places/ANXAkqExB2iP_wSPBl9-XfjUl56j9L41kbmm6nv9shbfG6S9watM62yWifv-z7aEWdl_JLIa9xLUQgf9Q3VAljTtLh6lEiXK-5FuyAE=s4800-w1000-h563",
						          "latitude": 48.2017525,
						          "longitude": 16.3792027,
						          "name": "인터콘티넨탈 빈",
						          "regularOpeningHours": null,
						          "googleMapsUri": "https://maps.google.com/?cid=8466626282516283695&g_mp=CiVnb29nbGUubWFwcy5wbGFjZXMudjEuUGxhY2VzLkdldFBsYWNlEAIYBCAA",
						          "category": "ACCOMMODATION"
						        }
						      },
						      {
						        "id": 2,
						        "day": 1,
						        "sequence": 2,
						        "distanceKm": 1.2,
						        "transportation": [
						          {
						            "mode": "WALKING",
						            "timeMin": 15
						          }
						        ],
						        "travelerTip": "성당 내부는 무료 입장이 가능해요.",
						        "travelerTips": [
						          "성당 내부는 무료 입장이 가능해요.",
						          "북탑 전망대에 올라가면 모자이크 지붕을 가까이서 볼 수 있어요.",
						          "미사 시간에는 조용히 관람해야 해요."
						        ],
						        "planB": [
						          {
						            "name": "Karlskirche",
						            "feature": "바로크 양식의 걸작으로 꼽히는 성당"
						          }
						        ],
						        "estimatedDuration": 60,
						        "place": {
						          "googlePlaceId": "ChIJz-w_Ip8HbUcRWTHc-vNXlxc",
						          "thumbnail": "https://lh3.googleusercontent.com/place-photos/AL8-SNE69Dgq3auRVX7tt9GQuMltF-NqNXHhY_NZcNDq8bUpiU858i94ZqtsYWVuRnzzRcFhGhZ_mx-xqFzU8KY8UPpEC9b2F_hwZWVNp4HKxjB4M20YMLbr68BwKIL44Pv24CdbrTsL-y2b4R2UaiuwfRC7RQ=s4800-w4800-h3600",
						          "latitude": 48.208411399999996,
						          "longitude": 16.3734707,
						          "name": "슈테판 대성당",
						          "regularOpeningHours": null,
						          "googleMapsUri": "https://maps.google.com/?cid=1699924089753055577&g_mp=CiVnb29nbGUubWFwcy5wbGFjZXMudjEuUGxhY2VzLkdldFBsYWNlEAIYBCAA",
						          "category": "ATTRACTION"
						        }
						      },
						      {
						        "id": 3,
						        "day": 1,
						        "sequence": 3,
						        "distanceKm": 0.1,
						        "transportation": [
						          {
						            "mode": "WALKING",
						            "timeMin": 3
						          }
						        ],
						        "travelerTip": "오스트리아 국민 웨하스 마너를 종류별로 살 수 있어요.",
						        "travelerTips": [
						          "오스트리아 국민 웨하스 마너를 종류별로 살 수 있어요.",
						          "귀여운 마너 모자는 기념품으로 딱이에요."
						        ],
						        "planB": [
						          {
						            "name": "Gerstner K. u. K. Hofzuckerbäcker",
						            "feature": "황실에 납품하던 유서 깊은 디저트 가게"
						          }
						        ],
						        "estimatedDuration": 30,
						        "place": {
						          "googlePlaceId": "ChIJAzabap8HbUcRWK_H_Ri2b6A",
						          "thumbnail": "https://lh3.googleusercontent.com/place-photos/AL8-SNGicaGdXly1Yp4VKC4fE32hMstjrPo1X3d96Sdf0-hxN_rN1njcB_rm-r-36yMkXBLn84XhSAmobaG16BGFiYC8233PqYeokbcLV7CSzfvdGtXIxNmZLlW_BR5TCCwEkEaaobAy5SI_K3FI=s4800-w3024-h4032",
						          "latitude": 48.209141699999996,
						          "longitude": 16.373043199999998,
						          "name": "Manner",
						          "regularOpeningHours": "AM 10:00 ~ PM 9:00",
						          "googleMapsUri": "https://maps.google.com/?cid=11560658986936545112&g_mp=CiVnb29nbGUubWFwcy5wbGFjZXMudjEuUGxhY2VzLkdldFBsYWNlEAIYBCAA",
						          "category": "RESTAURANT"
						        }
						      },
						      {
						        "id": 4,
						        "day": 1,
						        "sequence": 4,
						        "distanceKm": 0.3,
						        "transportation": [
						          {
						            "mode": "WALKING",
						            "timeMin": 5
						          }
						        ],
						        "travelerTip": "예약 없이는 입장이 거의 불가능하니 꼭 미리 예약하세요.",
						        "travelerTips": [
						          "예약 없이는 입장이 거의 불가능하니 꼭 미리 예약하세요.",
						          "예약 실패 시 근처 다른 슈니첼 가게들도 맛이 훌륭해요.",
						          "굴라쉬 소스에 케첩을 섞으면 돈가스 소스 맛이 나요."
						        ],
						        "planB": [
						          {
						            "name": "Lugeck",
						            "feature": "피그뮐러에서 운영하는 모던한 분위기의 레스토랑"
						          },
						          {
						            "name": "Plachutta Wollzeile",
						            "feature": "타펠슈피츠(갈비탕 비슷한 요리)로 유명한 맛집"
						          }
						        ],
						        "estimatedDuration": 40,
						        "place": {
						          "googlePlaceId": "ChIJbU-Egp8HbUcRDZ_dZN6qW34",
						          "thumbnail": "https://lh3.googleusercontent.com/places/ANXAkqFWrnIjZ1wpFGc1nvnojmra68Q_MC1UVQFOD1MDoWbQ2SbX8sMOO8sPHYMccJcJwMRMkr4lr745gqqq3QofxkiDCEl3P7NVUFU=s4800-w2048-h1365",
						          "latitude": 48.209311899999996,
						          "longitude": 16.375577,
						          "name": "Figlmüller – Restaurant Bäckerstraße",
						          "regularOpeningHours": "AM 11:30 ~ PM 11:30",
						          "googleMapsUri": "https://maps.google.com/?cid=9105058943811297037&g_mp=CiVnb29nbGUubWFwcy5wbGFjZXMudjEuUGxhY2VzLkdldFBsYWNlEAIYBCAA",
						          "category": "RESTAURANT"
						        }
						      },
						      {
						        "id": 5,
						        "day": 1,
						        "sequence": 5,
						        "distanceKm": 2.5,
						        "transportation": [
						          {
						            "mode": "TAXI",
						            "timeMin": 15
						          }
						        ],
						        "travelerTip": "영화 '비포 선라이즈'에 나온 대관람차(Riesenrad)가 유명해요.",
						        "travelerTips": [
						          "영화 '비포 선라이즈'에 나온 대관람차(Riesenrad)가 유명해요.",
						          "입장료는 무료이고 놀이기구별로 요금을 내요."
						        ],
						        "planB": [
						          {
						            "name": "Stadtpark",
						            "feature": "요한 슈트라우스 동상이 있는 아름다운 시립 공원"
						          }
						        ],
						        "estimatedDuration": 90,
						        "place": {
						          "googlePlaceId": "ChIJY6GrTT8HbUcR9LGo6Zxp5_w",
						          "thumbnail": "https://lh3.googleusercontent.com/place-photos/AL8-SNEbzcEL2rREKF70ikd60JnNbB2lYRRF5mWvnYWgtck4zTlcDT5JYoGGxgumMlwzwnRMFRV0xnW_MfkGnqEMshP_RZkmezLiufsUTD3NkHRKD4Rr6MlGq0UCXdURCmbi1oyrykV3dR59_RRPNw=s4800-w1500-h1000",
						          "latitude": 48.2154648,
						          "longitude": 16.4001739,
						          "name": "프라터",
						          "regularOpeningHours": null,
						          "googleMapsUri": "https://maps.google.com/?cid=18223650539833962996&g_mp=CiVnb29nbGUubWFwcy5wbGFjZXMudjEuUGxhY2VzLkdldFBsYWNlEAIYBCAA",
						          "category": "RESTAURANT"
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
		@Parameter(description = "조회할 일차 (default = 1)", example = "1", required = false)
		@RequestParam(value = "day", required = false)
		@Min(value = 1, message = "day는 항상 1 이상 입니다.") final Integer day
	);

	@Operation(
		summary = "인기 여행지 목록 조회",
		description = "조회수 내림차순으로 여행 템플릿 목록을 조회합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "성공",
			content = @Content(
				schema = @Schema(implementation = TravelTemplatePopularResponse.class),
				examples = @ExampleObject(
					name = "SUCCESS",
					value = """
						{
						  "code": "2000",
						  "message": "요청에 성공하였습니다.",
						  "data": {
						    "content": [
						      {
						        "id": 1,
						        "title": "도쿄 3박 4일 완벽 여행 가이드",
						        "thumbnail": "https://example.com/thumbnail/tokyo.jpg",
						        "programName": "빠니보틀",
						        "programType": "YOUTUBE",
						        "traveler": "빠니보틀 Pani Bottle",
						        "country": "JP",
						        "city": "도쿄",
						        "nights": 3,
						        "days": 4
						      }
						    ],
						    "hasNext": true
						  }
						}
						"""
				)
			)
		)
	})
	ResponseEntity<SuccessResponse<SliceResponse<TravelTemplatePopularResponse>>> readPopularTravelTemplates(
		@Parameter(description = "프로그램 ID", example = "1", required = false)
		@RequestParam(value = "travelProgramId", required = false) final Long travelProgramId,
		@Parameter(description = "페이지 번호 (0부터 시작)", example = "0", required = false)
		@RequestParam(value = "page", defaultValue = "0") final int page,
		@Parameter(description = "페이지 사이즈", example = "20", required = false)
		@RequestParam(value = "size", defaultValue = "20") @Min(value = 1, message = "size는 1 이상 입니다.") final int size
	);

	@Operation(
		summary = "추천 여행지 목록 조회",
		description = "사용자 기반 추천 여행 템플릿 목록을 조회합니다.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "성공",
			content = @Content(
				schema = @Schema(implementation = TravelTemplateRecommendationResponse.class),
				examples = @ExampleObject(
					name = "SUCCESS",
					value = """
						{
						  "code": "2000",
						  "message": "요청에 성공하였습니다.",
						  "data": {
						    "content": [
						      {
						        "id": 1,
						        "title": "도쿄 3박 4일 완벽 여행 가이드",
						        "thumbnail": "https://example.com/thumbnail/tokyo.jpg",
						        "programName": "빠니보틀",
						        "programType": "YOUTUBE",
						        "traveler": "빠니보틀 Pani Bottle",
						        "country": "JP",
						        "city": "도쿄",
						        "nights": 3,
						        "days": 4
						      }
						    ],
						    "hasNext": true
						  }
						}
						"""
				)
			)
		)
	})
	ResponseEntity<SuccessResponse<SliceResponse<TravelTemplateRecommendationResponse>>> readRecommendedTravelTemplates(
		@CurrentUuid String uuid,
		@Parameter(description = "페이지 번호 (0부터 시작)", example = "0", required = false)
		@RequestParam(value = "page", defaultValue = "0") final int page,
		@Parameter(description = "페이지 사이즈", example = "20", required = false)
		@RequestParam(value = "size", defaultValue = "20") @Min(value = 1, message = "size는 1 이상 입니다.") final int size
	);

	@Operation(
		summary = "여행 템플릿 검색",
		description = "키워드로 여행 템플릿을 검색합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "성공",
			content = @Content(
				schema = @Schema(implementation = TravelTemplateSearchResponse.class),
				examples = @ExampleObject(
					name = "SUCCESS",
					value = """
						{
						  "code": "2000",
						  "message": "요청에 성공하였습니다.",
						  "data": {
						    "content": [
						      {
						        "id": 1,
						        "title": "도쿄 3박 4일 완벽 여행 가이드",
						        "thumbnail": "https://example.com/thumbnail/tokyo.jpg",
						        "programName": "빠니보틀",
						        "programType": "YOUTUBE",
						        "traveler": "빠니보틀 Pani Bottle",
						        "country": "JP",
						        "city": "도쿄",
						        "nights": 3,
						        "days": 4
						      }
						    ],
						    "hasNext": true
						  }
						}
						"""
				)
			)
		)
	})
	ResponseEntity<SuccessResponse<SliceResponse<TravelTemplateSearchResponse>>> searchTravelTemplates(
		@Parameter(description = "검색 키워드", example = "도쿄", required = true)
		@RequestParam(value = "keyword") final String keyword,
		@Parameter(description = "페이지 번호 (0부터 시작)", example = "0", required = false)
		@RequestParam(value = "page", defaultValue = "0") final int page,
		@Parameter(description = "페이지 사이즈", example = "20", required = false)
		@RequestParam(value = "size", defaultValue = "20") @Min(value = 1, message = "size는 1 이상 입니다.") final int size
	);
}
