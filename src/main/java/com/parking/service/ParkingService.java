package com.parking.service;

import com.parking.dto.request.VehicleEntryRequest;
import com.parking.dto.response.VehicleEntryResponse;
import com.parking.dto.response.VehicleExitResponse;

public interface ParkingService {

    VehicleEntryResponse registerVehicleEntry(VehicleEntryRequest request);

    VehicleExitResponse processVehicleExit(String vehicleNumber);
}
