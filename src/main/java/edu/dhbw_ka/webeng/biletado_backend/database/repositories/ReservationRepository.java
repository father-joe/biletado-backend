package edu.dhbw_ka.webeng.biletado_backend.database.repositories;

import edu.dhbw_ka.webeng.biletado_backend.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    @Query("select r from Reservation r where r.roomId = ?1 " +
            "and not ((?2 < r.from and ?3 < r.from) or (?2 > r.to and ?3 > r.to))")
    List<Reservation> findConflicts(UUID roomId, Date dateFrom, Date dateTo);
}
