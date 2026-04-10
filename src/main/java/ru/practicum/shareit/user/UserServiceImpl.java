package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailAlreadyExistsException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        log.info("Попытка создать пользователя с email={}", userDto.getEmail());

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new EmailAlreadyExistsException("Email уже существует");
        }

        User user = UserMapper.toUser(userDto);
        User savedUser = userRepository.save(user);

        log.info("Пользователь создан с id={}", savedUser.getId());

        return UserMapper.toUserDto(savedUser);
    }

    @Override
    public UserDto getUser(Long id) {
        log.info("Запрос на получение пользователя с id={}", id);

        User user = getUserOrThrow(id);

        log.info("Пользователь найден: id={}", id);

        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.info("Запрос на получение всех пользователей");

        List<UserDto> users = userRepository.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .toList();

        log.info("Получено пользователей: {}", users.size());

        return users;
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        log.info("Обновление пользователя с id={}", id);

        User user = getUserOrThrow(id);

        if (userDto.getName() != null && !userDto.getName().isBlank()) {
            log.debug("Обновление имени пользователя id={}", id);
            user.setName(userDto.getName());
        }

        if (userDto.getEmail() != null
                && !userDto.getEmail().isBlank()
                && userDto.getEmail().contains("@")) {

            log.debug("Проверка email на уникальность: {}", userDto.getEmail());

            boolean emailExists = userRepository.findAll().stream()
                    .anyMatch(u -> !u.getId().equals(id)
                            && u.getEmail().equals(userDto.getEmail()));

            if (emailExists) {
                log.warn("Попытка установить существующий email: {}", userDto.getEmail());
                throw new EmailAlreadyExistsException("Email уже существует");
            }

            user.setEmail(userDto.getEmail());
        }

        User savedUser = userRepository.save(user);

        log.info("Пользователь обновлён: id={}", id);

        return UserMapper.toUserDto(savedUser);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Удаление пользователя с id={}", id);

        getUserOrThrow(id);

        userRepository.deleteById(id);

        log.info("Пользователь удалён: id={}", id);
    }


    private User getUserOrThrow(Long id) {
        log.debug("Поиск пользователя с id={}", id);

        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Пользователь не найден: id={}", id);
                    return new UserNotFoundException("Пользователь не найден с id " + id);
                });
    }
}
