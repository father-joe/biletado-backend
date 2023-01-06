package edu.dhbw_ka.webeng.biletado_backend.model;

import lombok.Data;

import java.util.Date;

@Data
public class Reservation {
    String id;
    Date from;
    Date to;
    String room_id;
}

/*
 "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
 "from": "2023-01-06",
 "to": "2023-01-06",
 "room_id": "3fa85f64-5717-4562-b3fc-2c963f66afa6"
 */
