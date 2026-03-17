package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.exception.AccessDeniedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final UserService userService;

    private final Map<Long, Item> items = new HashMap<>();
    private long nextId = 1;

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        userService.findUser(userId);

        Item item = ItemMapper.toItem(itemDto);
        if (item.getAvailable() == null) {
            item.setAvailable(true);
        }

        item.setOwner(userId);
        item.setId(nextId);
        nextId++;
        items.put(item.getId(), item);

        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto getItem(Long itemId) {
        Item item = items.get(itemId);

        if (item == null) {
            throw new ItemNotFoundException("Предмет не найден");
        }

        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getAllItems(Long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().equals(userId))
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        Item item = items.get(itemId);

        if (item == null) {
            throw new ItemNotFoundException("Предмет не найден");
        }
        if (!item.getOwner().equals(userId)) {
            throw new AccessDeniedException("Редактировать может только владелец");
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        return ItemMapper.toItemDto(item);
    }

    @Override
    public Item findItem(Long id) {
        Item item = items.get(id);
        if (item == null) {
            throw new ItemNotFoundException("Предмет не найден");
        }

        return item;
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(item ->
                        item.getName().toLowerCase().contains(text.toLowerCase()) ||
                                item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .map(ItemMapper::toItemDto)
                .toList();
    }
}
