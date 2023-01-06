package edu.dhbw_ka.webeng.biletado_backend.rest;

import edu.dhbw_ka.webeng.biletado_backend.model.Reservation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class ReservationsController {

    @GetMapping(path = "/reservations")
    List<Reservation> getReservations() {

        Reservation r1 = new Reservation();
        r1.setId("id: 1");
        r1.setFrom(new Date());
        r1.setTo(new Date());
        r1.setRoom_id("room_id: 1");

        List<Reservation> reservations = new ArrayList<>();
        reservations.add(r1);
        return reservations;
    }

    @PostMapping(path = "/reservations")
    Reservation createReservation(@RequestBody Reservation reservation) {
        return reservation;
    }
}
