package ru.practicum.shareIt.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ItemServiceImpl service;

    @Test
    void createItem_shouldReturnCreatedItem() {

        User user = new User();
        user.setId(1L);

        Item item = new Item();
        item.setId(10L);
        item.setName("Drill");
        item.setAvailable(true);

        ItemDto dto = new ItemDto();
        dto.setName("Drill");
        dto.setDescription("desc");
        dto.setAvailable(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto result = service.createItem(1L, dto);

        assertEquals("Drill", result.getName());
    }

    @Test
    void getItem_shouldReturnItem_whenNotOwner() {

        Item item = new Item();
        item.setId(10L);

        when(itemRepository.findById(10L)).thenReturn(Optional.of(item));
        when(commentRepository.findByItemId(10L)).thenReturn(List.of());

        ItemDto result = service.getItem(10L, 2L);

        assertNotNull(result);
    }

    @Test
    void getAllItems_shouldReturnList() {

        Item item = new Item();
        item.setId(1L);
        item.setName("Drill");

        when(itemRepository.findByOwnerId(1L)).thenReturn(List.of(item));
        when(commentRepository.findByItemIdIn(any())).thenReturn(List.of());
        when(bookingRepository.findByItemIdInAndStatusOrderByStartDesc(any(), any()))
                .thenReturn(List.of());

        List<ItemDto> result = service.getAllItems(1L);

        assertEquals(1, result.size());
    }

    @Test
    void searchItems_shouldReturnResults() {

        Item item = new Item();
        item.setId(1L);
        item.setName("Drill");

        when(itemRepository.search("drill")).thenReturn(List.of(item));

        List<ItemDto> result = service.searchItems("drill");

        assertEquals(1, result.size());
    }

    @Test
    void addComment_shouldCreateComment_whenUserHadBooking() {

        User user = new User();
        user.setId(1L);

        Item item = new Item();
        item.setId(10L);

        Comment comment = new Comment();
        comment.setId(100L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(10L)).thenReturn(Optional.of(item));

        // пользователь имеет завершённое бронирование
        ru.practicum.shareit.booking.Booking booking =
                new ru.practicum.shareit.booking.Booking();
        booking.setItem(item);
        booking.setStatus(ru.practicum.shareit.booking.BookingStatus.APPROVED);
        booking.setEnd(java.time.LocalDateTime.now().minusDays(1));

        when(bookingRepository.findByBookerIdOrderByStartDesc(1L))
                .thenReturn(List.of(booking));

        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        ru.practicum.shareit.comment.dto.CommentCreateDto dto =
                new ru.practicum.shareit.comment.dto.CommentCreateDto("text");

        var result = service.addComment(1L, 10L, dto);

        assertNotNull(result);
    }
}
