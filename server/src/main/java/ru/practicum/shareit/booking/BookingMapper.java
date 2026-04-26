package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserShortDto;

public class BookingMapper {

    public static BookingDto toDto(Booking booking) {
        if (booking == null) return null;

        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),

                new ItemShortDto(
                        booking.getItem().getId(),
                        booking.getItem().getName()
                ),

                new UserShortDto(
                        booking.getBooker().getId()
                ),

                booking.getStatus()
        );
    }

    public static Booking toBooking(BookingCreateDto dto, Item item, User user) {
        if (dto == null) return null;

        return new Booking(
                null,
                dto.getStart(),
                dto.getEnd(),
                item,
                user,
                BookingStatus.WAITING
        );
    }

    public static BookingShortDto toShortDto(Booking booking) {
        if (booking == null) return null;

        return new BookingShortDto(
                booking.getId(),
                booking.getBooker().getId(),
                booking.getStart(),
                booking.getEnd()
        );
    }
}
