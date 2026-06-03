package com.parking.util;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParkingFeeCalculatorTest {

    @Test
    void shouldCalculateDurationInWholeHoursRoundedUp() {
        LocalDateTime entry = LocalDateTime.of(2026, 6, 1, 10, 0);
        LocalDateTime exit = LocalDateTime.of(2026, 6, 1, 13, 0);

        assertEquals(3, ParkingFeeCalculator.calculateDurationInHours(entry, exit));
    }

    @Test
    void shouldApplyCarHourlyRate() {
        assertEquals(60.0, ParkingFeeCalculator.calculateParkingFee("CAR", 3));
    }

    @Test
    void shouldApplyBikeHourlyRateWithMinimumCharge() {
        assertEquals(20.0, ParkingFeeCalculator.calculateParkingFee("BIKE", 1));
    }

    @Test
    void shouldApplyTruckHourlyRate() {
        assertEquals(100.0, ParkingFeeCalculator.calculateParkingFee("TRUCK", 2));
    }

    @Test
    void shouldRejectUnsupportedVehicleType() {
        assertThrows(
                com.parking.exception.InvalidVehicleTypeException.class,
                () -> ParkingFeeCalculator.calculateParkingFee("BUS", 2));
    }
}
