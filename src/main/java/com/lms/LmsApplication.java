package com.lms;

import com.lms.config.SwaggerConfig;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableMongoAuditing  // Enables automatic createdAt/updatedAt timestamps
@EnableAsync          // Enables async methods (@Async)
@EnableScheduling     // Enables scheduled tasks (@Scheduled)
@OpenAPIDefinition(info = @Info(
        title = "Learning Management System API",
        version = "1.0",
        description = "Udemy-like platform APIs"
))
@Import(SwaggerConfig.class)  // Optional: Only if you have custom Swagger config
public class LmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(LmsApplication.class, args);
    }
}