package com.parking.exception;

public class ActiveParkingRecordNotFoundException extends RuntimeException {

    public ActiveParkingRecordNotFoundException(String message) {
        super(message);
    }
}
