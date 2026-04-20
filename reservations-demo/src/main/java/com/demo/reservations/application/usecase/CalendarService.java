package com.demo.reservations.application.usecase;

import com.demo.reservations.application.port.in.CalendarUseCase;
import com.demo.reservations.application.port.out.ReservationRepository;
import com.demo.reservations.domain.model.CalendarExport;
import com.demo.reservations.domain.model.Reservation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalendarService implements CalendarUseCase {

    private final ReservationRepository repository;

    public CalendarService(ReservationRepository repository) {
        this.repository = repository;
    }

    @Override
    public String exportOne(Long reservationId) {
        Reservation reservation = repository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con id: " + reservationId));
        return CalendarExport.toIcal(reservation);
    }

    @Override
    public String exportAll() {
        List<Reservation> all = repository.findAll();
        return CalendarExport.toIcal(all, "Reservas de Salas");
    }
}
