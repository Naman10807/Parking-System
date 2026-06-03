package com.parking.service.impl;

import com.parking.dto.response.DashboardResponse;
import com.parking.entity.SlotStatus;
import com.parking.repository.ParkingRecordRepository;
import com.parking.repository.ParkingSlotRepository;
import com.parking.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final ParkingSlotRepository parkingSlotRepository;
    private final ParkingRecordRepository parkingRecordRepository;

    @Override
    public DashboardResponse getDashboardSummary() {
        long totalSlots = parkingSlotRepository.count();
        long availableSlots = parkingSlotRepository.countByStatus(SlotStatus.AVAILABLE);
        long occupiedSlots = parkingSlotRepository.countByStatus(SlotStatus.OCCUPIED);
        long activeVehicles = parkingRecordRepository.countByExitTimeIsNull();
        long totalVehiclesParked = parkingRecordRepository.count();
        double totalRevenue = parkingRecordRepository.sumCompletedParkingFees();

        return DashboardResponse.builder()
                .totalSlots(totalSlots)
                .availableSlots(availableSlots)
                .occupiedSlots(occupiedSlots)
                .activeVehicles(activeVehicles)
                .totalVehiclesParked(totalVehiclesParked)
                .totalRevenue(totalRevenue)
                .build();
    }
}
