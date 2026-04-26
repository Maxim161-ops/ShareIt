package ru.practicum.shareIt.user;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceTest {

    @Autowired
    private UserServiceImpl userService;

    @Test
    void createUser_shouldSaveToDb_andReturnUser() {

        UserDto created = userService.createUser(
                new UserDto(null, "Max", "max@test.com")
        );

        assertNotNull(created.getId());
        assertEquals("Max", created.getName());
        assertEquals("max@test.com", created.getEmail());

        UserDto fromDb = userService.getUser(created.getId());

        assertEquals(created.getId(), fromDb.getId());
        assertEquals("Max", fromDb.getName());
    }
}
