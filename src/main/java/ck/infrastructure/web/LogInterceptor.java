package ck.infrastructure.web;

import ck.infrastructure.exception.ForbiddenException;
import ck.infrastructure.validator.EqualsValidator;
import ck.infrastructure.validator.NotBlankValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

/**
 * 链路日志拦截器
 */
@Component
@RequiredArgsConstructor
public class LogInterceptor  implements Filter {
    private final ObjectMapper objectMapper;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String url = request.getRequestURI();
        if(url.contains("swagger-ui") || url.contains("api-docs") || url.contains("webjars") || url.contains("actuator")) {
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }
        try{
            String requestId = request.getHeader("requestId");
            new NotBlankValidator(requestId , new ForbiddenException(url)).run();
            String traceId = request.getHeader("traceId");
            new NotBlankValidator(traceId , new ForbiddenException(url)).run();
            if(Objects.isNull(request.getSession().getAttribute("traceId"))) {
                request.getSession().setAttribute("traceId", traceId);
            }
            new EqualsValidator<>(traceId, request.getSession().getAttribute("traceId"), new ForbiddenException(url)).run();
            MDC.put("requestId", requestId);
            MDC.put("traceId", traceId);
            filterChain.doFilter(servletRequest,servletResponse);
        } catch (ForbiddenException e) {
            objectMapper.writeValue(servletResponse.getOutputStream(), ApiResponse.forbidden(e.getMessage()));
        }finally {
            MDC.clear();
        }
    }
}
