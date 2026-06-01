package com.parking.service;

import com.parking.dto.request.ParkingSlotRequest;
import com.parking.dto.response.ParkingSlotResponse;
import java.util.List;

public interface ParkingSlotService {

    ParkingSlotResponse createSlot(ParkingSlotRequest request);

    List<ParkingSlotResponse> getAllSlots();

    ParkingSlotResponse getSlotById(Long id);

    ParkingSlotResponse updateSlot(Long id, ParkingSlotRequest request);

    void deleteSlot(Long id);
}
