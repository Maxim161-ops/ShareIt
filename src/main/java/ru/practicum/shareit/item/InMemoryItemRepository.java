package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Slf4j
@Repository
public class InMemoryItemRepository implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();
    private long nextId = 1;

    @Override
    public Item save(Item item) {
        if (item.getId() == null) {
            item.setId(nextId++);
            log.info("Создание нового item с id={}", item.getId());
        } else {
            log.info("Обновление item с id={}", item.getId());
        }
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Optional<Item> findById(Long id) {
        Item item = items.get(id);

        if (item == null) {
            log.warn("Item с id={} не найден", id);
        } else {
            log.info("Получен item с id={}", id);
        }
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public List<Item> findAll() {
        log.info("Получение всех items, количество={}", items.size());
        return new ArrayList<>(items.values());
    }
}
