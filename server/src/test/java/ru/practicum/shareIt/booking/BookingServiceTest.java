package ru.practicum.shareIt.booking;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BookingServiceTest {

    @Autowired
    private BookingServiceImpl service;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void createBooking_success() {

        User user = userRepository.save(new User(null, "User", "user@test.com"));

        User owner = userRepository.save(new User(null, "Owner", "owner@test.com"));

        Item item = new Item();
        item.setName("Drill");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(owner);

        item = itemRepository.save(item);

        BookingCreateDto dto = new BookingCreateDto(
                item.getId(),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        BookingDto result = service.createBooking(user.getId(), dto);

        assertNotNull(result.getId());
        assertEquals(BookingStatus.WAITING, result.getStatus());
    }

    @Test
    void createBooking_shouldFail_whenOwnerBooksOwnItem() {

        User owner = userRepository.save(new User(null, "Owner", "owner@test.com"));

        Item item = new Item();
        item.setName("Drill");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(owner);

        item = itemRepository.save(item);

        BookingCreateDto dto = new BookingCreateDto(
                item.getId(),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        assertThrows(AccessDeniedException.class,
                () -> service.createBooking(owner.getId(), dto));
    }

    @Test
    void getUserBookings_shouldReturnList() {

        User user = userRepository.save(new User(null, "User", "user@test.com"));
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
        booking.setStatus(BookingStatus.WAITING);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));

        bookingRepository.save(booking);

        List<BookingDto> result = service.getUserBookings(user.getId(), "ALL");

        assertEquals(1, result.size());
    }

    @Test
    void getBooking_shouldReturnBooking() {

        User user = userRepository.save(new User(null, "User", "user@test.com"));
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
        booking.setStatus(BookingStatus.WAITING);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));

        booking = bookingRepository.save(booking);

        BookingDto result = service.getBooking(user.getId(), booking.getId());

        assertEquals(booking.getId(), result.getId());
    }
}
