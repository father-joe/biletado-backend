package edu.dhbw_ka.webeng.biletado_backend.rest;

import edu.dhbw_ka.webeng.biletado_backend.database.repositories.ReservationRepository;
import edu.dhbw_ka.webeng.biletado_backend.model.Reservation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class ReservationsController {

    @Autowired
    ReservationRepository reservationRepository;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @GetMapping(path = "/reservations/")
    List<Reservation> getReservations(@RequestParam(required = false) UUID room_id, @RequestParam(required = false) String before, @RequestParam(required = false) String after) {
        log.info("Processing request GET /reservations/ with parameters room_id={} , before={} , after={}", room_id, before, after);

        try {
            Date beforeDate = before != null ? dateFormat.parse(before) : null;
            Date afterDate = after != null ? dateFormat.parse(after) : null;

            //TODO replace with filtering by database
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

    //TODO error code 400: invalid supplied
    @GetMapping(path = "/reservations/{id}/")
    Reservation getReservation(@PathVariable UUID id) {
        log.info("Processing request GET /reservations/{}", id);
        Optional<Reservation> maybeReservation = reservationRepository.findById(id);
        return maybeReservation.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    //TODO error code 401: if the operation requires authentication but it's not given
    //TODO error code 422: if the room is not found
    @PostMapping(path = "/reservations/")
    @ResponseStatus(HttpStatus.CREATED)
    Reservation createReservation(@RequestBody Reservation reservation) {
        log.info("Processing request POST /reservations/");
        List<Reservation> conflicts = reservationRepository.findConflicts(reservation.getRoomId(), reservation.getFrom(), reservation.getTo());
        if (conflicts.size() > 0)  {
            log.error("Failed to execute request POST /reservation/: conflict with existing reservation(s) {}", conflicts);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "found conflicting reservation(s)");
        }
        reservationRepository.save(reservation);
        log.info("created reservation with id={}", reservation.getId());
        return reservation;
    }

    //TODO error code 401: if the operation requires authentication but it's not given
    //TODO error code 422: room not found or mismatching id in url and object
    @PutMapping(path = "/reservations/{id}/")
    Reservation createOrUpdateReservation(@PathVariable UUID id, @RequestBody Reservation reservation) {
        log.info("Processing request PUT /reservations/{}", id);
        if (!id.equals(reservation.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        reservationRepository.save(reservation);
        log.info("created or update reservation with id={}", id);
        return reservation;
    }

    //TODO error code 401: if the operation requires authentication but it's not given
    @DeleteMapping(path = "/reservations/{id}/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteReservation(@PathVariable UUID id) {
        log.info("Processing request DELETE /reservations/{}", id);
        Optional<Reservation> maybeReservation = reservationRepository.findById(id);
        if (maybeReservation.isEmpty()) {
            log.error("Failed to execute request DELETE /reservations/{}: reservation with id={} not found", id, id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
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
