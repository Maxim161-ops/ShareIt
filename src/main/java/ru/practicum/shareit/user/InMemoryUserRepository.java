package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class InMemoryUserRepository implements UserRepository{
    private final Map<Long, User> users = new HashMap<>();
    private long nextId = 1;

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(nextId++);
            log.info("Создан новый пользователь с id={}", user.getId());
        } else {
            log.info("Обновление пользователя с id={}", user.getId());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        User user = users.get(id);

        if (user != null) {
            log.info("Найден пользователь с id={}", id);
        } else {
            log.warn("Пользователь с id={} не найден", id);
        }

        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findAll() {
        log.info("Возвращено {} пользователей", users.size());
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteById(Long id) {
        if (users.containsKey(id)) {
            users.remove(id);
            log.info("Пользователь с id={} был удален", id);
        } else {
            log.warn("Попытка удалить несуществующего пользователя с id={}", id);
        }
    }
}
