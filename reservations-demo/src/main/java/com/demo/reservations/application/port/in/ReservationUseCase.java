package com.demo.reservations.application.port.in;

import com.demo.reservations.domain.model.Reservation;
import java.util.List;

public interface ReservationUseCase {
    Reservation create(Reservation reservation);
    List<Reservation> findAll();
    List<Reservation> findByUser(String username);
    Reservation findById(Long id);
    Reservation update(Long id, Reservation reservation);
    void deleteById(Long id);
}
