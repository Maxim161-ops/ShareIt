package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailAlreadyExistsException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Map<Long, User> users = new HashMap<>();
    private long nextId = 1;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);

        boolean emailExists = users.values().stream()
                .anyMatch(u -> u.getEmail().equals(user.getEmail()));
        if (emailExists) {
            throw new EmailAlreadyExistsException("Email уже существует");
        }

        user.setId(nextId++);

        users.put(user.getId(), user);

        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto getUser(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return users.values()
                .stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = users.get(id);
        if (user == null) {
            throw new UserNotFoundException("Пользователь не найден");
        }

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }

        if (userDto.getEmail() != null) {
            boolean emailExists = users.values().stream()
                    .anyMatch(u -> !u.getId().equals(id) && u.getEmail().equals(userDto.getEmail()));
            if (emailExists) {
                throw new EmailAlreadyExistsException("Email уже существует");
            }
            user.setEmail(userDto.getEmail());
        }

        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        users.remove(id);
    }

    @Override
    public User findUser(Long id) {
       User user = users.get(id);
        if (user == null) {
            throw new UserNotFoundException("Пользователь не найден");
        }

        return user;
    }
}
