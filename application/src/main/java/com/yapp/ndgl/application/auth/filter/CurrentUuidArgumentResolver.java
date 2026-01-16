package com.yapp.ndgl.application.auth.filter;

import com.yapp.ndgl.application.auth.annotation.CurrentUuid;
import com.yapp.ndgl.common.exception.CommonErrorCode;
import com.yapp.ndgl.common.exception.GlobalException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CurrentUuidArgumentResolver implements HandlerMethodArgumentResolver {

  private static final String UUID_ATTRIBUTE = "uuid";

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(CurrentUuid.class) &&
        parameter.getParameterType().equals(String.class);
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory) {
    HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
    if (request != null) {
      String uuid = (String) request.getAttribute(UUID_ATTRIBUTE);
      if (uuid == null) {
        throw new GlobalException(CommonErrorCode.UNAUTHORIZED);
      }
      return uuid;
    }
    throw new GlobalException(CommonErrorCode.UNAUTHORIZED);
  }
}
