package edu.dhbw_ka.webeng.biletado_backend.rest;

import edu.dhbw_ka.webeng.biletado_backend.database.repositories.ReservationRepository;
import edu.dhbw_ka.webeng.biletado_backend.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
