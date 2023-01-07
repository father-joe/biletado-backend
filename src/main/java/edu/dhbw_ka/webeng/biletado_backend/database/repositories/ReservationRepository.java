package edu.dhbw_ka.webeng.biletado_backend.database.repositories;

import edu.dhbw_ka.webeng.biletado_backend.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
}
