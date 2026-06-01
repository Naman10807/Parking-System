package com.parking.dto.request;

import com.parking.entity.SlotStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSlotRequest {

    @NotBlank(message = "Slot number is required")
    @Size(max = 50, message = "Slot number must not exceed 50 characters")
    private String slotNumber;

    @NotBlank(message = "Vehicle type is required")
    @Size(max = 50, message = "Vehicle type must not exceed 50 characters")
    private String vehicleType;

    @NotNull(message = "Status is required")
    private SlotStatus status;
}
