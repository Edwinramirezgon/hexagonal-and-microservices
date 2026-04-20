package com.demo.reservations.infrastructure.adapter.in.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReservationRequest {

    private String        roomId;
    private String        attendee;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String        createdBy;

    public ReservationRequest() {}

    public String        getRoomId()    { return roomId; }
    public String        getAttendee()  { return attendee; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime()   { return endTime; }
    public String        getCreatedBy() { return createdBy; }

    public void setRoomId(String roomId)            { this.roomId = roomId; }
    public void setAttendee(String attendee)        { this.attendee = attendee; }
    public void setStartTime(LocalDateTime start)   { this.startTime = start; }
    public void setEndTime(LocalDateTime end)       { this.endTime = end; }
    public void setCreatedBy(String createdBy)      { this.createdBy = createdBy; }
}
