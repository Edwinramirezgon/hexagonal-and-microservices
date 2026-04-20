package com.demo.reservations.application.usecase;

import com.demo.reservations.application.port.in.ReservationUseCase;
import com.demo.reservations.application.port.out.AuthServicePort;
import com.demo.reservations.application.port.out.EmailQueuePort;
import com.demo.reservations.application.port.out.ReservationRepository;
import com.demo.reservations.domain.exception.RoomNotAvailableException;
import com.demo.reservations.domain.model.Reservation;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReservationService implements ReservationUseCase {

    private final ReservationRepository repository;
    private final EmailQueuePort        emailQueuePort;
    private final AuthServicePort       authServicePort;
    private final DateTimeFormatter     fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public ReservationService(ReservationRepository repository,
                              EmailQueuePort emailQueuePort,
                              AuthServicePort authServicePort) {
        this.repository      = repository;
        this.emailQueuePort  = emailQueuePort;
        this.authServicePort = authServicePort;
    }

    @Override
    public Reservation create(Reservation reservation) {
        boolean hasConflict = repository.findByRoomId(reservation.getRoomId())
                .stream()
                .anyMatch(e -> e.overlaps(reservation.getStartTime(), reservation.getEndTime()));

        if (hasConflict)
            throw new RoomNotAvailableException(
                    reservation.getRoomId(),
                    reservation.getStartTime().toString(),
                    reservation.getEndTime().toString());

        Reservation saved = repository.save(reservation);
        publishConfirmationEmail(saved);
        return saved;
    }

    @Override
    public List<Reservation> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Reservation> findByUser(String username) {
        return repository.findByCreatedBy(username);
    }

    @Override
    public Reservation findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con id: " + id));
    }

    @Override
    public Reservation update(Long id, Reservation updated) {
        Reservation existing = findById(id);

        boolean hasConflict = repository.findByRoomId(updated.getRoomId())
                .stream()
                .filter(r -> !r.getId().equals(id))
                .anyMatch(r -> r.overlaps(updated.getStartTime(), updated.getEndTime()));

        if (hasConflict)
            throw new RoomNotAvailableException(
                    updated.getRoomId(),
                    updated.getStartTime().toString(),
                    updated.getEndTime().toString());

        existing.setRoomId(updated.getRoomId());
        existing.setAttendee(updated.getAttendee());
        existing.setStartTime(updated.getStartTime());
        existing.setEndTime(updated.getEndTime());
        return repository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        findById(id);
        repository.deleteById(id);
    }

    private void publishConfirmationEmail(Reservation r) {
        try {
            String email = authServicePort.getEmailByUsername(r.getCreatedBy());
            if (email == null || email.isBlank()) return;
            String body = String.format(
                "Tu reserva ha sido confirmada.%n%nSala: %s%nAsistente: %s%nInicio: %s%nFin: %s",
                r.getRoomId(), r.getAttendee(),
                r.getStartTime().format(fmt),
                r.getEndTime().format(fmt));
            emailQueuePort.publish(email, "Confirmacion de Reserva - " + r.getRoomId(), body);
        } catch (Exception ignored) {}
    }
}
