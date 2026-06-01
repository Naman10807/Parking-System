package com.parking.mapper;

import com.parking.dto.request.ParkingSlotRequest;
import com.parking.dto.response.ParkingSlotResponse;
import com.parking.entity.ParkingSlot;
import org.springframework.stereotype.Component;

@Component
public class ParkingSlotMapper {

    public ParkingSlot toEntity(ParkingSlotRequest request) {
        return ParkingSlot.builder()
                .slotNumber(request.getSlotNumber().trim())
                .vehicleType(request.getVehicleType().trim())
                .status(request.getStatus())
                .build();
    }

    public void updateEntity(ParkingSlot slot, ParkingSlotRequest request) {
        slot.setSlotNumber(request.getSlotNumber().trim());
        slot.setVehicleType(request.getVehicleType().trim());
        slot.setStatus(request.getStatus());
    }

    public ParkingSlotResponse toResponse(ParkingSlot slot) {
        return ParkingSlotResponse.builder()
                .id(slot.getId())
                .slotNumber(slot.getSlotNumber())
                .vehicleType(slot.getVehicleType())
                .status(slot.getStatus())
                .build();
    }
}
