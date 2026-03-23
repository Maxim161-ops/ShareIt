package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestBody @Valid ItemDto itemDto) {
        log.info("POST /items - создание вещи пользователем id={}", userId);
        return itemService.createItem(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable("itemId") Long itemId) {
        log.info("GET /items/{} - получение вещи", itemId);
        return itemService.getItem(itemId);
    }

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("GET /items - получение всех вещей пользователя id={}", userId);
        return itemService.getAllItems(userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId,
                              @RequestBody ItemDto itemDto) {
        log.info("PATCH /items/{} - обновление вещи пользователем id={}", itemId, userId);
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        log.info("GET /items/search?text={} - поиск вещей", text);
        return itemService.searchItems(text);
    }
}
