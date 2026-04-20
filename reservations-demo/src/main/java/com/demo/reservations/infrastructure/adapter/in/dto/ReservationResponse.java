package com.demo.reservations.infrastructure.adapter.in.dto;

import java.time.LocalDateTime;

public class ReservationResponse {

    private Long          id;
    private String        roomId;
    private String        attendee;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String        createdBy;

    public ReservationResponse() {}

    public ReservationResponse(Long id, String roomId, String attendee,
                                LocalDateTime startTime, LocalDateTime endTime,
                                String createdBy) {
        this.id        = id;
        this.roomId    = roomId;
        this.attendee  = attendee;
        this.startTime = startTime;
        this.endTime   = endTime;
        this.createdBy = createdBy;
    }

    public Long          getId()        { return id; }
    public String        getRoomId()    { return roomId; }
    public String        getAttendee()  { return attendee; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime()   { return endTime; }
    public String        getCreatedBy() { return createdBy; }
}
