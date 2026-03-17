package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final ItemService itemService;
    private final UserService userService;

    Map<Long, Booking> bookings = new HashMap<>();
    private long nextId = 1;

    public BookingDto createBooking(Long userId, BookingDto bookingDto) {

        User user = userService.findUser(userId);

        Item item = itemService.findItem(bookingDto.getItemId());
        if (!item.getAvailable()) {
            throw new RuntimeException("Вещь недоступна для бронирования");
        }
        if (item.getOwner().equals(userId)) {
            throw new RuntimeException("Нельзя бронировать свою вещь");
        }

        Booking booking = BookingMapper.toBooking(bookingDto, item, user);
        booking.setId(nextId++);

        booking.setStatus(BookingStatus.WAITING);
        bookings.put(booking.getId(), booking);

        return BookingMapper.toBookingDto(booking);
    }

    public BookingDto approveBooking(Long userId, Long bookingId, Boolean approved) {

        Booking booking = bookings.get(bookingId);

        if (booking == null) {
            throw new RuntimeException("Бронирование не найдено");
        }

        Item item = booking.getItem();

        if (!item.getOwner().equals(userId)) {
            throw new RuntimeException("Подтверждать может только владелец вещи");
        }
        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new RuntimeException("Бронирование уже обработано");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        return BookingMapper.toBookingDto(booking);
    }

    public BookingDto getBooking(Long userId, Long bookingId) {

        Booking booking = bookings.get(bookingId);

        if (booking == null) {
            throw new RuntimeException("Бронирование не найдено");
        }

        Item item = booking.getItem();
        User booker = booking.getBooker();

        if (!item.getOwner().equals(userId) && !userId.equals(booker.getId())) {
            throw new RuntimeException("Доступ запрещен");
        }

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getUserBookings(Long userId) {
        return bookings.values().stream()
                .filter(booking -> booking.getBooker().getId().equals(userId))
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    public List<BookingDto> getOwnerBookings(Long userId) {
        return bookings.values()
                .stream()
                .filter(booking -> booking.getItem().getOwner().equals(userId))
                .map(BookingMapper::toBookingDto)
                .toList();
    }
}
