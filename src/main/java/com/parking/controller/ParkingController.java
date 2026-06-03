package com.parking.controller;

import com.parking.dto.request.VehicleEntryRequest;
import com.parking.dto.response.VehicleEntryResponse;
import com.parking.dto.response.VehicleExitResponse;
import com.parking.service.ParkingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/parking")
@RequiredArgsConstructor
@Validated
@Tag(name = "Parking", description = "Vehicle entry and parking operations")
public class ParkingController {

    private final ParkingService parkingService;

    @Operation(summary = "Register vehicle entry")
    @PreAuthorize("hasAnyRole('ADMIN', 'ATTENDANT')")
    @PostMapping("/entry")
    public ResponseEntity<VehicleEntryResponse> registerVehicleEntry(
            @Valid @RequestBody VehicleEntryRequest request) {
        VehicleEntryResponse response = parkingService.registerVehicleEntry(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Process vehicle exit")
    @PreAuthorize("hasAnyRole('ADMIN', 'ATTENDANT')")
    @PostMapping("/exit/{vehicleNumber}")
    public ResponseEntity<VehicleExitResponse> processVehicleExit(
            @PathVariable
            @NotBlank(message = "Vehicle number is required")
            @Pattern(regexp = "^[A-Za-z0-9]+$", message = "Vehicle number must be alphanumeric")
            String vehicleNumber) {
        VehicleExitResponse response = parkingService.processVehicleExit(vehicleNumber);
        return ResponseEntity.ok(response);
    }
}
