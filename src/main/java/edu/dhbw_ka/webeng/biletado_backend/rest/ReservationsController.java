package edu.dhbw_ka.webeng.biletado_backend.rest;

import edu.dhbw_ka.webeng.biletado_backend.model.Reservation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ReservationsController {

    List<Reservation> reservations = new ArrayList<>();

    @GetMapping(path = "/reservations")
    List<Reservation> getReservations() {
        return reservations;
    }

    @PostMapping(path = "/reservations")
    Reservation createReservation(@RequestBody Reservation reservation) {
        reservations.add(reservation);
        return reservation;
    }
}
