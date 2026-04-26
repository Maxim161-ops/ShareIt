package ru.practicum.shareIt.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.ItemRequestServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ItemRequestServiceTest {

    @Autowired
    private ItemRequestServiceImpl requestService;

    @Autowired
    private UserServiceImpl userService;

    @Test
    void getUserRequests_shouldReturnList_fromRealDb() {

        UserDto user = userService.createUser(
                new UserDto(null, "Max", "max@test.com")
        );

        ItemRequestDto created = requestService.create(
                user.getId(),
                new ItemRequestDto(null, "Need drill", null, null)
        );

        List<ItemRequestDto> result = requestService.getUserRequests(user.getId());

        assertEquals(1, result.size());
        assertEquals(created.getId(), result.get(0).getId());
    }

    @Test
    void getRequestById_shouldReturnRequest_fromRealDb() {

        UserDto user = userService.createUser(
                new UserDto(null, "Max", "max@test.com")
        );

        ItemRequestDto created = requestService.create(
                user.getId(),
                new ItemRequestDto(null, "Need drill", null, null)
        );

        ItemRequestDto result = requestService.getRequestById(
                user.getId(),
                created.getId()
        );

        assertEquals(created.getId(), result.getId());
        assertEquals("Need drill", result.getDescription());
    }
}
