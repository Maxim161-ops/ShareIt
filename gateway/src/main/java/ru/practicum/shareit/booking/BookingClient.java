package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {

    private static final String API_PREFIX = "/bookings";

    public BookingClient(@Value("${shareit-server.url}") String serverUrl,
                         RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .build()
        );
    }

    public ResponseEntity<Object> createBooking(Long userId, BookingCreateDto dto) {
        return post("", userId, dto);
    }

    public ResponseEntity<Object> approveBooking(Long userId, Long bookingId, Boolean approved) {
        return patch("/" + bookingId + "?approved=" + approved, userId, null);
    }

    public ResponseEntity<Object> getBooking(Long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getUserBookings(Long userId, String state) {
        return get("?state={state}", userId, Map.of("state", state));
    }

    public ResponseEntity<Object> getOwnerBookings(Long userId, String state) {
        return get("owner?state={state}", userId, Map.of("state", state));
    }
}
