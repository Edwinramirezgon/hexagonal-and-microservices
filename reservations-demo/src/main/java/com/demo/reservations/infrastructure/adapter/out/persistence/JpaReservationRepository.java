package com.demo.reservations.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JpaReservationRepository extends JpaRepository<ReservationEntity, Long> {
    List<ReservationEntity> findByRoomId(String roomId);
    List<ReservationEntity> findByCreatedBy(String createdBy);
}
