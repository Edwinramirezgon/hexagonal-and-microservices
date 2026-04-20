package com.demo.reservations.application.port.out;

import com.demo.reservations.domain.model.Reservation;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    Reservation save(Reservation reservation);
    List<Reservation> findAll();
    List<Reservation> findByCreatedBy(String username);
    Optional<Reservation> findById(Long id);
    List<Reservation> findByRoomId(String roomId);
    void deleteById(Long id);
}
