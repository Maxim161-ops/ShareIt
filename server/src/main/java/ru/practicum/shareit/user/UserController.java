package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserDto dto) {
        log.info("Получен запрос на создание пользователя: {}", dto);
        UserDto result = userService.createUser(dto);
        log.info("Пользователь успешно создан с id={}", result.getId());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getItem(@PathVariable Long id) {
        log.info("Получен запрос на получение пользователя с id={}", id);
        UserDto result = userService.getUser(id);
        log.info("Пользователь с id={} успешно получен", id);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        log.info("Получен запрос на получение всех пользователей");
        List<UserDto> result = userService.getAllUsers();
        log.info("Получено пользователей: {}", result.size());
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id,
                                          @RequestBody UserDto dto) {
        log.info("Получен запрос на обновление пользователя с id={}", id);
        UserDto result = userService.updateUser(id, dto);
        log.info("Пользователь с id={} успешно обновлён", id);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Получен запрос на удаление пользователя с id={}", id);
        userService.deleteUser(id);
        log.info("Пользователь с id={} успешно удалён", id);
        return ResponseEntity.noContent().build();
    }
}
