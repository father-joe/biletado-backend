package edu.dhbw_ka.webeng.biletado_backend.service;

import edu.dhbw_ka.webeng.biletado_backend.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class RoomService {

    @Autowired
    RestTemplate restTemplate;

    public Room getRoom(UUID roomId) {
        String url = "http://localhost/api/assets/rooms/" + roomId + "/";
        return restTemplate.getForEntity(url, Room.class).getBody();
    }
}
