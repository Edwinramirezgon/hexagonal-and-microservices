package com.demo.frontend.application.port.in.dto;

import java.time.LocalDateTime;

public class ReservationDto {
    private Long          id;
    private String        roomId;
    private String        attendee;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String        createdBy;

    public ReservationDto() {}

    public Long          getId()        { return id; }
    public String        getRoomId()    { return roomId; }
    public String        getAttendee()  { return attendee; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime()   { return endTime; }
    public String        getCreatedBy() { return createdBy; }

    public void setId(Long id)                    { this.id = id; }
    public void setRoomId(String roomId)          { this.roomId = roomId; }
    public void setAttendee(String attendee)      { this.attendee = attendee; }
    public void setStartTime(LocalDateTime start) { this.startTime = start; }
    public void setEndTime(LocalDateTime end)     { this.endTime = end; }
    public void setCreatedBy(String createdBy)    { this.createdBy = createdBy; }
}
