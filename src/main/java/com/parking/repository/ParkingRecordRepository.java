package com.parking.repository;

import com.parking.entity.ParkingRecord;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingRecordRepository extends JpaRepository<ParkingRecord, Long> {

    boolean existsByVehicleVehicleNumberAndExitTimeIsNull(String vehicleNumber);

    @EntityGraph(attributePaths = {"vehicle", "parkingSlot"})
    Optional<ParkingRecord> findByVehicleVehicleNumberAndExitTimeIsNull(String vehicleNumber);
}
