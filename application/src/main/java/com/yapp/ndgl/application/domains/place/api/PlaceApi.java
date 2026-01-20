package com.yapp.ndgl.application.domains.place.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yapp.ndgl.application.domains.place.dto.PlaceDetailResponse;
import com.yapp.ndgl.common.response.ErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RequestMapping("/api/v1/places")
public interface PlaceApi {

	@Operation(
		summary = "장소 상세 조회",
		description = "placeId로 Google Places 상세 정보를 조회한다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "성공",
			content = @Content(
				schema = @Schema(implementation = PlaceDetailResponse.class),
				examples = @ExampleObject(
					name = "SUCCESS",
					value = """
						"place": {
						            "id": "ChIJPR5EUkCNGGARyhvN1EVWEc0",
						            "name": "규카츠 모토무라 신주쿠 본점",
						            "nationalPhoneNumber": "03-6457-6920",
						            "internationalPhoneNumber": "+81 3-6457-6920",
						            "formattedAddress": "일본 〒160-0021 Tokyo, Shinjuku City, Kabukichō, 1-chōme−２５−３ WaMall, B2F 西武新宿駅前ビル",
						            "location": {
						                "latitude": 35.6946268,
						                "longitude": 139.7016497
						            },
						            "userRatingCount": 7294,
						            "rating": 4.9,
						            "regularOpeningHours": [
						                "월요일: AM 11:00 ~ PM 10:00",
						                "화요일: AM 11:00 ~ PM 10:00",
						                "수요일: AM 11:00 ~ PM 10:00",
						                "목요일: AM 11:00 ~ PM 10:00",
						                "금요일: AM 11:00 ~ PM 10:00",
						                "토요일: AM 11:00 ~ PM 10:00",
						                "일요일: AM 11:00 ~ PM 10:00"
						            ],
						            "googleMapsUri": "https://maps.google.com/?cid=14776686710302251978&g_mp=CiVnb29nbGUubWFwcy5wbGFjZXMudjEuUGxhY2VzLkdldFBsYWNlEAIYBCAA",
						            "websiteUri": "https://www.gyukatsu-motomura.com/store/Shinjuku-Main_Store?utm_source=google&utm_medium=GBP_shinjuku-main",
						            "photos": [
						                {
						                    "name": "places/ChIJPR5EUkCNGGARyhvN1EVWEc0/photos/AcnlKN0lI8P4nBUdzcxtdgiIoqfHBok-JjiblvJSkRWRz0tY7qOx36ur_nTq5ge0PDrqVisUNGhJXu5eRLkAh47A46O1nl-Pz9u6uF6hA9qexlhjGjza2OU7RvFkKZO08B-NlGiHX48wwxJHYPJP7zjDlxHMW01RQ50tjQ-cElBSw5SWJV7WeFaV7ucL6hVm_C4AzpgmFMM7Zn6W5D04tyN7XY7t1sv4s3cWgvm_qP98z1D6iY8B9XIiFybFFkPG-B_AkZsjUy0mfdWVKOzs9m2XbTqj5DV0_BtxunE6NHDmMb7xaA",
						                    "widthPx": 4800,
						                    "heightPx": 3200,
						                    "photoUri": "https://lh3.googleusercontent.com/places/ANXAkqGbrXxA6_1tCvdNIB0BxCAB1ovsQH2KHOY5sBZEDZ8MI86hg5WIjXA0Ts_l3lKhJre-RpTvHVSMKusLWnwsDCW3HbqOAx6l3D0=s4800-w4800-h3200"
						                },
						                {
						                    "name": "places/ChIJPR5EUkCNGGARyhvN1EVWEc0/photos/AcnlKN2D8l-skqxcjQm0iphoERg3D7tN7q81wTnoMMNPEDmmuSuHGRMbIgl3Uyy8hKSjs-iUe43YRNaN6rKl6YptDCBZMqWK2YqIOAHq4XtkIN8SMPAttoqkeWIthhjc75m5fwGy-udn3LYhuKXnq4F5DO-ACyCK5mawPDvNO790ChnD2EUw2AbN-H_kkbaJ1Te2cGDlqbFfuyvuzgEwAI3m_FM4AxVJWQY4_-D_Lum8Wq5B-ncZV594ohj6ShMffszOeHF5TMBXQxB21qJ5anLlDC5XPaUBTSM2EDDLSJC4txK7Fg",
						                    "widthPx": 4800,
						                    "heightPx": 3199,
						                    "photoUri": "https://lh3.googleusercontent.com/places/ANXAkqEMPzXk9awDHM_DUUM4AIZhI517Gt5aXJbgunsMGjYHF0nfPn-GnmmXQkYD_b8oa05FSBFdjZ3F0a0hI8efpSbuGppnuIktE0I=s4800-w4800-h3199"
						                },
						                {
						                    "name": "places/ChIJPR5EUkCNGGARyhvN1EVWEc0/photos/AcnlKN1sIk17Tit1JA4BCE5Z9LZFpN0lyK7BaAjMEi9Etq-PA9XJWZd3SJktgVvWKqpJWHyQC_jqW4IvMGh8r7EnyQcEy-LHhzsFCvc2S1H6jkgHa0ojCm2I5elt-mogfisWUCDVdvVUlMNNC54eFXaIs0ZGUAUYdtR2uGxArEU7puHNMid1JVf7qJyZn0S4yh_L9d6JCMsXOPPCcgTAa3myJkJnU4hq9J2VVemuYLIT8zVCNrjvl5xeWoLmgx5dAjxogX99M9MVR4-LYDmP2BAmfQNR7MaiawQ2Pcy3a2ppL104Aj_AQh3DyfcD46fLDl3ut2DUhZOcpprRovEINQKwouYOWqDXJ84_qTn7qy_Xh8kMbrXDghgvX11eVqB9g8MpbQq40y_j3t_bvbCyYdlqjwxdDLwkfzh5NegrBKihc9366GBRygUoosqU-Mu2ig",
						                    "widthPx": 4000,
						                    "heightPx": 2252,
						                    "photoUri": "https://lh3.googleusercontent.com/place-photos/AEkURDwS4nC4h3f_NNJlzpdsONOAYA3V8E81f3hZiP2G2KpjwgdoMAqU5-rgqoAu2N87sEVlZHOhEk-h6w1vUnj5sSBYl7JitGuaJCn2iMoBw-eTphrA9MUP2kkLNpc_TV-cAoqaX9s3mXOg-aDJIK7Y-THj=s4800-w4000-h2252"
						                },
						                {
						                    "name": "places/ChIJPR5EUkCNGGARyhvN1EVWEc0/photos/AcnlKN0P7XckYydK1Lgtb5nm5pUWSN5p5_2kMzj21Ape-mqfCa65BpyL969I53TK4ZMeEfN92MlrVvyYG9gsv3zv4kIwMTejwvNMgVqI1053SQ-JX87wY5h5c4i-LOZ7EbiizqeUD89NCKf5VZchSvJLm_2pbdc2gAZBZlAtHy_9-KonYLHL4MQ2URCOf-vK7YOH7czZ4N55jCx_LERnWOKCP9YAzl-Kno4_2I_q4QlChjYr2rS-T6SFIdwqOTWcWeqL2hBnawU_nJyNQc-CIg89ElsnchiDdBRYR7pi9GidPdyHoEBSXggu3-AF0xOqrhVipgeIs8A6mDYmDLdGWznuoZV1qrL2M67aPIkrnrl_MHHEqEmzQGlcL3-amNfxtRwbFI1GBYcs_DfNf-mavDoicD6EKRfR3yiwCPSpDVUpZdXGJv2t",
						                    "widthPx": 2048,
						                    "heightPx": 2048,
						                    "photoUri": "https://lh3.googleusercontent.com/place-photos/AEkURDy1Cnf9q3WReJC6ENTjje5HlfMMBMLJ9c833nfozwS1CgriTRQlOWOnbetukGXDCUwKHEu6xeK79CHxsZe75jlFJIoz6pViokXK12NZojqQZnYcyvgUAhXHqEmiQQ3bx9Ohsf9gCdu-nSRrJ-c=s4800-w2048-h2048"
						                }
						            ]
						        }
						"""
				)
			)
		),
		@ApiResponse(
			responseCode = "400",
			description = "잘못된 요청",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class),
				examples = {
					@ExampleObject(
						name = "MISSING_REQUEST_PARAMETER",
						value = """
							{
							  "code": "COMM-01-006",
							  "message": "필수 요청 파라미터가 존재하지 않습니다",
							  "errors": []
							}
							"""
					),
					@ExampleObject(
						name = "INVALID_PLACE_ID",
						value = """
							{
							  "code": "GMAP_PLACE-07-003",
							  "message": "유효하지 않은 Place ID 입니다",
							  "errors": []
							}
							"""
					),
					@ExampleObject(
						name = "INVALID_PHOTO_NAME",
						value = """
							{
							  "code": "GMAP_PLACE-07-005",
							  "message": "유효하지 않은 Photo Name 입니다",
							  "errors": []
							}
							"""
					)
				}
			)
		),
		@ApiResponse(
			responseCode = "504",
			description = "외부 API 호출 실패 또는 타임아웃",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class),
				examples = {
					@ExampleObject(
						name = "API_CALL_FAILED",
						value = """
							{
							  "code": "GMAP_PLACE-07-001",
							  "message": "Google Maps Places API 호출에 실패했습니다",
							  "errors": []
							}
							"""
					),
					@ExampleObject(
						name = "API_TIMEOUT",
						value = """
							{
							  "code": "GMAP_PLACE-07-002",
							  "message": "Google Maps Places API 응답 시간이 초과되었습니다",
							  "errors": []
							}
							"""
					)
				}
			)
		),
		@ApiResponse(
			responseCode = "500",
			description = "서버 내부 오류",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class),
				examples = {
					@ExampleObject(
						name = "INTERNAL_SERVER_ERROR",
						value = """
							{
							  "code": "COMM-08-001",
							  "message": "서버 내부 오류가 발생했습니다",
							  "errors": []
							}
							"""
					),
					@ExampleObject(
						name = "RESPONSE_PARSE_FAILED",
						value = """
							{
							  "code": "GMAP_PLACE-07-004",
							  "message": "Google Maps Places API 응답 파싱에 실패했습니다",
							  "errors": []
							}
							"""
					)
				}
			)
		)
	})
	@GetMapping("/detail")
	ResponseEntity<?> readPlaceDetail(
		@Parameter(description = "Google Places 장소 ID", example = "ChIJSc8jdZORQTURu6BMwxrKbGg", required = true)
		@RequestParam("placeId") final String placeId
	);
}
