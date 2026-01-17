package com.yapp.ndgl.application.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;

/**
 * Facade 계층을 나타내는 스테레오타입 어노테이션.
 * 
 * Controller와 Service 사이의 중간 계층으로,
 * 여러 Service를 조합하여 Controller에 필요한 기능을 제공합니다.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Facade {

}
