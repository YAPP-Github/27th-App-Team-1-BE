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
					  "traveler" : "이름 또는 프로그램 (유튜브 링크라면 자동 매핑)",
					  "summary": "유재석, 이성민, 지석진, 양세찬이 오스트리아 빈을 여행하며 겪는 좌충우돌 에피소드를 담고 있다. 프라터 놀이공원에서의 즐거운 시간, 한식당 방문, 그리고 클림트의 키스를 보러 간 벨베데레 궁전 투어까지 알차게 즐기는 모습이 인상적이다.",
					  "budget_per_person": 350000,
					  "continent": "EUROPE",
					  "country": "AT",
					  "city": "빈",
					  "travel_program_type": "YOUTUBE",
					  "link": "https://www.youtube.com/watch?v=0RJpkvVJVUE",
					  "itinerary": [
					    {
					      "day": 1,
					      "activities": [
					        {
					          "sequence": 1,
					          "place_name": "Prater Wien",
					          "estimated_time": 120,
					          "distance_km": 0.0,
					          "transportation": [],
					          "traveler_tips": [
					            "영화 '비포 선라이즈' 촬영지인 관람차는 꼭 타봐야 해요.",
					            "범퍼카가 관람차보다 훨씬 재밌으니 강력 추천해요.",
					            "놀이공원 내에서 파는 핫도그와 간식을 즐겨보세요."
					          ],
					          "plan_b": [
					            {
					              "name": "Schweizerhaus"
					            }
					          ]
					        },
					        {
					          "sequence": 2,
					          "place_name": "Penny Markt",
					          "estimated_time": 40,
					          "distance_km": 1.5,
					          "transportation": [
					            {
					              "mode": "WALKING",
					              "time_min": 15
					            }
					          ],
					          "traveler_tips": [
					            "1유로도 안 하는 캔커피가 정말 맛있으니 꼭 드셔보세요.",
					            "귤이 한국보다 저렴하고 맛있어서 간식으로 딱이에요.",
					            "다양한 현지 과자와 초콜릿을 기념품으로 사기 좋아요."
					          ],
					          "plan_b": [
					            {
					              "name": "BILLA"
					            }
					          ]
					        }
					      ]
					    },
					    {
					      "day": 2,
					      "activities": [
					        {
					          "sequence": 1,
					          "place_name": "InterContinental Vienna",
					          "estimated_time": 60,
					          "distance_km": 0.0,
					          "transportation": [],
					          "traveler_tips": [
					            "멋진 저녁 식사를 위해 수트를 차려입고 나가보세요.",
					            "호텔 로비에서 사진을 찍으면 시상식 분위기를 낼 수 있어요."
					          ],
					          "plan_b": [
					            {
					              "name": "Hotel Sacher Wien"
					            }
					          ]
					        },
					        {
					          "sequence": 2,
					          "place_name": "Restaurant Sura",
					          "estimated_time": 90,
					          "distance_km": 2.0,
					          "transportation": [
					            {
					              "mode": "TAXI",
					              "time_min": 10
					            }
					          ],
					          "traveler_tips": [
					            "김치찌개와 제육볶음은 한국의 맛 그대로라 정말 맛있어요.",
					            "예약 없이 가면 웨이팅이 있을 수 있으니 예약하는 게 좋아요.",
					            "격식 있게 차려입고 한식을 즐기는 것도 색다른 추억이 돼요."
					          ],
					          "plan_b": [
					            {
					              "name": "Yori"
					            }
					          ]
					        },
					        {
					          "sequence": 3,
					          "place_name": "Wien Hauptbahnhof",
					          "estimated_time": 40,
					          "distance_km": 3.0,
					          "transportation": [
					            {
					              "mode": "TAXI",
					              "time_min": 15
					            }
					          ],
					          "traveler_tips": [
					            "헝가리 부다페스트행 기차표는 미리 예매하는 게 안전해요.",
					            "1등석(RJX) 좌석을 예매하면 더 편안하게 이동할 수 있어요."
					          ],
					          "plan_b": [
					            {
					              "name": "Wien Westbahnhof"
					            }
					          ]
					        }
					      ]
					    },
					    {
					      "day": 3,
					      "activities": [
					        {
					          "sequence": 1,
					          "place_name": "InterContinental Vienna",
					          "estimated_time": 30,
					          "distance_km": 0.0,
					          "transportation": [],
					          "traveler_tips": [
					            "체크아웃 전 짐을 잘 챙겼는지 마지막으로 확인하세요."
					          ],
					          "plan_b": []
					        },
					        {
					          "sequence": 2,
					          "place_name": "Schloss Belvedere",
					          "estimated_time": 90,
					          "distance_km": 1.5,
					          "transportation": [
					            {
					              "mode": "TAXI",
					              "time_min": 10
					            }
					          ],
					          "traveler_tips": [
					            "클림트의 '키스' 원본을 볼 수 있는 상궁 티켓을 추천해요.",
					            "천장의 화려한 그림과 창밖으로 보이는 정원 뷰가 예술이에요.",
					            "오픈 시간에 맞춰가면 여유롭게 관람할 수 있어요."
					          ],
					          "plan_b": [
					            {
					              "name": "Botanischer Garten der Universität Wien"
					            }
					          ]
					        },
					        {
					          "sequence": 3,
					          "place_name": "Wien Hauptbahnhof",
					          "estimated_time": 30,
					          "distance_km": 1.0,
					          "transportation": [
					            {
					              "mode": "TAXI",
					              "time_min": 5
					            }
					          ],
					          "traveler_tips": [
					            "기차 시간보다 여유 있게 도착해서 플랫폼을 확인하세요.",
					            "역 내 마트에서 기차 여행 간식을 사는 것도 좋아요."
					          ],
					          "plan_b": []
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
						    "country": "AT",
						    "countryName": "오스트리아",
						    "city": "빈",
						    "budgetPerPerson": 350000,
						    "nights": 0,
						    "days": 1,
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
						        "id": 31,
						        "day": 1,
						        "sequence": 1,
						        "distanceKm": 0,
						        "transportation": [],
						        "memo": null,
						        "travelerTips": [
						          "김포공항에서 일본 항공 비즈니스석을 이용하면 쾌적하게 여행을 시작할 수 있어요."
						        ],
						        "startTime": null,
						        "estimatedDuration": 120,
						        "budget": null,
						        "place": {
						          "googlePlaceId": "ChIJF6qs-dCcfDURVMw5Ij-Qd6w",
						          "thumbnail": "https://lh3.googleusercontent.com/place-photos/AL8-SNHi7y_J3VIsTJZIOTITwK2s7SBN6ZJYwG0WowjcU9Jvz7WOwL1kBSAl-PTwGZHGSHlUxwZIDmXpjt3C-wKKEb5KXv3EmErU3dwuYa9YVqSaUxCHRxTLaXe6ycZDALgqs03P8rJsXtXY86H7iA=s4800-w4032-h3024",
						          "latitude": 37.5655383,
						          "longitude": 126.8013282,
						          "name": "김포국제공항",
						          "regularOpeningHours": null,
						          "googleMapsUri": "https://maps.google.com/?cid=12427560297583725652&g_mp=CiVnb29nbGUubWFwcy5wbGFjZXMudjEuUGxhY2VzLkdldFBsYWNlEAIYBCAA",
						          "category": "AIRPORT",
						          "nearbyPlaces": null
						        }
						      },
						      {
						        "id": 32,
						        "day": 1,
						        "sequence": 2,
						        "distanceKm": 20.5,
						        "transportation": [
						          {
						            "mode": "TRANSIT",
						            "timeMin": 40
						          }
						        ],
						        "memo": null,
						        "travelerTips": [
						          "조개 라면이 일품인데, 맑은 국물의 시오 라면을 추천해요."
						        ],
						        "startTime": null,
						        "estimatedDuration": 60,
						        "budget": null,
						        "place": {
						          "googlePlaceId": "ChIJSRvys-eLGGARzwqQ4I3g03o",
						          "thumbnail": "https://lh3.googleusercontent.com/place-photos/AL8-SNF9s7SrRb9RkBOX36EguX1Vhv6_zDSjR-GasZ-WGShIRrdIG4i-mdnprOlyDy35BeNlT_GzX-TKatc0R2Ws7KBB-KS1LZgm9-avSNEvhc-yIwNvzDtBZOe1tnNlm0MpDQOD66GLCBWuGXIzdZ5sfsHm=s4800-w2590-h3238",
						          "latitude": 35.6690038,
						          "longitude": 139.7643604,
						          "name": "무기토 올리브 긴자점",
						          "regularOpeningHours": "AM 11:00 ~ PM 9:30",
						          "googleMapsUri": "https://maps.google.com/?cid=8850664592675703503&g_mp=CiVnb29nbGUubWFwcy5wbGFjZXMudjEuUGxhY2VzLkdldFBsYWNlEAIYBCAA",
						          "category": "RESTAURANT",
						          "nearbyPlaces": [
						            {
						              "googlePlaceId": "ChIJF0WZ2-uLGGAR9kfXM51Wfxk",
						              "name": "이치란 신바시점",
						              "thumbnail": "https://lh3.googleusercontent.com/places/ANXAkqFCjm4P0NdhPtNjPcwdcDN6p0G4gaRfqLwYKe-B7nx_sSPO2wTebq5LL042vbPAofsc01VBxHhu512m0TUxlO37sFgfpsLVt5Q=s4800-w3024-h4032",
						              "category": "RESTAURANT",
						              "rating": 4.2,
						              "latitude": 35.6675081,
						              "longitude": 139.7560198
						            },
						            {
						              "googlePlaceId": "ChIJjykJZCCLGGARMx5vYqRsWYI",
						              "name": "카가리 긴자본점",
						              "thumbnail": "https://lh3.googleusercontent.com/places/ANXAkqGUiIAunadLapF_GJvbT2aFVi2WmciQhyXUeIQbRsEvJAlPYJQ7H9FajkaqQoo1ygiRFodXQc8AOnycm8mgEDatwtWbI-garDs=s4800-w3872-h2576",
						              "category": "RESTAURANT",
						              "rating": 4.2,
						              "latitude": 35.6712019,
						              "longitude": 139.76133149999998
						            },
						            {
						              "googlePlaceId": "ChIJ1WzWeC-LGGARKltXcP7uLqg",
						              "name": "츠지타 긴자",
						              "thumbnail": "https://lh3.googleusercontent.com/places/ANXAkqHWUKPQJMyWTGEeR0FY1bnqFeP9sb1_WCQvWqORCpkoP_aijT94WO-HhpDbd9d2OCsCusmqnQ3XOhu_3U6jyLRi0hIC2-xYJ8g=s4800-w816-h544",
						              "category": "RESTAURANT",
						              "rating": 4.7,
						              "latitude": 35.671432599999996,
						              "longitude": 139.7673806
						            }
						          ]
						        }
						      },
						      {
						        "id": 33,
						        "day": 1,
						        "sequence": 3,
						        "distanceKm": 0.5,
						        "transportation": [
						          {
						            "mode": "WALKING",
						            "timeMin": 7
						          }
						        ],
						        "memo": null,
						        "travelerTips": [
						          "초코빵과 독일 소시지 빵이 유명해요. 일본 베이커리는 수준이 아주 높아요."
						        ],
						        "startTime": null,
						        "estimatedDuration": 40,
						        "budget": null,
						        "place": {
						          "googlePlaceId": "ChIJE8S0lOSLGGAR84AdzQ71F3I",
						          "thumbnail": "https://lh3.googleusercontent.com/place-photos/AL8-SNH7mZlDB_cQwVJQRufuZi0eGxYEAAAEyGAzjRP_4L4RF1i1KDtENfIPHP8X9AubRN_B3NpJd1cUB6QdY0VRiKbae3rdFvtl3tHZmlS52ru8_x6MDTEO2q-H7OHfxh47ozoFJDovj6jp5q4mpw=s4800-w4032-h3024",
						          "latitude": 35.6755334,
						          "longitude": 139.76659519999998,
						          "name": "센트레 더 베이커리",
						          "regularOpeningHours": "AM 9:00 ~ PM 6:00",
						          "googleMapsUri": "https://maps.google.com/?cid=8221309088707739891&g_mp=CiVnb29nbGUubWFwcy5wbGFjZXMudjEuUGxhY2VzLkdldFBsYWNlEAIYBCAA",
						          "category": "CAFE",
						          "nearbyPlaces": [
						            {
						              "googlePlaceId": "ChIJ_9oVOZ-LGGARTMN4dW-AI-4",
						              "name": "팡 메종 긴자점",
						              "thumbnail": "https://lh3.googleusercontent.com/place-photos/AL8-SNFNUyXta3Jv8a9o-xTj7-MHnyVRFWTwf0YjtqQqAOMaJmGxVJHvQdWtN06Ek-OKjn0ohUtHd3zIFQXQGbQ9R1QpplXLCb6VKOTYDrH9-0etzkRUC_JtT1Ep9_1YedT8L8Vsfx26OQAdhsr4-7b0afLb=s4800-w2992-h2992",
						              "category": "CAFE",
						              "rating": 4.4,
						              "latitude": 35.6715607,
						              "longitude": 139.7705344
						            },
						            {
						              "googlePlaceId": "ChIJaz0IaOSLGGARec_RLv1YlbI",
						              "name": "키르훼봉 그랑 메종 긴자점",
						              "thumbnail": "https://lh3.googleusercontent.com/places/ANXAkqF39rgWT9kPXihB5bUi4wacQJquGn2Q5aJEEYv96y_acTM38OlFGi9IhEoUhvMj6-Igf4C-LiJW8u-bdw4o78Wg18_3sKK3p3Q=s4800-w542-h305",
						              "category": "RESTAURANT",
						              "rating": 4.2,
						              "latitude": 35.6740612,
						              "longitude": 139.767203
						            },
						            {
						              "googlePlaceId": "ChIJ6T8nDfqLGGAR2NB5iV6rCgw",
						              "name": "에쉬레 메종 뒤 부르",
						              "thumbnail": "https://lh3.googleusercontent.com/places/ANXAkqGRyksA-A_rQUb2En26j5R_gLnv3xUWdWTuR1OKLbYXfom9qyIOz4-LllGy7Pb0tKrrAQ62-JAMxG2F5-yaEexCtdHbsd0e96I=s4800-w1500-h1000",
						              "category": "RESTAURANT",
						              "rating": 4,
						              "latitude": 35.6785703,
						              "longitude": 139.7625325
						            }
						          ]
						        }
						      },
						      {
						        "id": 34,
						        "day": 1,
						        "sequence": 4,
						        "distanceKm": 1.2,
						        "transportation": [
						          {
						            "mode": "TRANSIT",
						            "timeMin": 15
						          }
						        ],
						        "memo": null,
						        "travelerTips": [
						          "가성비 좋은 4성급 호텔이에요. 짐을 옮겨주시면 소액의 팁을 드리는 것이 예의예요."
						        ],
						        "startTime": null,
						        "estimatedDuration": 60,
						        "budget": null,
						        "place": {
						          "googlePlaceId": "ChIJD7G_2_2LGGARcK8ZATyh2U8",
						          "thumbnail": "https://lh3.googleusercontent.com/places/ANXAkqEPt4G0XsQOpCH7L-vDIUysKg6X6Vo2HQVrY12Y4AOVcuIbsBlan1YxbJYgpXxoBK3kcEc9GdSNybNoH2fzwLU6AdNM6Rjz6rM=s4800-w2848-h1602",
						          "latitude": 35.6830406,
						          "longitude": 139.77112119999998,
						          "name": "호텔 류메이칸 도쿄",
						          "regularOpeningHours": null,
						          "googleMapsUri": "https://maps.google.com/?cid=5753807278031089520&g_mp=CiVnb29nbGUubWFwcy5wbGFjZXMudjEuUGxhY2VzLkdldFBsYWNlEAIYBCAA",
						          "category": "ACCOMMODATION",
						          "nearbyPlaces": [
						            {
						              "googlePlaceId": "ChIJq0s44BuMGGARvBtm78mmRRg",
						              "name": "팰리스 호텔 도쿄",
						              "thumbnail": "https://lh3.googleusercontent.com/places/ANXAkqFrKSTdIFcQgPC-19sG_cKgck63Uroj4v9dYkkrSubKkAvb5V2ksihoBYp3xM1mKtxHoViOH8bn3G0xjxX8opCP-Mwh2HoqepM=s4800-w4800-h3199",
						              "category": "ACCOMMODATION",
						              "rating": 4.5,
						              "latitude": 35.6845024,
						              "longitude": 139.76122949999998
						            },
						            {
						              "googlePlaceId": "ChIJ281VfFWJGGARYaR_hmZuo0I",
						              "name": "만다린 오리엔탈 도쿄",
						              "thumbnail": "https://lh3.googleusercontent.com/places/ANXAkqFI8ark9NPvar40UOBMs5e7lTlTqOAEzeZkqTlFOHoFbhwP4YVbuVBt5F5hjtmOCQ1Xpro6p8adVS2u84_KiabDK-sWRTHAgs0=s4800-w3840-h2160",
						              "category": "ACCOMMODATION",
						              "rating": 4.5,
						              "latitude": 35.6870475,
						              "longitude": 139.7730634
						            },
						            {
						              "googlePlaceId": "ChIJlaVM3eOLGGARxiqelk1qzC8",
						              "name": "KOKO HOTEL Ginza 1-chome",
						              "thumbnail": "https://lh3.googleusercontent.com/places/ANXAkqHnf2CnSLmb5rQ9iuWtdbznN7356i1DtRhYiNU5q1FBbmw3tp-P3Via-xZrAIfXDDjw40OoNG8OAKNm_qjM0tVnJbUwv4-h308=s4800-w4582-h3055",
						              "category": "ACCOMMODATION",
						              "rating": 3.8,
						              "latitude": 35.6741957,
						              "longitude": 139.76927139999998
						            }
						          ]
						        }
						      },
						      {
						        "id": 35,
						        "day": 1,
						        "sequence": 5,
						        "distanceKm": 2.4,
						        "transportation": [
						          {
						            "mode": "WALKING",
						            "timeMin": 30
						          }
						        ],
						        "memo": null,
						        "travelerTips": [
						          "캥거루 고기 스테이크라는 이색 메뉴를 시도해보세요. 생각보다 훨씬 맛있어요."
						        ],
						        "startTime": null,
						        "estimatedDuration": 90,
						        "budget": null,
						        "place": {
						          "googlePlaceId": "ChIJY1hD3rGNGGARLF5vOikFOlw",
						          "thumbnail": "https://lh3.googleusercontent.com/places/ANXAkqEE6ZnZW9oedkqF4SpGQtZr74E_lxVYooo8EVmth7AAKbLzLzFioQJ5fjq9S6OwxC0LnHEjEsO_SPzbJCFA54NPu696slwacQM=s4800-w664-h491",
						          "latitude": 35.6794052,
						          "longitude": 139.7372696,
						          "name": "벤자민 스테이크하우스 도쿄",
						          "regularOpeningHours": "AM 11:30 ~ PM 3:00, PM 5:00~11:00",
						          "googleMapsUri": "https://maps.google.com/?cid=6645629874770763308&g_mp=CiVnb29nbGUubWFwcy5wbGFjZXMudjEuUGxhY2VzLkdldFBsYWNlEAIYBCAA",
						          "category": "RESTAURANT",
						          "nearbyPlaces": [
						            {
						              "googlePlaceId": "ChIJAVoEeHmMGGARvoInBliuVns",
						              "name": "Anchor Point",
						              "thumbnail": "https://lh3.googleusercontent.com/places/ANXAkqFJts3Nv15NRLNlJbk2Eav2GA3mCd3AYCe6ObGlWz88G1mj_hncmxIC469ek3BhOK32iRlGRO3D9buWZOis_qqxg4bn2NU44g=s4800-w2000-h1331",
						              "category": "RESTAURANT",
						              "rating": 4.2,
						              "latitude": 35.679478599999996,
						              "longitude": 139.7397061
						            },
						            {
						              "googlePlaceId": "ChIJz8hGbH-MGGARBYaegg4n-9U",
						              "name": "고베 비프 카이세키 511",
						              "thumbnail": "https://lh3.googleusercontent.com/places/ANXAkqEZscjLl1VLqK_tYzjN4u1r3uSNGM6ZIl4vVgFAg9irvO8cDZBmP0HUQj-Lrsj83yHXZWUwedC5EbdGdg18MJkmA2uFFA6PXI4=s4800-w4800-h3200",
						              "category": "RESTAURANT",
						              "rating": 4.5,
						              "latitude": 35.6746902,
						              "longitude": 139.73529249999999
						            }
						          ]
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
