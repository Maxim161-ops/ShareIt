package ru.practicum.shareit.request;

import java.util.List;

public interface ItemRequestService {

    ItemRequest create(ItemRequest itemRequest);


    List<ItemRequest> getUserRequests(Long userId);


    List<ItemRequest> getOtherRequests(Long userId);


    ItemRequest getRequest(Long userId, Long requestId);
}

