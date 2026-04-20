package com.demo.reservations.infrastructure.adapter.in.mapper;

import com.demo.reservations.domain.model.Reservation;
import com.demo.reservations.infrastructure.adapter.in.dto.ReservationRequest;
import com.demo.reservations.infrastructure.adapter.in.dto.ReservationResponse;

public class ReservationRequestMapper {

    public static Reservation toDomain(ReservationRequest request) {
        return Reservation.create(
                request.getRoomId(),
                request.getAttendee(),
                request.getStartTime(),
                request.getEndTime(),
                request.getCreatedBy()
        );
    }

    public static ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getRoomId(),
                reservation.getAttendee(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getCreatedBy()
        );
    }
}
