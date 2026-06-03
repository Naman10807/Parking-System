package com.parking.service.impl;

import com.parking.dto.request.VehicleEntryRequest;
import com.parking.dto.response.VehicleEntryResponse;
import com.parking.entity.ParkingRecord;
import com.parking.entity.ParkingSlot;
import com.parking.entity.SlotStatus;
import com.parking.entity.Vehicle;
import com.parking.exception.NoAvailableSlotException;
import com.parking.exception.VehicleAlreadyParkedException;
import com.parking.repository.ParkingRecordRepository;
import com.parking.repository.ParkingSlotRepository;
import com.parking.repository.VehicleRepository;
import com.parking.service.ParkingService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParkingServiceImpl implements ParkingService {

    private static final String ENTRY_SUCCESS_MESSAGE = "Vehicle entered successfully";

    private final ParkingSlotRepository parkingSlotRepository;
    private final VehicleRepository vehicleRepository;
    private final ParkingRecordRepository parkingRecordRepository;

    @Override
    @Transactional
    public VehicleEntryResponse registerVehicleEntry(VehicleEntryRequest request) {
        String vehicleNumber = request.getVehicleNumber().trim().toUpperCase();
        String vehicleType = request.getVehicleType().trim().toUpperCase();
        String ownerName = request.getOwnerName().trim();

        if (parkingRecordRepository.existsByVehicleVehicleNumberAndExitTimeIsNull(vehicleNumber)) {
            throw new VehicleAlreadyParkedException(
                    "Vehicle with number '" + vehicleNumber + "' is already parked");
        }

        ParkingSlot availableSlot = findAvailableSlot(vehicleType);

        Vehicle vehicle = vehicleRepository.findByVehicleNumber(vehicleNumber)
                .map(existing -> updateVehicle(existing, ownerName, vehicleType))
                .orElseGet(() -> createVehicle(vehicleNumber, ownerName, vehicleType));

        LocalDateTime entryTime = LocalDateTime.now();

        ParkingRecord parkingRecord = ParkingRecord.builder()
                .vehicle(vehicle)
                .parkingSlot(availableSlot)
                .entryTime(entryTime)
                .build();
        parkingRecordRepository.save(parkingRecord);

        availableSlot.setStatus(SlotStatus.OCCUPIED);
        parkingSlotRepository.save(availableSlot);

        return VehicleEntryResponse.builder()
                .vehicleNumber(vehicle.getVehicleNumber())
                .slotNumber(availableSlot.getSlotNumber())
                .entryTime(entryTime)
                .message(ENTRY_SUCCESS_MESSAGE)
                .build();
    }

    private ParkingSlot findAvailableSlot(String vehicleType) {
        return parkingSlotRepository
                .findFirstByStatusAndVehicleTypeIgnoreCaseOrderByIdAsc(SlotStatus.AVAILABLE, vehicleType)
                .or(() -> parkingSlotRepository.findFirstByStatusOrderByIdAsc(SlotStatus.AVAILABLE))
                .orElseThrow(() -> new NoAvailableSlotException("No available parking slots at the moment"));
    }

    private Vehicle createVehicle(String vehicleNumber, String ownerName, String vehicleType) {
        Vehicle vehicle = Vehicle.builder()
                .vehicleNumber(vehicleNumber)
                .ownerName(ownerName)
                .vehicleType(vehicleType)
                .build();
        return vehicleRepository.save(vehicle);
    }

    private Vehicle updateVehicle(Vehicle vehicle, String ownerName, String vehicleType) {
        vehicle.setOwnerName(ownerName);
        vehicle.setVehicleType(vehicleType);
        return vehicleRepository.save(vehicle);
    }
}
