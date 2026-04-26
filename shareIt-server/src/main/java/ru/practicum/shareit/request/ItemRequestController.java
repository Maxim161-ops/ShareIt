package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService requestService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestBody ItemRequestDto dto) {
        log.info("Создание запроса пользователем id={}", userId);
        return ResponseEntity.ok(requestService.create(userId, dto));
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestDto>> getUserRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId) {

        log.info("Получение запросов пользователя id={}", userId);

        return ResponseEntity.ok(
                requestService.getUserRequests(userId)
        );
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemRequestDto>> getAll(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam int from,
            @RequestParam int size) {

        log.info("Получение всех запросов: userId={}, from={}, size={}",
                userId, from, size);

        return ResponseEntity.ok(
                requestService.getRequests(userId, from, size)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemRequestDto> getRequestById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long id) {

        log.info("Получение запроса id={} пользователем id={}", id, userId);

        return ResponseEntity.ok(
                requestService.getRequestById(userId, id)
        );
    }
}
