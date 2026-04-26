package ru.practicum.shareIt.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;



class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private BookingServiceImpl service;

    @Test
    void createBooking_success() {

        Long userId = 1L;
        Long itemId = 2L;

        User user = new User();
        user.setId(userId);

        User owner = new User();
        owner.setId(99L);

        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner);

        BookingCreateDto dto = new BookingCreateDto(
                itemId,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        Booking booking = new Booking();
        booking.setId(10L);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(dto.getStart());
        booking.setEnd(dto.getEnd());
        booking.setStatus(BookingStatus.WAITING);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingDto result = service.createBooking(userId, dto);

        assertNotNull(result);
        assertEquals(10L, result.getId());
    }

    @Test
    void createBooking_shouldFail_whenOwnerBooksOwnItem() {

        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        Item item = new Item();
        item.setId(2L);
        item.setAvailable(true);

        User owner = new User();
        owner.setId(userId); // тот же пользователь

        item.setOwner(owner);

        BookingCreateDto dto = new BookingCreateDto(
                2L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item));

        assertThrows(AccessDeniedException.class,
                () -> service.createBooking(userId, dto));
    }

    @Test
    void getUserBookings_shouldReturnList() {

        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        Booking booking = new Booking();
        booking.setId(1L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findByBookerIdOrderByStartDesc(userId))
                .thenReturn(List.of(booking));

        List<BookingDto> result = service.getUserBookings(userId, "ALL");

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void getBooking_shouldReturnBooking() {

        Long userId = 1L;
        Long bookingId = 1L;

        User user = new User();
        user.setId(userId);

        User owner = new User();
        owner.setId(2L);

        Item item = new Item();
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBooker(user);
        booking.setItem(item);

        when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.of(booking));

        BookingDto result = service.getBooking(userId, bookingId);

        assertEquals(bookingId, result.getId());
    }
}
