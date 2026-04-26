package ru.practicum.shareit.request;

import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;


import java.util.List;

public class ItemRequestMapper {

    public static ItemRequestDto toDto(ItemRequest request, List<ItemShortDto> items) {

        return new ItemRequestDto(
                request.getId(),
                request.getDescription(),
                request.getCreated(),
                items == null ? List.of() : items
        );
    }
}
