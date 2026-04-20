package com.demo.reservations.infrastructure.adapter.out;

import com.demo.reservations.application.port.out.ReservationRepository;
import com.demo.reservations.domain.model.Reservation;
import com.demo.reservations.infrastructure.adapter.out.persistence.JpaReservationRepository;
import com.demo.reservations.infrastructure.adapter.out.persistence.ReservationEntity;

import java.util.List;
import java.util.Optional;

public class SqlServerReservationAdapter implements ReservationRepository {

    private final JpaReservationRepository jpa;

    public SqlServerReservationAdapter(JpaReservationRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Reservation save(Reservation reservation) {
        ReservationEntity entity  = ReservationEntity.fromDomain(reservation);
        ReservationEntity saved   = jpa.save(entity);
        return saved.toDomain();
    }

    @Override
    public List<Reservation> findAll() {
        return jpa.findAll().stream().map(ReservationEntity::toDomain).toList();
    }

    @Override
    public List<Reservation> findByCreatedBy(String username) {
        return jpa.findByCreatedBy(username).stream().map(ReservationEntity::toDomain).toList();
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return jpa.findById(id).map(ReservationEntity::toDomain);
    }

    @Override
    public List<Reservation> findByRoomId(String roomId) {
        return jpa.findByRoomId(roomId).stream().map(ReservationEntity::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        jpa.deleteById(id);
    }
}
