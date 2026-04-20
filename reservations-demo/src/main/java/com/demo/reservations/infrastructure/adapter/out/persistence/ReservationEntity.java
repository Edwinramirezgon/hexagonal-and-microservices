package com.demo.reservations.infrastructure.adapter.out.persistence;

import com.demo.reservations.domain.model.Reservation;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String roomId;

    @Column(nullable = false)
    private String attendee;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private String createdBy;

    public ReservationEntity() {}

    public static ReservationEntity fromDomain(Reservation r) {
        ReservationEntity e = new ReservationEntity();
        e.id        = r.getId();
        e.roomId    = r.getRoomId();
        e.attendee  = r.getAttendee();
        e.startTime = r.getStartTime();
        e.endTime   = r.getEndTime();
        e.createdBy = r.getCreatedBy();
        return e;
    }

    public Reservation toDomain() {
        Reservation r = Reservation.create(roomId, attendee, startTime, endTime, createdBy);
        r.setId(id);
        return r;
    }

    public Long          getId()        { return id; }
    public void          setId(Long id) { this.id = id; }
    public String        getRoomId()    { return roomId; }
    public String        getAttendee()  { return attendee; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime()   { return endTime; }
    public String        getCreatedBy() { return createdBy; }
}
