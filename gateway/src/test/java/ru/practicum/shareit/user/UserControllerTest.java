package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserClient userClient;

    @Test
    void createUser_shouldReturnCreated() throws Exception {

        UserDto dto = new UserDto(null, "Max", "max@test.com");

        when(userClient.createUser(any()))
                .thenReturn(status(201).build());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void createUser_shouldFail_whenInvalidEmail() throws Exception {

        UserDto dto = new UserDto(null, "Max", "");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getUser_shouldReturnOk() throws Exception {

        Long userId = 1L;

        when(userClient.getUser(userId))
                .thenReturn(ok().build());

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk());
    }

    @Test
    void getAllUsers_shouldReturnOk() throws Exception {

        when(userClient.getAllUsers())
                .thenReturn(ok().build());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    void updateUser_shouldReturnOk() throws Exception {

        Long userId = 1L;

        UserDto dto = new UserDto(null, "Updated", "updated@test.com");

        when(userClient.updateUser(eq(userId), any()))
                .thenReturn(ok().build());

        mockMvc.perform(patch("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser_shouldReturnOk() throws Exception {

        Long userId = 1L;

        when(userClient.deleteUser(userId))
                .thenReturn(ok().build());

        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isOk());
    }
}
