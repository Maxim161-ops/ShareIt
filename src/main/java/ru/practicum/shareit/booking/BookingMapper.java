package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        Long itemId = booking.getItem() != null ? booking.getItem().getId() : null;
        Long bookerId = booking.getBooker() != null ? booking.getBooker().getId() : null;

        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                itemId,
                bookerId,
                booking.getStatus()
        );
    }

    public static Booking toBooking(BookingDto dto, Item item, User booker) {
        if (dto == null) return null;

        return new Booking(
                dto.getId(),
                dto.getStart(),
                dto.getEnd(),
                item,
                booker,
                dto.getStatus()
        );
    }
}