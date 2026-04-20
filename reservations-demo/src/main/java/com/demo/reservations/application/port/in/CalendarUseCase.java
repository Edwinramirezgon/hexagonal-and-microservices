package com.demo.reservations.application.port.in;


public interface CalendarUseCase {
    String exportOne(Long reservationId);
    String exportAll();
}
