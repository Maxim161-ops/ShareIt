package ru.practicum.shareIt.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_shouldReturnCreatedUser() {

        User user = new User();
        user.setId(1L);
        user.setName("Max");
        user.setEmail("max@mail.com");

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        UserDto result = userService.createUser(new UserDto(
                null,
                "Max",
                "max@mail.com"
        ));

        assertEquals("Max", result.getName());
        assertEquals("max@mail.com", result.getEmail());
    }
}
