package com.yapp.ndgl.application.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    /**
     * 락 키를 생성하는 SpEL 표현식.
     */
    String key();

    /**
     * 락 획득 대기 시간 (단위: 초).
     */
    int timeout() default 3;
}
