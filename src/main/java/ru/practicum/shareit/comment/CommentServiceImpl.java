package ru.practicum.shareit.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final Map<Long, Comment> comments = new ConcurrentHashMap<>();
    private long nextId = 1;

    private final BookingService bookingService;
    private final ItemService itemService;
    private final UserService userService;

    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        Item item = itemService.findItem(itemId);

        // Проверка, что пользователь арендовал эту вещь и бронирование завершено
        boolean hadBooking = bookingService.getUserBookings(userId).stream()
                .anyMatch(b -> b.getItemId().equals(itemId) && b.getStatus() == BookingStatus.APPROVED && b.getEnd().isBefore(java.time.LocalDateTime.now()));

        if (!hadBooking) {
            throw new RuntimeException("Оставить отзыв можно только после завершенного бронирования");
        }

        User user = userService.findUser(userId);
        Comment comment = CommentMapper.toComment(commentDto, itemId, user.getName());
        comment.setId(nextId++);
        comments.put(comment.getId(), comment);

        return CommentMapper.toDto(comment);
    }

    @Override
    public List<CommentDto> getCommentsByItem(Long itemId) {
        return comments.values().stream()
                .filter(c -> c.getItemId().equals(itemId))
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
    }
}
