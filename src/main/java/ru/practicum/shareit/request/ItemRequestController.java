package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;
    private final UserService userService;

    @PostMapping
    public ItemRequestDto createRequest(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody @Valid ItemRequestDto requestDto) {

        ItemRequest request = ItemRequestMapper.toItemRequest(requestDto, userId);
        request = itemRequestService.create(request);
        return ItemRequestMapper.toDto(request);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody @Valid UserDto userDto) {
        return userService.updateUser(id, userDto);
    }

    @GetMapping
    public List<ItemRequestDto> getUserRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId) {

        List<ItemRequest> requests = itemRequestService.getUserRequests(userId);
        return requests.stream()
                .map(ItemRequestMapper::toDto)
                .toList();
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getOtherRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId) {

        List<ItemRequest> requests = itemRequestService.getOtherRequests(userId);
        return requests.stream()
                .map(ItemRequestMapper::toDto)
                .toList();
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequest(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long requestId) {

        ItemRequest request = itemRequestService.getRequest(userId, requestId);
        return ItemRequestMapper.toDto(request);
    }
}
