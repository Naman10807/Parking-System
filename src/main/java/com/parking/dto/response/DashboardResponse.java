package com.parking.dto.response;

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
public class DashboardResponse {

    private long totalSlots;
    private long availableSlots;
    private long occupiedSlots;
    private long activeVehicles;
    private long totalVehiclesParked;
    private double totalRevenue;
}
