package com.parking.repository;

import com.parking.entity.ParkingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingRecordRepository extends JpaRepository<ParkingRecord, Long> {

    boolean existsByVehicleVehicleNumberAndExitTimeIsNull(String vehicleNumber);
}
