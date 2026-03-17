package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestBody @Valid ItemDto itemDto) {
        return itemService.createItem(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable("itemId") Long itemId) {
        return itemService.getItem(itemId);
    }

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAllItems(userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId,
                              @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        return itemService.searchItems(text);
    }
}
