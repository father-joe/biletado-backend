package edu.dhbw_ka.webeng.biletado_backend.rest;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.text.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.*;
import org.springframework.security.test.context.support.*;
import org.springframework.test.context.*;
import org.springframework.test.web.servlet.*;

import edu.dhbw_ka.webeng.biletado_backend.model.*;
import edu.dhbw_ka.webeng.biletado_backend.service.*;

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
        mockMvc.perform(get("/reservations/")).andDo(print()).andExpect(status().isOk())
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
