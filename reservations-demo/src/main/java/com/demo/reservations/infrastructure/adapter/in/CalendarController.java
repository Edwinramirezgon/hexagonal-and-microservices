package com.demo.reservations.infrastructure.adapter.in;

import com.demo.reservations.application.port.in.CalendarUseCase;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/calendar")
@CrossOrigin(origins = "*")
public class CalendarController {

    private final CalendarUseCase calendarUseCase;

    public CalendarController(CalendarUseCase calendarUseCase) {
        this.calendarUseCase = calendarUseCase;
    }

    @GetMapping("/reservation/{id}.ics")
    public ResponseEntity<byte[]> downloadOne(@PathVariable Long id) {
        String ical = calendarUseCase.exportOne(id);
        return icalResponse(ical, "reserva-" + id + ".ics");
    }

    @GetMapping("/reservations.ics")
    public ResponseEntity<byte[]> downloadAll() {
        String ical = calendarUseCase.exportAll();
        return icalResponse(ical, "reservas.ics");
    }

    private ResponseEntity<byte[]> icalResponse(String ical, String filename) {
        byte[] bytes = ical.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/calendar; charset=UTF-8"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
        headers.setContentLength(bytes.length);
        return ResponseEntity.ok().headers(headers).body(bytes);
    }
}
