package ck.infrastructure.web;

import ck.infrastructure.notify.INotifyService;
import ck.infrastructure.notify.impl.NotifyServiceImpl;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.web.context.support.ServletRequestHandledEvent;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Optional;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        return new HeaderHttpSessionIdResolver("traceId");
    }

    public OperationCustomizer operationCustomizer() {
        return (operation, handlerMethod) -> {
            operation.addParametersItem(new HeaderParameter().name("traceId").description("追踪ID，每个用户都是唯一的").required(true));
            operation.addParametersItem(new HeaderParameter().name("requestId").description("请求ID，每次请求都不一样").required(true));
            return operation;
        };
    }
    @Bean
    public GroupedOpenApi groupedOpenApi(){
        return GroupedOpenApi.builder()
                .group("默认")
                .addOperationCustomizer(operationCustomizer())
                .build();
    }

    @Bean
    public ApplicationListener<ServletRequestHandledEvent> requestHandledEventApplicationListener(INotifyService notifyService){
        return event -> {
            if(event.getProcessingTimeMillis() > 300){
                notifyService.warn("{} {} {} {} {}ms",
                        event.getMethod(),
                        event.getClientAddress(), event.getRequestUrl(), event.getStatusCode(), event.getProcessingTimeMillis());
                notifyService.callTime(event.getProcessingTimeMillis(), Optional.ofNullable(MDC.get("requestId")).orElse(""));
            }else{
                notifyService.info("{} {} {} {} {}ms",
                        event.getMethod(),
                        event.getClientAddress(), event.getRequestUrl(), event.getStatusCode(), event.getProcessingTimeMillis());
            }
        };
    }
}
