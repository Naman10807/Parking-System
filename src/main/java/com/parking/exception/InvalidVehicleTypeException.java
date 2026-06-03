package com.parking.exception;

public class InvalidVehicleTypeException extends RuntimeException {

    public InvalidVehicleTypeException(String message) {
        super(message);
    }
}
