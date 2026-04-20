package com.demo.reservations.infrastructure.adapter.out.mapper;

import com.demo.reservations.domain.model.Reservation;
import com.demo.reservations.infrastructure.adapter.out.persistence.ReservationEntity;


public class ReservationEntityMapper {

    public static ReservationEntity toEntity(Reservation reservation) {
        return ReservationEntity.fromDomain(reservation);
    }

    public static Reservation toDomain(ReservationEntity entity) {
        return entity.toDomain();
    }
}
