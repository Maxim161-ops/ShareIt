package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDto> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @RequestBody ItemDto dto) {

        log.info("Запрос на создание вещи: userId={}, данные={}", userId, dto);

        ItemDto result = itemService.createItem(userId, dto);

        log.info("Вещь успешно создана: id={}, ownerId={}, name={}",
                result.getId(),
                userId,
                result.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @PathVariable Long id) {
        log.info("Получение вещи id={} пользователем id={}", id, userId);
        return ResponseEntity.ok(itemService.getItem(id, userId));
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получение всех вещей пользователя id={}", userId);
        return ResponseEntity.ok(itemService.getAllItems(userId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemDto> update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable Long id,
                                          @RequestBody ItemDto dto) {
        log.info("Обновление вещи id={} пользователем id={}", id, userId);
        return ResponseEntity.ok(itemService.updateItem(userId, id, dto));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> search(@RequestParam String text) {
        log.info("Поиск вещей по тексту: '{}'", text);
        return ResponseEntity.ok(itemService.searchItems(text));
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Object> addComment(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long id,
            @RequestBody CommentCreateDto dto) {

        log.info("Добавление комментария к вещи id={} пользователем id={}", id, userId);

        return ResponseEntity.ok(itemService.addComment(userId, id, dto));
    }
}
