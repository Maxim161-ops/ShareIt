package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

@Slf4j
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDto> create(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody BookingCreateDto dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingService.createBooking(userId, dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookingDto> approve(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long id,
            @RequestParam Boolean approved) {

        return ResponseEntity.ok(
                bookingService.approveBooking(userId, id, approved)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> get(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long id) {

        return ResponseEntity.ok(
                bookingService.getBooking(userId, id)
        );
    }

    @GetMapping
    public ResponseEntity<Object> getUserBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(defaultValue = "ALL") String state) {
        log.info("Получение бронирований пользователя id={}, state={}", userId, state);
        return ResponseEntity.ok(bookingService.getUserBookings(userId, state));
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(defaultValue = "ALL") String state) {
        log.info("Получение бронирований владельца id={}, state={}", userId, state);
        return ResponseEntity.ok(bookingService.getOwnerBookings(userId, state));
    }
}
