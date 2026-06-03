package com.parking.util;

import com.parking.exception.InvalidVehicleTypeException;
import java.time.Duration;
import java.time.LocalDateTime;

public final class ParkingFeeCalculator {

    private static final double MINIMUM_CHARGE = 20.0;

    private static final double CAR_RATE_PER_HOUR = 20.0;
    private static final double BIKE_RATE_PER_HOUR = 10.0;
    private static final double TRUCK_RATE_PER_HOUR = 50.0;

    private ParkingFeeCalculator() {
    }

    public static long calculateDurationInHours(LocalDateTime entryTime, LocalDateTime exitTime) {
        long minutes = Duration.between(entryTime, exitTime).toMinutes();
        if (minutes <= 0) {
            return 1;
        }
        return (long) Math.ceil(minutes / 60.0);
    }

    public static double calculateParkingFee(String vehicleType, long durationInHours) {
        double hourlyRate = resolveHourlyRate(vehicleType);
        double calculatedFee = durationInHours * hourlyRate;
        return Math.max(MINIMUM_CHARGE, calculatedFee);
    }

    private static double resolveHourlyRate(String vehicleType) {
        return switch (vehicleType.trim().toUpperCase()) {
            case "CAR" -> CAR_RATE_PER_HOUR;
            case "BIKE" -> BIKE_RATE_PER_HOUR;
            case "TRUCK" -> TRUCK_RATE_PER_HOUR;
            default -> throw new InvalidVehicleTypeException(
                    "Unsupported vehicle type for fee calculation: " + vehicleType);
        };
    }
}
