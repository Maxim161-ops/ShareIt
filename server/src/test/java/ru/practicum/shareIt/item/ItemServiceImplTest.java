package ru.practicum.shareIt.item;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ItemServiceImplTest {

    @Autowired
    private ItemServiceImpl service;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ru.practicum.shareit.item.ItemRepository itemRepository;

    @Test
    void createItem_shouldSaveToDb() {

        User user = new User();
        user.setName("Max");
        user.setEmail("max@test.com");
        user = userRepository.save(user);

        ItemDto dto = new ItemDto();
        dto.setName("Drill");
        dto.setDescription("desc");
        dto.setAvailable(true);

        ItemDto result = service.createItem(user.getId(), dto);

        assertNotNull(result.getId());
        assertEquals("Drill", result.getName());
    }

    @Test
    void getItem_shouldReturnItem() {

        User user = userRepository.save(new User(null, "Max", "mail@test.com"));

        Item item = new Item();
        item.setName("Drill");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(user);

        item = itemRepository.save(item);

        ItemDto result = service.getItem(item.getId(), user.getId());

        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
    }

    @Test
    void getAllItems_shouldReturnList() {

        User user = userRepository.save(new User(null, "Max", "mail@test.com"));

        Item item = new Item();
        item.setName("Drill");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(user);

        itemRepository.save(item);

        List<ItemDto> result = service.getAllItems(user.getId());

        assertEquals(1, result.size());
    }

    @Test
    void searchItems_shouldReturnResults() {

        User user = userRepository.save(new User(null, "Max", "mail@test.com"));

        Item item = new Item();
        item.setName("Drill");
        item.setDescription("Power tool");
        item.setAvailable(true);
        item.setOwner(user);

        itemRepository.save(item);

        List<ItemDto> result = service.searchItems("drill");

        assertEquals(1, result.size());
    }

    @Test
    void addComment_shouldWork_whenBookingFinished() {

        User user = userRepository.save(new User(null, "Max", "mail@test.com"));

        User owner = userRepository.save(new User(null, "Owner", "owner@test.com"));

        Item item = new Item();
        item.setName("Drill");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(owner);

        item = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));

        bookingRepository.save(booking);

        var dto = new ru.practicum.shareit.comment.dto.CommentCreateDto("good");

        var result = service.addComment(user.getId(), item.getId(), dto);

        assertNotNull(result);
        assertEquals("good", result.getText());
    }
}
