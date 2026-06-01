package com.parking.controller;

import com.parking.dto.request.ParkingSlotRequest;
import com.parking.dto.response.ParkingSlotResponse;
import com.parking.service.ParkingSlotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/slots")
@RequiredArgsConstructor
@Tag(name = "Parking Slots", description = "Parking slot management APIs")
public class ParkingSlotController {

    private final ParkingSlotService parkingSlotService;

    @Operation(summary = "Create a parking slot")
    @PostMapping
    public ResponseEntity<ParkingSlotResponse> createSlot(@Valid @RequestBody ParkingSlotRequest request) {
        ParkingSlotResponse response = parkingSlotService.createSlot(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get all parking slots")
    @GetMapping
    public ResponseEntity<List<ParkingSlotResponse>> getAllSlots() {
        return ResponseEntity.ok(parkingSlotService.getAllSlots());
    }

    @Operation(summary = "Get a parking slot by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ParkingSlotResponse> getSlotById(@PathVariable Long id) {
        return ResponseEntity.ok(parkingSlotService.getSlotById(id));
    }

    @Operation(summary = "Update a parking slot")
    @PutMapping("/{id}")
    public ResponseEntity<ParkingSlotResponse> updateSlot(
            @PathVariable Long id,
            @Valid @RequestBody ParkingSlotRequest request) {
        return ResponseEntity.ok(parkingSlotService.updateSlot(id, request));
    }

    @Operation(summary = "Delete a parking slot")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSlot(@PathVariable Long id) {
        parkingSlotService.deleteSlot(id);
        return ResponseEntity.noContent().build();
    }
}
