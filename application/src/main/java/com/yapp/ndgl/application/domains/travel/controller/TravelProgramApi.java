package com.yapp.ndgl.application.domains.travel.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.yapp.ndgl.application.domains.travel.controller.dto.TravelProgramResponse;
import com.yapp.ndgl.common.response.SuccessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Travel Program", description = "여행 프로그램 관련 API")
public interface TravelProgramApi {

    @Operation(
        summary = "여행 프로그램 목록 조회",
        description = "여행 프로그램 전체 목록을 조회합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(
                schema = @Schema(implementation = TravelProgramResponse.class),
                examples = @ExampleObject(
                    name = "SUCCESS",
                    value = """
                        {
                          "code": "2000",
                          "message": "요청에 성공하였습니다.",
                          "data": [
                            {
                              "id": 1,
                              "name": "빠니보틀",
                              "profileImage": "https://example.com/thumbnail/panibottle.jpg",
                              "type": "YOUTUBE"
                            }
                          ]
                        }
                        """
                )
            )
        )
    })
    ResponseEntity<SuccessResponse<List<TravelProgramResponse>>> readTravelPrograms();
}
