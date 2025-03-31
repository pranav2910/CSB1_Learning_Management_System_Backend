package com.lms.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI lmsOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("LMS API")
                .description("Udemy-like Learning Management System")
                .version("1.0")
                .contact(new Contact()
                    .name("Your Name")
                    .email("contact@lms.com"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://lms.com/license")));
    }
}