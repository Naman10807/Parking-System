package com.parking.service;

import com.parking.dto.response.ParkingHistoryResponse;
import java.time.LocalDate;
import java.util.List;

public interface ParkingHistoryService {

    List<ParkingHistoryResponse> getAllHistory();

    List<ParkingHistoryResponse> getHistoryByVehicleNumber(String vehicleNumber);

    List<ParkingHistoryResponse> getHistoryBetweenDates(LocalDate startDate, LocalDate endDate);
}
