package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDto create(Long userId, ItemRequestDto dto) {

        User user = getUser(userId);

        ItemRequest request = ItemRequestMapper.toEntity(dto, user);

        ItemRequest saved = requestRepository.save(request);

        List<ItemShortDto> items = itemRepository.findByRequestId(saved.getId())
                .stream()
                .map(ItemMapper::toShortDto)
                .toList();

        return ItemRequestMapper.toDto(saved, items);
    }

    @Override
    public List<ItemRequestDto> getUserRequests(Long userId) {

        getUser(userId);

        List<ItemRequest> requests =
                requestRepository.findByRequesterIdOrderByCreatedDesc(userId);

        return mapWithItems(requests);
    }

    @Override
    public List<ItemRequestDto> getRequests(Long userId, int from, int size) {

        getUser(userId);

        Pageable pageable = PageRequest.of(from / size, size);

        List<ItemRequest> requests =
                requestRepository.findAllByRequesterIdNotOrderByCreatedDesc(userId, pageable);

        return mapWithItems(requests);
    }

    @Override
    @Transactional
    public ItemRequestDto getRequestById(Long userId, Long requestId) {

        getUser(userId);

        ItemRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос не найден, id=" + requestId));

        List<ItemShortDto> items = itemRepository.findByRequestId(requestId)
                .stream()
                .map(ItemMapper::toShortDto)
                .toList();

        return ItemRequestMapper.toDto(request, items);
    }

    private List<ItemRequestDto> mapWithItems(List<ItemRequest> requests) {

        if (requests.isEmpty()) return List.of();

        List<Long> ids = requests.stream()
                .map(ItemRequest::getId)
                .toList();

        List<Item> items = itemRepository.findByRequestIdIn(ids);

        Map<Long, List<ItemShortDto>> map = items.stream()
                .collect(Collectors.groupingBy(
                        item -> item.getRequest().getId(),
                        Collectors.mapping(ItemMapper::toShortDto, Collectors.toList())
                ));

        return requests.stream()
                .map(r -> ItemRequestMapper.toDto(r, map.getOrDefault(r.getId(), List.of())))
                .toList();
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден, id=" + userId));
    }
}
