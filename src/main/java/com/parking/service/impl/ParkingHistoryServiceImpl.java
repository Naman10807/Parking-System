package com.parking.service.impl;

import com.parking.dto.response.ParkingHistoryResponse;
import com.parking.exception.InvalidDateRangeException;
import com.parking.mapper.ParkingHistoryMapper;
import com.parking.repository.ParkingRecordRepository;
import com.parking.service.ParkingHistoryService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParkingHistoryServiceImpl implements ParkingHistoryService {

    private final ParkingRecordRepository parkingRecordRepository;
    private final ParkingHistoryMapper parkingHistoryMapper;

    @Override
    public List<ParkingHistoryResponse> getAllHistory() {
        return parkingRecordRepository.findAllByOrderByEntryTimeDesc().stream()
                .map(parkingHistoryMapper::toResponse)
                .toList();
    }

    @Override
    public List<ParkingHistoryResponse> getHistoryByVehicleNumber(String vehicleNumber) {
        String normalizedVehicleNumber = normalizeVehicleNumber(vehicleNumber);
        return parkingRecordRepository
                .findByVehicle_VehicleNumberIgnoreCaseOrderByEntryTimeDesc(normalizedVehicleNumber)
                .stream()
                .map(parkingHistoryMapper::toResponse)
                .toList();
    }

    @Override
    public List<ParkingHistoryResponse> getHistoryBetweenDates(LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

        return parkingRecordRepository
                .findByEntryTimeBetweenOrderByEntryTimeDesc(startDateTime, endDateTime)
                .stream()
                .map(parkingHistoryMapper::toResponse)
                .toList();
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date are required");
        }
        if (startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException("Start date must not be after end date");
        }
    }

    private String normalizeVehicleNumber(String vehicleNumber) {
        if (vehicleNumber == null || vehicleNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle number is required");
        }
        return vehicleNumber.trim().toUpperCase();
    }
}
