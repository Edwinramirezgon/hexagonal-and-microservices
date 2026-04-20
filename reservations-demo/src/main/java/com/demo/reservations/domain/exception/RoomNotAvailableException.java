package com.demo.reservations.domain.exception;

public class RoomNotAvailableException extends RuntimeException {

    public RoomNotAvailableException(String roomId, String startTime, String endTime) {
        super(String.format(
            "La sala '%s' ya está reservada entre %s y %s.",
            roomId, startTime, endTime
        ));
    }
}
