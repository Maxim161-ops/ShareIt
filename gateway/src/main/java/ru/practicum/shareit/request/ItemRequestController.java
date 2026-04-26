package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;


@Slf4j
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody @Valid ItemRequestDto dto) {

        log.info("POST /requests userId={}", userId);
        return itemRequestClient.createRequest(userId, dto);
    }

    @GetMapping
    public ResponseEntity<Object> getUserRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId) {

        log.info("GET /requests userId={}", userId);
        return itemRequestClient.getUserRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {

        log.info("GET /requests/all userId={}, from={}, size={}", userId, from, size);
        return itemRequestClient.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long requestId) {

        log.info("GET /requests/{}", requestId);
        return itemRequestClient.getRequestById(userId, requestId);
    }
}
