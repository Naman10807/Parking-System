package com.parking.dto.response;

import com.parking.entity.SlotStatus;
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
public class ParkingSlotResponse {

    private Long id;
    private String slotNumber;
    private String vehicleType;
    private SlotStatus status;
}
