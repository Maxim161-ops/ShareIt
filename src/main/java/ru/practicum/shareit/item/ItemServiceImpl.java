package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.exception.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        log.info("Создание предмета пользователем id={}", userId);

        User user = getUserOrThrow(userId);

        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user);

        Item savedItem = itemRepository.save(item);
        log.info("Предмет создан id={} пользователем id={}", savedItem.getId(), userId);

        return ItemMapper.toItemDto(savedItem);
    }

    @Override
    public ItemDto getItem(Long itemId, Long userId) {
        log.info("Получение предмета id={} пользователем id={}", itemId, userId);

        Item item = getItemOrThrow(itemId);

        List<CommentDto> comments = commentRepository.findByItemId(itemId)
                .stream()
                .map(CommentMapper::toDto)
                .toList();

        log.debug("Найдено комментариев: {} для itemId={}", comments.size(), itemId);

        BookingShortDto lastBookingDto = null;
        BookingShortDto nextBookingDto = null;

        if (item.getOwner().getId().equals(userId)) {
            log.debug("Пользователь является владельцем itemId={}", itemId);
            List<Booking> bookings = bookingRepository.findByItemId(itemId);
            LocalDateTime now = LocalDateTime.now();

            Booking lastBooking = bookings.stream()
                    .filter(b -> b.getEnd().isBefore(now))
                    .max(Comparator.comparing(Booking::getEnd))
                    .orElse(null);

            Booking nextBooking = bookings.stream()
                    .filter(b -> b.getStart().isAfter(now))
                    .min(Comparator.comparing(Booking::getStart))
                    .orElse(null);

            lastBookingDto = BookingMapper.toShortDto(lastBooking);
            nextBookingDto = BookingMapper.toShortDto(nextBooking);
        }
        log.debug("Последний и следующий брони получены для itemId={}", itemId);

        return ItemMapper.toItemDto(
                item,
                lastBookingDto,
                nextBookingDto,
                comments
        );
    }


    @Override
    public List<ItemDto> getAllItems(Long userId) {
        log.info("Получение всех предметов пользователя id={}", userId);

        List<Item> items = itemRepository.findByOwnerId(userId);
        log.debug("Найдено предметов: {}", items.size());

        if (items.isEmpty()) {
            return List.of();
        }

        List<Long> itemIds = items.stream()
                .map(Item::getId)
                .toList();

        List<Comment> comments = commentRepository.findByItemIdIn(itemIds);

        Map<Long, List<CommentDto>> commentsMap = comments.stream()
                .collect(Collectors.groupingBy(
                        comment -> comment.getItem().getId(),
                        Collectors.mapping(CommentMapper::toDto, Collectors.toList())
                ));

        List<Booking> bookings = bookingRepository
                .findByItemIdInAndStatusOrderByStartDesc(itemIds, BookingStatus.APPROVED);

        Map<Long, List<Booking>> bookingsMap = bookings.stream()
                .collect(Collectors.groupingBy(b -> b.getItem().getId()));

        LocalDateTime now = LocalDateTime.now();

        return items.stream()
                .map(item -> {
                    ItemDto dto = ItemMapper.toItemDto(item);

                    dto.setComments(commentsMap.getOrDefault(item.getId(), List.of()));

                    List<Booking> itemBookings = bookingsMap.get(item.getId());

                    if (itemBookings != null && !itemBookings.isEmpty()) {

                        Booking last = itemBookings.stream()
                                .filter(b -> b.getStart().isBefore(now))
                                .max(Comparator.comparing(Booking::getStart))
                                .orElse(null);

                        Booking next = itemBookings.stream()
                                .filter(b -> b.getStart().isAfter(now))
                                .min(Comparator.comparing(Booking::getStart))
                                .orElse(null);

                        dto.setLastBooking(
                                last != null ? BookingMapper.toShortDto(last) : null
                        );

                        dto.setNextBooking(
                                next != null ? BookingMapper.toShortDto(next) : null
                        );
                    }

                    return dto;
                })
                .toList();
    }

        @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        log.info("Обновление предмета id={} пользователем id={}", itemId, userId);
        Item item = getItemOrThrow(itemId);

        if (!item.getOwner().getId().equals(userId)) {
            log.warn("Попытка неавторизованного обновления itemId={} пользователем id={}", itemId, userId);
            throw new AccessDeniedException("Редактировать может только владелец");
        }
        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            item.setName(itemDto.getName().trim());
            log.debug("Обновлено имя itemId={}", itemId);
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            item.setDescription(itemDto.getDescription().trim());
            log.debug("Обновлено описание itemId={}", itemId);
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
            log.debug("Обновлен доступ itemId={}", itemId);
        }

        itemRepository.save(item);
        log.info("Предмет успешно обновлён id={}", itemId);

        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        log.info("Поиск предметов по тексту: {}", text);

        if (text == null || text.isBlank()) {
            log.info("Пустой запрос поиска — возвращаем пустой список");
            return List.of();
        }

        List<ItemDto> result = itemRepository.search(text).stream()
                .map(ItemMapper::toItemDto)
                .toList();

        log.info("Найдено предметов по запросу '{}': {}", text, result.size());

        return result;
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentCreateDto dto) {
        log.info("Добавление комментария пользователем id={} к itemId={}", userId, itemId);

        User user = getUserOrThrow(userId);
        Item item = getItemOrThrow(itemId);

        boolean hasBooking = bookingRepository.findByBookerIdOrderByStartDesc(userId)
                .stream()
                .anyMatch(b ->
                        b.getItem().getId().equals(itemId) &&
                                b.getStatus() == BookingStatus.APPROVED &&
                                b.getEnd().isBefore(LocalDateTime.now())
                );
        if (!hasBooking) {
            log.warn("Пользователь id={} не имеет права комментировать itemId={}", userId, itemId);
            throw new IllegalStateException("Пользователь не брал эту вещь в аренду");
        }

        Comment comment = CommentMapper.toComment(dto, item, user);
        Comment saved = commentRepository.save(comment);
        log.info("Комментарий добавлен id={} к itemId={}", saved.getId(), itemId);

        return CommentMapper.toDto(saved);
    }

    private Item getItemOrThrow(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Предмет с id " + itemId + "не найден"));
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + userId + "не найден"));
    }
}