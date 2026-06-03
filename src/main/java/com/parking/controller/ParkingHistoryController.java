package com.parking.controller;

import com.parking.dto.response.ParkingHistoryResponse;
import com.parking.service.ParkingHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
@Validated
@Tag(name = "Parking History", description = "Parking record history and search APIs")
public class ParkingHistoryController {

    private final ParkingHistoryService parkingHistoryService;

    @Operation(summary = "Get all parking history records")
    @PreAuthorize("hasAnyRole('ADMIN', 'ATTENDANT')")
    @GetMapping
    public ResponseEntity<List<ParkingHistoryResponse>> getAllHistory() {
        return ResponseEntity.ok(parkingHistoryService.getAllHistory());
    }

    @Operation(summary = "Get parking history by vehicle number")
    @PreAuthorize("hasAnyRole('ADMIN', 'ATTENDANT')")
    @GetMapping("/vehicle/{vehicleNumber}")
    public ResponseEntity<List<ParkingHistoryResponse>> getHistoryByVehicleNumber(
            @PathVariable
            @NotBlank(message = "Vehicle number is required")
            @Pattern(regexp = "^[A-Za-z0-9]+$", message = "Vehicle number must be alphanumeric")
            String vehicleNumber) {
        return ResponseEntity.ok(parkingHistoryService.getHistoryByVehicleNumber(vehicleNumber));
    }

    @Operation(summary = "Get parking history between dates")
    @PreAuthorize("hasAnyRole('ADMIN', 'ATTENDANT')")
    @GetMapping("/date-range")
    public ResponseEntity<List<ParkingHistoryResponse>> getHistoryBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(parkingHistoryService.getHistoryBetweenDates(startDate, endDate));
    }
}
