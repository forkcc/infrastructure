package ck.infrastructure;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@OpenAPIDefinition(info = @Info(description = "所有的请求体必须AES加密，/aes/key/encrypt接口可以用来测试加密"))
@SpringBootApplication
public class InfrastructureRunner {
    public static void main(String[] args) {
        SpringApplication.run(InfrastructureRunner.class, args);
    }
}
