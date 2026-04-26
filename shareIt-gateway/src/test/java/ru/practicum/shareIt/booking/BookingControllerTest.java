package ru.practicum.shareIt.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingCreateDto;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingClient bookingClient;


    @Test
    void createBooking_shouldReturnCreated() throws Exception {

        Long userId = 1L;

        BookingCreateDto dto = new BookingCreateDto(
                10L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        when(bookingClient.createBooking(eq(userId), any()))
                .thenReturn(org.springframework.http.ResponseEntity.status(201).build());

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void approveBooking_shouldReturnOk() throws Exception {

        Long ownerId = 1L;
        Long bookingId = 5L;

        when(bookingClient.approveBooking(ownerId, bookingId, true))
                .thenReturn(org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", ownerId)
                        .param("approved", "true"))
                .andExpect(status().isOk());
    }

    @Test
    void getBooking_shouldReturnOk() throws Exception {

        Long userId = 1L;
        Long bookingId = 5L;

        when(bookingClient.getBooking(userId, bookingId))
                .thenReturn(org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }

    @Test
    void getUserBookings_shouldReturnOk() throws Exception {

        Long userId = 1L;

        when(bookingClient.getUserBookings(userId, "ALL"))
                .thenReturn(org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "ALL"))
                .andExpect(status().isOk());
    }

    @Test
    void getOwnerBookings_shouldReturnOk() throws Exception {

        Long ownerId = 1L;

        when(bookingClient.getOwnerBookings(ownerId, "ALL"))
                .thenReturn(org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", ownerId)
                        .param("state", "ALL"))
                .andExpect(status().isOk());
    }
}
