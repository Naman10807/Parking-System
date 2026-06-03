package com.parking.repository;

import com.parking.entity.ParkingRecord;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingRecordRepository extends JpaRepository<ParkingRecord, Long> {

    boolean existsByVehicleVehicleNumberAndExitTimeIsNull(String vehicleNumber);

    @EntityGraph(attributePaths = {"vehicle", "parkingSlot"})
    Optional<ParkingRecord> findByVehicleVehicleNumberAndExitTimeIsNull(String vehicleNumber);

    long countByExitTimeIsNull();

    @Query("SELECT COALESCE(SUM(pr.parkingFee), 0) FROM ParkingRecord pr WHERE pr.parkingFee IS NOT NULL")
    double sumCompletedParkingFees();

    @EntityGraph(attributePaths = {"vehicle", "parkingSlot"})
    List<ParkingRecord> findAllByOrderByEntryTimeDesc();

    @EntityGraph(attributePaths = {"vehicle", "parkingSlot"})
    List<ParkingRecord> findByVehicle_VehicleNumberIgnoreCaseOrderByEntryTimeDesc(String vehicleNumber);

    @EntityGraph(attributePaths = {"vehicle", "parkingSlot"})
    @Query("""
            SELECT pr FROM ParkingRecord pr
            WHERE pr.entryTime >= :startDateTime AND pr.entryTime < :endDateTime
            ORDER BY pr.entryTime DESC
            """)
    List<ParkingRecord> findByEntryTimeBetweenOrderByEntryTimeDesc(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime);
}
