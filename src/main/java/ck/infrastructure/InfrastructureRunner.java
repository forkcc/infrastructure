package ck.infrastructure;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class InfrastructureRunner {
    public static void main(String[] args) {
        SpringApplication.run(InfrastructureRunner.class, args);
    }
}
