package edu.dhbw_ka.webeng.biletado_backend.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Room {

    UUID id;
    String name;
    UUID storey_id;
}
