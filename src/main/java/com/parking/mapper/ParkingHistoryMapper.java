package com.parking.mapper;

import com.parking.dto.response.ParkingHistoryResponse;
import com.parking.entity.ParkingRecord;
import org.springframework.stereotype.Component;

@Component
public class ParkingHistoryMapper {

    public ParkingHistoryResponse toResponse(ParkingRecord record) {
        return ParkingHistoryResponse.builder()
                .recordId(record.getId())
                .vehicleNumber(record.getVehicle().getVehicleNumber())
                .ownerName(record.getVehicle().getOwnerName())
                .vehicleType(record.getVehicle().getVehicleType())
                .slotNumber(record.getParkingSlot().getSlotNumber())
                .entryTime(record.getEntryTime())
                .exitTime(record.getExitTime())
                .parkingFee(record.getParkingFee())
                .build();
    }
}
