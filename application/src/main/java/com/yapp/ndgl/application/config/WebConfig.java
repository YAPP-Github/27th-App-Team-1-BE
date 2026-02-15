package com.yapp.ndgl.application.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.yapp.ndgl.application.domains.auth.filter.CurrentUuidArgumentResolver;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  private final CurrentUuidArgumentResolver currentUuidArgumentResolver;

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(currentUuidArgumentResolver);
  }

  // @Override
  // public void addCorsMappings(CorsRegistry registry) {
  //   registry.addMapping("/**")
  //       .allowedOriginPatterns("*")
  //       .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
  //       .allowedHeaders("*")
  //       .allowCredentials(true);
  // }
}
