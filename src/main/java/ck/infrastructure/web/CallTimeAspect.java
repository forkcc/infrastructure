package ck.infrastructure.web;

import ck.infrastructure.notify.INotifyService;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CallTimeAspect {
    private final INotifyService  notifyService;
    private final HttpServletRequest request;
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMappingMethods() {}


    @Around("postMappingMethods()")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable{
        long start = System.currentTimeMillis();
        try{
            return joinPoint.proceed(joinPoint.getArgs());
        }finally {
            long end = System.currentTimeMillis();
            //如果接口运行时间超过300ms就预警
            if(end - start > 300){
                notifyService.callTime(end-start, request);
            }
        }
    }
}
