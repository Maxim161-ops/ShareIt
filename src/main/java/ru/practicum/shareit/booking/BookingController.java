package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody BookingDto bookingDto) {
        return bookingService.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId,
            @RequestParam Boolean approved) {
        return bookingService.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId) {
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getUserBookings(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getUserBookings(userId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getOwnerBookings(userId);
    }
}
