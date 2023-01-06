package edu.dhbw_ka.webeng.biletado_backend.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservationsController {
    @GetMapping(path = "/reservations")
    String getReservations() {
        return "Hello World";
    }
}
