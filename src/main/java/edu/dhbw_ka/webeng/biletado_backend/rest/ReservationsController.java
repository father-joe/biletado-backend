package edu.dhbw_ka.webeng.biletado_backend.rest;

import edu.dhbw_ka.webeng.biletado_backend.database.repositories.ReservationRepository;
import edu.dhbw_ka.webeng.biletado_backend.model.Reservation;
import edu.dhbw_ka.webeng.biletado_backend.model.Room;
import edu.dhbw_ka.webeng.biletado_backend.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class ReservationsController {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    RoomService roomService;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @GetMapping(path = "/reservations/")
    List<Reservation> getReservations(@RequestParam(required = false) UUID room_id, @RequestParam(required = false) String before, @RequestParam(required = false) String after) {
        log.info("Processing request GET /reservations/ with parameters room_id={} , before={} , after={}", room_id, before, after);

        try {
            Date beforeDate = before != null ? dateFormat.parse(before) : null;
            Date afterDate = after != null ? dateFormat.parse(after) : null;

            List<Reservation> reservations = reservationRepository.findAll()
                    .stream()
                    .filter(reservation -> room_id == null || reservation.getRoomId().equals(room_id))
                    .filter(reservation -> before == null || reservation.getFrom().compareTo(beforeDate) <= 0)
                    .filter(reservation -> after == null || reservation.getFrom().compareTo(afterDate) >= 0)
                    .collect(Collectors.toList());
            if (reservations.isEmpty()) {
                log.error("Failed to execute request GET /reservation/: no reservation found for filters room_id={}, before={}, after={}", room_id, before, after);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            return reservations;
        } catch (ParseException e) {
            log.error("Failed to execute request GET /reservation/: No valid date in parameters");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/reservations/{id}/")
    Reservation getReservation(@PathVariable UUID id) {
        log.info("Processing request GET /reservations/{}", id);
        Optional<Reservation> maybeReservation = reservationRepository.findById(id);
        return maybeReservation.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "reservation not found"));
    }

    @PostMapping(path = "/reservations/")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    Reservation createReservation(@RequestBody Reservation reservation) {
        log.info("Processing request POST /reservations/");

        try {
            Room room = roomService.getRoom(reservation.getRoomId());
        } catch(Exception e) {
            log.error("could not get room with id={}", reservation.getRoomId(), e);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Room not found");
        }

        List<Reservation> conflicts = reservationRepository.findConflicts(reservation.getRoomId(), reservation.getFrom(), reservation.getTo());
        if (conflicts.size() > 0)  {
            log.error("Failed to execute request POST /reservation/: conflict with existing reservation(s) {}", conflicts);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "found conflicting reservation(s)");
        }
        reservationRepository.save(reservation);
        log.info("created reservation with id={}", reservation.getId());
        return reservation;
    }

    //TODO error code 422: room not found or mismatching id in url and object
    @PutMapping(path = "/reservations/{id}/")
    @PreAuthorize("isAuthenticated()")
    Reservation createOrUpdateReservation(@PathVariable UUID id, @RequestBody Reservation reservation) {
        log.info("Processing request PUT /reservations/{}", id);

        try {
            Room room = roomService.getRoom(reservation.getRoomId());
        } catch(Exception e) {
            log.error("could not get room with id={}", reservation.getRoomId(), e);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Room not found");
        }

        if (!id.equals(reservation.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        reservationRepository.save(reservation);
        log.info("created or update reservation with id={}", id);
        return reservation;
    }

    @DeleteMapping(path = "/reservations/{id}/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    void deleteReservation(@PathVariable UUID id) {
        log.info("Processing request DELETE /reservations/{}", id);
        Optional<Reservation> maybeReservation = reservationRepository.findById(id);
        if (maybeReservation.isEmpty()) {
            log.error("Failed to execute request DELETE /reservations/{}: reservation with id={} not found", id, id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "room not found");
        } else {
            reservationRepository.delete(maybeReservation.get());
            log.info("Delete reservation with id={}", id);
        }
    }

    @GetMapping(path = "/reservations/status/")
    Status getStatus() {
        log.info("Processing request GET /reservations/status/");
        return new Status();
    }
}
