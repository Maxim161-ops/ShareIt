package ru.practicum.shareit.request;

import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;


import java.time.LocalDateTime;
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

    public static ItemRequest toEntity(ItemRequestDto dto, User user) {
        ItemRequest request = new ItemRequest();
        request.setDescription(dto.getDescription());
        request.setRequester(user);
        request.setCreated(LocalDateTime.now());
        return request;
    }
}
