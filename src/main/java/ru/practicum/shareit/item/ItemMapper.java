package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public class ItemMapper {

    public static ItemDto toItemDto(Item item, BookingShortDto lastBooking, BookingShortDto nextBooking, List<CommentDto> comments) {
        if (item == null) return null;

        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getOwner().getId(),
                item.getAvailable(),
                item.getRequestId(),
                lastBooking,
                nextBooking,
                comments
        );
    }

    public static ItemDto toItemDto(Item item) {
        if (item == null) return null;

        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getOwner().getId(),
                item.getAvailable(),
                item.getRequestId(),
                null,
                null,
                List.of()
        );
    }


    public static Item toItem(ItemDto itemDto) {
        if (itemDto == null) return null;

        return new Item(
                null,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                null,
                itemDto.getRequestId()
        );
    }
}
