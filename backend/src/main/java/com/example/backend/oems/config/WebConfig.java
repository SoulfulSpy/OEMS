package com.example.backend.oems.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration for CORS and security headers.
 * Configures cross-origin resource sharing policies based on application properties.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    private final AppProperties appProperties;
    
    public WebConfig(AppProperties appProperties) {
        this.appProperties = appProperties;
    }
    
    /**
     * Configure CORS filter for cross-origin requests.
     * Uses application properties to define allowed origins, methods, and headers.
     * 
     * @return Configured CorsFilter bean
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // Parse allowed origins from comma-separated string
        List<String> origins = Arrays.asList(
            appProperties.getCors().getAllowedOrigins().split(",")
        );
        config.setAllowedOrigins(origins);
        
        // Parse allowed methods from comma-separated string
        List<String> methods = Arrays.asList(
            appProperties.getCors().getAllowedMethods().split(",")
        );
        config.setAllowedMethods(methods);
        
        // Configure allowed headers
        if ("*".equals(appProperties.getCors().getAllowedHeaders())) {
            config.setAllowedHeaders(List.of("*"));
        } else {
            List<String> headers = Arrays.asList(
                appProperties.getCors().getAllowedHeaders().split(",")
            );
            config.setAllowedHeaders(headers);
        }
        
        config.setAllowCredentials(appProperties.getCors().isAllowCredentials());
        config.setMaxAge(3600L); // Cache preflight response for 1 hour
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}