package edu.dhbw_ka.webeng.biletado_backend.rest;

import edu.dhbw_ka.webeng.biletado_backend.database.repositories.ReservationRepository;
import edu.dhbw_ka.webeng.biletado_backend.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class ReservationsController {

    @Autowired
    ReservationRepository reservationRepository;

    @GetMapping(path = "/reservations")
    List<Reservation> getReservations() {
        return reservationRepository.findAll();
    }

    @PostMapping(path = "/reservations")
    Reservation createReservation(@RequestBody Reservation reservation) {
        reservationRepository.save(reservation);
        return reservation;
    }

    @GetMapping(path = "/reservations/{id}")
    Reservation getReservation(@PathVariable UUID id) {
        Optional<Reservation> maybeReservation = reservationRepository.findById(id);
        return maybeReservation.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

}
