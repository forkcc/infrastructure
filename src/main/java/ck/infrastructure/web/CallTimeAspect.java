package ck.infrastructure.web;

import ck.infrastructure.notify.INotifyService;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * 用于计算接口访问时间，对慢接口预警
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CallTimeAspect {
    private final INotifyService  notifyService;
    private final HttpServletRequest request;
    private final ObjectMapper objectMapper;
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMappingMethods() {}


    @Around("postMappingMethods()")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable{
        long start = System.currentTimeMillis();
        Object result = null;
        try{
            result =  joinPoint.proceed(joinPoint.getArgs());
            return result;
        }finally {
            long end = System.currentTimeMillis();
            //如果接口运行时间超过300ms就预警
            if(end - start > 300){
                notifyService.callTime(end-start, MDC.get("requestId"));
            }
        }
    }
}
