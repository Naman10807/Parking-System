package com.parking.repository;

import com.parking.entity.ParkingRecord;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingRecordRepository extends JpaRepository<ParkingRecord, Long> {

    boolean existsByVehicleVehicleNumberAndExitTimeIsNull(String vehicleNumber);

    @EntityGraph(attributePaths = {"vehicle", "parkingSlot"})
    Optional<ParkingRecord> findByVehicleVehicleNumberAndExitTimeIsNull(String vehicleNumber);

    long countByExitTimeIsNull();

    @Query("SELECT COALESCE(SUM(pr.parkingFee), 0) FROM ParkingRecord pr WHERE pr.parkingFee IS NOT NULL")
    double sumCompletedParkingFees();
}
