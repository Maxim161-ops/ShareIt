package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;


import java.util.List;

public class ItemMapper {

    public static ItemDto toItemDto(
            Item item,
            BookingShortDto lastBooking,
            BookingShortDto nextBooking,
            List<CommentDto> comments
    ) {
        if (item == null) return null;

        ItemDto dto = new ItemDto();

        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setRequestId(item.getRequest() != null ? item.getRequest().getId() : null);
        dto.setLastBooking(lastBooking);
        dto.setNextBooking(nextBooking);
        dto.setComments(comments);

        return dto;
    }

    public static ItemDto toItemDto(Item item) {
        return toItemDto(item, null, null, List.of());
    }

    public static ItemShortDto toShortDto(Item item) {
        if (item == null) return null;

        return new ItemShortDto(
                item.getId(),
                item.getName()
        );
    }

    public static Item toItem(ItemDto dto, User owner, ItemRequest request) {
        Item item = new Item();
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        item.setOwner(owner);
        item.setRequest(request);
        return item;
    }
}

