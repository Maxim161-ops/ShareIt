package ru.practicum.shareIt.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserClient;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        UserDto input = new UserDto(null, "John", "john@mail.com");

        when(userClient.createUser(any()))
                .thenReturn(org.springframework.http.ResponseEntity.status(201).body(input));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated());
    }

    @Test
    void getUser_shouldReturnOk() throws Exception {
        UserDto dto = new UserDto(1L, "John", "john@mail.com");

        when(userClient.getUser(1L))
                .thenReturn(org.springframework.http.ResponseEntity.ok(dto));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllUsers_shouldReturnOk() throws Exception {

        when(userClient.getAllUsers())
                .thenReturn(org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser_shouldReturnNoContent() throws Exception {

        when(userClient.deleteUser(1L))
                .thenReturn(org.springframework.http.ResponseEntity.noContent().build());

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }
}
