package com.demo.reservations.domain.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CalendarExport {

    private static final DateTimeFormatter ICAL_FMT     = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
    private static final DateTimeFormatter ICAL_UTC_FMT = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");


    private CalendarExport() {}

    public static String toIcal(List<Reservation> reservations, String calendarName) {
        StringBuilder sb = new StringBuilder();
        sb.append("BEGIN:VCALENDAR\r\n");
        sb.append("VERSION:2.0\r\n");
        sb.append("PRODID:-//Sistema de Reservas//ES\r\n");
        sb.append("CALSCALE:GREGORIAN\r\n");
        sb.append("METHOD:PUBLISH\r\n");
        sb.append("X-WR-CALNAME:").append(calendarName).append("\r\n");
        sb.append("X-WR-TIMEZONE:America/Bogota\r\n");
        sb.append("REFRESH-INTERVAL;VALUE=DURATION:PT1H\r\n");
        sb.append(vtimezone());
        for (Reservation r : reservations) sb.append(toVEvent(r));
        sb.append("END:VCALENDAR\r\n");
        return sb.toString();
    }

    public static String toIcal(Reservation r) {
        StringBuilder sb = new StringBuilder();
        sb.append("BEGIN:VCALENDAR\r\n");
        sb.append("VERSION:2.0\r\n");
        sb.append("PRODID:-//Sistema de Reservas//ES\r\n");
        sb.append("CALSCALE:GREGORIAN\r\n");
        sb.append("METHOD:PUBLISH\r\n");
        sb.append("X-WR-TIMEZONE:America/Bogota\r\n");
        sb.append(vtimezone());
        sb.append(toVEvent(r));
        sb.append("END:VCALENDAR\r\n");
        return sb.toString();
    }

    private static String toVEvent(Reservation r) {
        String uid     = "reserva-" + r.getId() + "@sistema-reservas";
        String dtStart = "DTSTART;TZID=America/Bogota:" + r.getStartTime().format(ICAL_FMT);
        String dtEnd   = "DTEND;TZID=America/Bogota:"   + r.getEndTime().format(ICAL_FMT);
        String now     = ZonedDateTime.now(ZoneId.of("UTC")).format(ICAL_UTC_FMT);

        return "BEGIN:VEVENT\r\n" +
               "UID:" + uid + "\r\n" +
               "DTSTAMP:" + now + "\r\n" +
               dtStart + "\r\n" +
               dtEnd   + "\r\n" +
               "SUMMARY:Reserva " + r.getRoomId() + "\r\n" +
               "DESCRIPTION:Asistente: " + r.getAttendee() + "\\nSala: " + r.getRoomId() + "\r\n" +
               "LOCATION:" + r.getRoomId() + "\r\n" +
               "END:VEVENT\r\n";
    }

    private static String vtimezone() {
        return "BEGIN:VTIMEZONE\r\n" +
               "TZID:America/Bogota\r\n" +
               "BEGIN:STANDARD\r\n" +
               "TZOFFSETFROM:-0500\r\n" +
               "TZOFFSETTO:-0500\r\n" +
               "TZNAME:COT\r\n" +
               "DTSTART:19700101T000000\r\n" +
               "END:STANDARD\r\n" +
               "END:VTIMEZONE\r\n";
    }
}
