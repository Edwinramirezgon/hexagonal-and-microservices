package com.demo.frontend.application.usecase;

import com.demo.frontend.application.port.in.ReservationUseCase;
import com.demo.frontend.application.port.in.dto.ReservationDto;
import com.demo.frontend.application.port.out.ReservationServicePort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService implements ReservationUseCase {

    private final ReservationServicePort reservationServicePort;

    public ReservationService(ReservationServicePort reservationServicePort) {
        this.reservationServicePort = reservationServicePort;
    }

    @Override
    public List<ReservationDto> findAll(String token) {
        return reservationServicePort.findAll(token);
    }

    @Override
    public ReservationDto create(ReservationDto dto, String token) {
        return reservationServicePort.create(dto, token);
    }

    @Override
    public ReservationDto update(Long id, ReservationDto dto, String token) {
        return reservationServicePort.update(id, dto, token);
    }

    @Override
    public void delete(Long id, String token) {
        reservationServicePort.delete(id, token);
    }

    @Override
    public byte[] downloadIcs(Long id, String token) {
        return reservationServicePort.downloadIcs(id, token);
    }

    @Override
    public byte[] downloadAllIcs() {
        return reservationServicePort.downloadAllIcs();
    }
}
