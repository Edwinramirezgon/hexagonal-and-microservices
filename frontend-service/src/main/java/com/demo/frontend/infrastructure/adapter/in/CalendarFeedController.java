package com.demo.frontend.infrastructure.adapter.in;

import com.demo.frontend.application.port.in.ReservationUseCase;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ical")
@CrossOrigin(origins = "*")
public class CalendarFeedController {

    private final ReservationUseCase reservationUseCase;

    public CalendarFeedController(ReservationUseCase reservationUseCase) {
        this.reservationUseCase = reservationUseCase;
    }

    @GetMapping("/reservations.ics")
    public ResponseEntity<byte[]> feed() {
        byte[] ics = reservationUseCase.downloadAllIcs();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/calendar; charset=UTF-8"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"reservations.ics\"");
        return ResponseEntity.ok().headers(headers).body(ics);
    }
}
