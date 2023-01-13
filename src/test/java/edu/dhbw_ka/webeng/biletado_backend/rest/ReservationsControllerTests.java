package edu.dhbw_ka.webeng.biletado_backend.rest;

import edu.dhbw_ka.webeng.biletado_backend.model.Room;
import edu.dhbw_ka.webeng.biletado_backend.service.RoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReservationsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    RoomService mockRoomService;
    
    @Test
    void getReservations_shouldListAllWhenNoFilter() throws Exception {
        mockMvc.perform(get("/reservations/")).andDo(print())
                .andExpect(status().isOk())
                // expect array of size 2 from data.sql
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @Test
    void getReservations_shouldFilterByRoomId() throws Exception {
        mockMvc.perform(get("/reservations/?room_id=00000000-0000-0000-0000-000000000001")).andDo(print())
                .andExpect(status().isOk())
                // expect single from data.sql
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].id", is("00000000-0000-0000-0000-000000000000")));
    }
    
    @Test
    @WithMockUser(username="authenticated user")
    void createReservation_shouldFailOnConflict() throws Exception {

        String requestBody = """
                {
                  "id": "00000000-0000-0000-0000-000000000004",
                  "from": "2023-01-01",
                  "to": "2023-01-03",
                  "room_id": "00000000-0000-0000-0000-000000000001"
                }
                """;

        when(mockRoomService.getRoom(any())).thenReturn(new Room());
        String message = mockMvc.perform(post("/reservations/").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andDo(print())
                .andExpect(status().isConflict())
                .andReturn()
                .getResolvedException().getMessage();
        assertThat(message, containsString("found conflicting reservation(s)"));
    }
}
