package com.demo.frontend.application.port.out;

import com.demo.frontend.application.port.in.dto.ReservationDto;
import java.util.List;

public interface ReservationServicePort {
    List<ReservationDto> findAll(String token);
    ReservationDto create(ReservationDto dto, String token);
    ReservationDto update(Long id, ReservationDto dto, String token);
    void delete(Long id, String token);
    byte[] downloadIcs(Long id, String token);
    byte[] downloadAllIcs();
}
