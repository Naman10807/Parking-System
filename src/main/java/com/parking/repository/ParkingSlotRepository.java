package com.parking.repository;

import com.parking.entity.ParkingSlot;
import com.parking.entity.SlotStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {

    boolean existsBySlotNumber(String slotNumber);

    boolean existsBySlotNumberAndIdNot(String slotNumber, Long id);

    Optional<ParkingSlot> findFirstByStatusAndVehicleTypeIgnoreCaseOrderByIdAsc(
            SlotStatus status, String vehicleType);

    Optional<ParkingSlot> findFirstByStatusOrderByIdAsc(SlotStatus status);

    long countByStatus(SlotStatus status);
}
