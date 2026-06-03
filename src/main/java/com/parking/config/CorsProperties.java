package com.parking.config;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.cors")
@Getter
@Setter
public class CorsProperties {

    private String allowedOrigins = "http://localhost:5173";
    private String allowedMethods = "GET,POST,PUT,PATCH,DELETE,OPTIONS";
    private String allowedHeaders = "*";
    private boolean allowCredentials = true;
    private long maxAge = 3600;

    private static final List<String> DEFAULT_ORIGINS =
            List.of("http://localhost:5173", "http://127.0.0.1:5173");

    public List<String> getAllowedOriginList() {
        List<String> origins = Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(origin -> !origin.isEmpty())
                .toList();
        return origins.isEmpty() ? DEFAULT_ORIGINS : origins;
    }

    public String[] getAllowedMethodArray() {
        return Arrays.stream(allowedMethods.split(","))
                .map(String::trim)
                .toArray(String[]::new);
    }

    /**
     * Explicit headers when using credentials; wildcard "*" is not reliable with
     * {@code allowCredentials=true} for browser preflight.
     */
    public List<String> getAllowedHeaderList() {
        if ("*".equals(allowedHeaders.trim())) {
            return List.of(
                    "Authorization",
                    "Content-Type",
                    "Accept",
                    "Origin",
                    "X-Requested-With");
        }
        return Arrays.stream(allowedHeaders.split(","))
                .map(String::trim)
                .filter(header -> !header.isEmpty())
                .toList();
    }
}
