package com.parking.service;

import com.parking.dto.request.VehicleEntryRequest;
import com.parking.dto.response.VehicleEntryResponse;

public interface ParkingService {

    VehicleEntryResponse registerVehicleEntry(VehicleEntryRequest request);
}
