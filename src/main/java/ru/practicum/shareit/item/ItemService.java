package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDto createItem(Long userId, ItemDto itemDto);

    ItemDto getItem(Long itemId);

    List<ItemDto> getAllItems(Long userId);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    Item findItem(Long id);

    List<ItemDto> searchItems(String text);
}