package ck.infrastructure.web;

import ck.infrastructure.exception.ForbiddenException;
import ck.infrastructure.validator.NotBlankValidator;
import ck.infrastructure.validator.NotEqualsValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

/**
 * 链路日志拦截器
 */
public class LogInterceptor  implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response,@NonNull  Object handler) throws Exception {
        String requestId = request.getHeader("requestId");
        new NotBlankValidator(requestId , new ForbiddenException()).run();
        String traceId = request.getHeader("traceId");
        new NotBlankValidator(traceId , new ForbiddenException()).run();
        new NotEqualsValidator<>(traceId, request.getSession().getAttribute("traceId"), new ForbiddenException()).run();
        MDC.put("requestId", requestId);
        MDC.put("traceId", traceId);
        if(Objects.isNull(request.getSession().getAttribute("traceId"))) {
            request.getSession().setAttribute("traceId", traceId);
        }
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull Object handler, Exception ex) throws Exception {
        MDC.clear();
    }
}
