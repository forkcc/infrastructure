package ck.infrastructure.web;

import ck.infrastructure.exception.ForbiddenException;
import ck.infrastructure.validator.EqualsValidator;
import ck.infrastructure.validator.NotBlankValidator;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 链路日志拦截器
 */
@Component
public class LogInterceptor  implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String url = request.getRequestURI();
        if(url.contains("swagger-ui") || url.contains("api-docs") || url.contains("webjars")) {
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }
        String requestId = request.getHeader("requestId");
        new NotBlankValidator(requestId , new ForbiddenException()).run();
        String traceId = request.getHeader("traceId");
        new NotBlankValidator(traceId , new ForbiddenException()).run();
        if(Objects.isNull(request.getSession().getAttribute("traceId"))) {
            request.getSession().setAttribute("traceId", traceId);
        }
        new EqualsValidator<>(traceId, request.getSession().getAttribute("traceId"), new ForbiddenException()).run();
        MDC.put("requestId", requestId);
        MDC.put("traceId", traceId);
        try{
            filterChain.doFilter(servletRequest,servletResponse);
        }finally {
            MDC.clear();
        }
    }
}
