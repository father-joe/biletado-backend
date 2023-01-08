package edu.dhbw_ka.webeng.biletado_backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "reservations")
@Data
public class Reservation {
    @Id
    UUID id;

    @Temporal(TemporalType.DATE)
    @Column(name = "\"from\"")  // from is keyword so escape as "from"
    Date from;

    @Temporal(TemporalType.DATE)
    @Column(name = "\"to\"")    // to is keyword so escape as "to"
    Date to;

    @Column(name = "room_id")
    @JsonProperty("room_id")
    UUID roomId;
}
