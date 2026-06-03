package com.parking.controller;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthResponse {

    private String status;
    private String message;
    private String application;
    private LocalDateTime timestamp;
}
