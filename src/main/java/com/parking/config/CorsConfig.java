package com.parking.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * CORS for Spring Security. {@link WebConfig} MVC mappings alone do not run before
 * the security filter chain, so preflight OPTIONS must be handled here.
 */
@Configuration
@RequiredArgsConstructor
public class CorsConfig {

    private final CorsProperties corsProperties;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(corsProperties.getAllowedOriginList());
        configuration.setAllowedMethods(Arrays.asList(corsProperties.getAllowedMethodArray()));
        configuration.setAllowedHeaders(resolveAllowedHeaders());
        configuration.setAllowCredentials(corsProperties.isAllowCredentials());
        configuration.setMaxAge(corsProperties.getMaxAge());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }

    private List<String> resolveAllowedHeaders() {
        List<String> headers = new ArrayList<>(corsProperties.getAllowedHeaderList());
        if (!headers.contains("Authorization")) {
            headers.add("Authorization");
        }
        return headers;
    }
}
