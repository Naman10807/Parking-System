package com.parking.controller;

import com.parking.dto.request.VehicleEntryRequest;
import com.parking.dto.response.VehicleEntryResponse;
import com.parking.service.ParkingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/parking")
@RequiredArgsConstructor
@Tag(name = "Parking", description = "Vehicle entry and parking operations")
public class ParkingController {

    private final ParkingService parkingService;

    @Operation(summary = "Register vehicle entry")
    @PostMapping("/entry")
    public ResponseEntity<VehicleEntryResponse> registerVehicleEntry(
            @Valid @RequestBody VehicleEntryRequest request) {
        VehicleEntryResponse response = parkingService.registerVehicleEntry(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
