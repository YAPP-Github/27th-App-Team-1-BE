package com.yapp.ndgl.application.temp.controller;

import com.yapp.ndgl.application.auth.annotation.CurrentUuid;
import com.yapp.ndgl.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Test", description = "테스트 API")
@RestController
@RequestMapping("/api/v1/temp")
public class TempController {

  @Operation(
      summary = "JWT UUID 조회",
      description = "JWT 토큰에서 추출한 UUID를 반환합니다.",
      security = @SecurityRequirement(name = "bearerAuth")
  )
  @GetMapping("/uuid")
  public SuccessResponse<String> getUuid(@CurrentUuid String uuid) {
    return SuccessResponse.success(uuid);
  }

  @PostConstruct
  public void init() {
    log.info("TestController initialized and loaded as bean");
    System.out.println("TestController loaded");
  }
}
