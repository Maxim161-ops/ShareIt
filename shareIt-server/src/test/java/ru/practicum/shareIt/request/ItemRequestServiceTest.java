package ru.practicum.shareIt.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyList;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemRequestServiceImpl service;

    @Test
    void getUserRequests_shouldReturnList() {

        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        ItemRequest request = new ItemRequest();
        request.setId(1L);
        request.setRequester(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(requestRepository.findByRequesterIdOrderByCreatedDesc(userId))
                .thenReturn(List.of(request));
        when(itemRepository.findByRequestIdIn(anyList()))
                .thenReturn(List.of());

        List<ItemRequestDto> result = service.getUserRequests(userId);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void getRequestById_shouldReturnRequest() {

        Long userId = 1L;
        Long requestId = 1L;

        User user = new User();
        user.setId(userId);

        ItemRequest request = new ItemRequest();
        request.setId(requestId);
        request.setDescription("Need drill");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(requestRepository.findById(requestId)).thenReturn(Optional.of(request));
        when(itemRepository.findByRequestId(requestId)).thenReturn(List.of());

        ItemRequestDto result = service.getRequestById(userId, requestId);

        assertEquals(requestId, result.getId());
        assertEquals("Need drill", result.getDescription());
    }
}
