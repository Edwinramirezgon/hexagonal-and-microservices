package com.demo.frontend.application.port.in;

import com.demo.frontend.application.port.in.dto.ReservationDto;
import java.util.List;

public interface ReservationUseCase {
    List<ReservationDto> findAll(String token);
    ReservationDto create(ReservationDto dto, String token);
    ReservationDto update(Long id, ReservationDto dto, String token);
    void delete(Long id, String token);
    byte[] downloadIcs(Long id, String token);
    byte[] downloadAllIcs();
}
