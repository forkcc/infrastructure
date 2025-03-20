package ck.infrastructure.web;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/swagger-ui.html", "/swagger-ui/**", "/*/api-docs", "/*/api-docs/**", "/webjars/**");
    }

    @Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        return HeaderHttpSessionIdResolver.xAuthToken();
    }

    public OperationCustomizer operationCustomizer() {
        return (operation, handlerMethod) -> {
            operation.addParametersItem(new HeaderParameter().name("traceId").description("追踪ID，每个用户都是唯一的"));
            operation.addParametersItem(new HeaderParameter().name("requestId").description("请求ID，每次请求都不一样"));
            operation.addParametersItem(new HeaderParameter().name("aesKey").description("请求体AES密码，需RSA公钥加密"));
            return operation;
        };
    }
    @Bean
    public GroupedOpenApi groupedOpenApi(){
        return GroupedOpenApi.builder()
                .group("默认")
                .packagesToScan("ck.infrastructure.safety")
                .build();
    }
}
