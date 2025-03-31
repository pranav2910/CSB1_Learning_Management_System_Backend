package com.lms.config;

import com.lms.security.JwtAuthenticationFilter;
import com.lms.security.OAuth2LoginSuccessHandler;
import com.lms.security.RateLimitingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final OAuth2LoginSuccessHandler oauth2SuccessHandler;
    private final RateLimitingFilter rateLimitingFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF (not needed for stateless JWT)
            .csrf(AbstractHttpConfigurer::disable)
            
            // Configure authorization rules
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/v1/auth/**",          // Auth endpoints
                    "/oauth2/**",               // OAuth2 redirects
                    "/v3/api-docs/**",          // OpenAPI docs
                    "/swagger-ui/**",           // Swagger UI
                    "/swagger-ui.html",         // Swagger HTML
                    "/webjars/**"               // Swagger assets
                ).permitAll()
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/instructor/**").hasRole("INSTRUCTOR")
                .anyRequest().authenticated()
            )
            
            // Set session management to stateless
            .sessionManagement(sess -> sess
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // Configure authentication provider
            .authenticationProvider(authenticationProvider)
            
            // Add JWT filter before UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            
            // Add rate limiting filter before JWT filter
            .addFilterBefore(rateLimitingFilter, JwtAuthenticationFilter.class)
            
            // Configure OAuth2 login
            .oauth2Login(oauth -> oauth
                .successHandler(oauth2SuccessHandler)
            );
        
        return http.build();
    }
}