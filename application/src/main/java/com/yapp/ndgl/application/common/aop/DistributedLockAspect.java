package com.yapp.ndgl.application.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.Ordered;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.yapp.ndgl.application.common.annotation.DistributedLock;
import com.yapp.ndgl.common.exception.CommonErrorCode;
import com.yapp.ndgl.common.exception.GlobalException;
import com.yapp.ndgl.lock.DistributedLockRepository;
import com.yapp.ndgl.lock.LockOptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class DistributedLockAspect {

    private static final ExpressionParser PARSER = new SpelExpressionParser();
    private static final ParameterNameDiscoverer NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    private final DistributedLockRepository lockRepository;

    @Around("@annotation(distributedLock)")
    public Object around(final ProceedingJoinPoint pjp, final DistributedLock distributedLock) throws Throwable {
        String key = resolveKey(distributedLock.key(), pjp);
        if (!StringUtils.hasText(key)) {
            log.error("분산락 키 표현식 평가 결과가 비어있습니다. expression={}", distributedLock.key());
            throw new GlobalException(CommonErrorCode.INVALID_LOCK_OPTIONS);
        }

        LockOptions options = lockRepository.createOptions(key, distributedLock.timeout());
        return lockRepository.withLock(options, pjp::proceed);
    }

    private String resolveKey(final String keyExpression, final ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        MethodBasedEvaluationContext context = new MethodBasedEvaluationContext(
            pjp.getTarget(), signature.getMethod(), pjp.getArgs(), NAME_DISCOVERER);
        return PARSER.parseExpression(keyExpression).getValue(context, String.class);
    }
}
