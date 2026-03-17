package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;


public class ItemRequestMapper {

    public static ItemRequestDto toDto(ItemRequest itemRequest) {
        if (itemRequest == null) return null;

        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated()
        );
    }

    public static ItemRequest toItemRequest(ItemRequestDto dto, Long requestorId) {
        if (dto == null) return null;

        return new ItemRequest(
                dto.getId(),
                dto.getDescription(),
                requestorId,
                dto.getCreated() != null ? dto.getCreated() : LocalDateTime.now()
        );
    }
}
