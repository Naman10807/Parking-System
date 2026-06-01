package com.parking.service.impl;

import com.parking.dto.request.ParkingSlotRequest;
import com.parking.dto.response.ParkingSlotResponse;
import com.parking.entity.ParkingSlot;
import com.parking.exception.DuplicateResourceException;
import com.parking.exception.ResourceNotFoundException;
import com.parking.mapper.ParkingSlotMapper;
import com.parking.repository.ParkingSlotRepository;
import com.parking.service.ParkingSlotService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParkingSlotServiceImpl implements ParkingSlotService {

    private final ParkingSlotRepository parkingSlotRepository;
    private final ParkingSlotMapper parkingSlotMapper;

    @Override
    @Transactional
    public ParkingSlotResponse createSlot(ParkingSlotRequest request) {
        if (parkingSlotRepository.existsBySlotNumber(request.getSlotNumber().trim())) {
            throw new DuplicateResourceException(
                    "Parking slot with number '" + request.getSlotNumber().trim() + "' already exists");
        }

        ParkingSlot savedSlot = parkingSlotRepository.save(parkingSlotMapper.toEntity(request));
        return parkingSlotMapper.toResponse(savedSlot);
    }

    @Override
    public List<ParkingSlotResponse> getAllSlots() {
        return parkingSlotRepository.findAll().stream()
                .map(parkingSlotMapper::toResponse)
                .toList();
    }

    @Override
    public ParkingSlotResponse getSlotById(Long id) {
        ParkingSlot slot = findSlotById(id);
        return parkingSlotMapper.toResponse(slot);
    }

    @Override
    @Transactional
    public ParkingSlotResponse updateSlot(Long id, ParkingSlotRequest request) {
        ParkingSlot slot = findSlotById(id);

        if (parkingSlotRepository.existsBySlotNumberAndIdNot(request.getSlotNumber().trim(), id)) {
            throw new DuplicateResourceException(
                    "Parking slot with number '" + request.getSlotNumber().trim() + "' already exists");
        }

        parkingSlotMapper.updateEntity(slot, request);
        return parkingSlotMapper.toResponse(parkingSlotRepository.save(slot));
    }

    @Override
    @Transactional
    public void deleteSlot(Long id) {
        ParkingSlot slot = findSlotById(id);
        parkingSlotRepository.delete(slot);
    }

    private ParkingSlot findSlotById(Long id) {
        return parkingSlotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parking slot not found with id: " + id));
    }
}
