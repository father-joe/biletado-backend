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

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @GetMapping(path = "/reservations/")
    List<Reservation> getReservations(@RequestParam(required = false) UUID room_id, @RequestParam(required = false) String before, @RequestParam(required = false) String after) {
        log.info("Processing request GET /reservations/ with parameters room_id={} , before={} , after={}", room_id, before, after);

        try {
            Date beforeDate = dateFormat.parse(before);
            Date afterDate = dateFormat.parse(after);

            //TODO replace with filtering by database
            List<Reservation> reservations = reservationRepository.findAll()
                    .stream()
                    .filter(reservation -> room_id == null || reservation.getRoom_id().equals(room_id))
                    .filter(reservation -> before == null || reservation.getFrom().compareTo(beforeDate) <= 0)
                    .filter(reservation -> after == null || reservation.getFrom().compareTo(afterDate) >= 0)
                    .collect(Collectors.toList());
            if (reservations.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            return reservations;
        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/reservations/{id}/")
    Reservation getReservation(@PathVariable UUID id) {
        Optional<Reservation> maybeReservation = reservationRepository.findById(id);
        return maybeReservation.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping(path = "/reservations/")
    @ResponseStatus(HttpStatus.CREATED)
    Reservation createReservation(@RequestBody Reservation reservation) {
        reservationRepository.save(reservation);
        return reservation;
    }

    @PutMapping(path = "/reservations/{id}/")
    Reservation createOrUpdateReservation(@PathVariable UUID id, @RequestBody Reservation reservation) {
        if (!id.equals(reservation.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        reservationRepository.save(reservation);
        return reservation;
    }

    @DeleteMapping(path = "/reservations/{id}/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteReservation(@PathVariable UUID id) {
        Optional<Reservation> maybeReservation = reservationRepository.findById(id);
        if (maybeReservation.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            reservationRepository.delete(maybeReservation.get());
        }
    }

    @GetMapping(path = "/reservations/status/")
    Status getStatus() {
        return new Status();
    }
}
