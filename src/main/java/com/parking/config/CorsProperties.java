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

    public List<String> getAllowedOriginList() {
        return Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(origin -> !origin.isEmpty())
                .toList();
    }

    public String[] getAllowedMethodArray() {
        return Arrays.stream(allowedMethods.split(","))
                .map(String::trim)
                .toArray(String[]::new);
    }
}
