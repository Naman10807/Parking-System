package com.parking.dto.response;

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
public class VehicleEntryResponse {

    private String vehicleNumber;
    private String slotNumber;
    private LocalDateTime entryTime;
    private String message;
}
