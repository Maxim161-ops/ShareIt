package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        if (item == null) return null;

        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getOwner(),
                item.getAvailable(),
                item.getRequest()
        );
    }


    public static Item toItem(ItemDto itemDto) {
        if (itemDto == null) return null;

        return new Item(
                null,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getOwner(),
                itemDto.getRequest()
        );
    }
}
