package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final Map<Long, ItemRequest> requests = new HashMap<>();
    private long nextId = 1;

    @Override
    public ItemRequest create(ItemRequest itemRequest) {
        itemRequest.setId(nextId++);
        itemRequest.setCreated(itemRequest.getCreated() != null ? itemRequest.getCreated() : java.time.LocalDateTime.now());
        requests.put(itemRequest.getId(), itemRequest);
        return itemRequest;
    }

    @Override
    public List<ItemRequest> getUserRequests(Long userId) {
        List<ItemRequest> result = new ArrayList<>();
        for (ItemRequest request : requests.values()) {
            if (request.getRequestor().equals(userId)) {
                result.add(request);
            }
        }
        return result;
    }

    @Override
    public List<ItemRequest> getOtherRequests(Long userId) {
        List<ItemRequest> result = new ArrayList<>();
        for (ItemRequest request : requests.values()) {
            if (!request.getRequestor().equals(userId)) {
                result.add(request);
            }
        }
        return result;
    }

    @Override
    public ItemRequest getRequest(Long userId, Long requestId) {
        ItemRequest request = requests.get(requestId);
        if (request == null) {
            throw new RuntimeException("Запрос не найден");
        }
        return request;
    }
}
