package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.BookingNotFoundException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto createBooking(Long userId, BookingCreateDto dto) {
        log.info("Создание бронирования: userId={}, itemId={}, start={}, end={}",
                userId, dto.getItemId(), dto.getStart(), dto.getEnd());
        User user = getUserOrThrow(userId);

        Item item = getItemOrThrow(dto.getItemId());

        if (dto.getEnd().isBefore(dto.getStart()) || dto.getEnd().isEqual(dto.getStart())) {
            log.warn("Неверные даты бронирования: start={}, end={}", dto.getStart(), dto.getEnd());
            throw new IllegalArgumentException("Дата окончания должна быть позже даты начала");
        }

        if (!item.getAvailable()) {
            log.warn("Вещь недоступна для бронирования: itemId={}", item.getId());
            throw new IllegalStateException("Вещь недоступна для бронирования");
        }

        if (item.getOwner().getId().equals(userId)) {
            log.warn("Пользователь пытается забронировать свою вещь: userId={}, itemId={}", userId, item.getId());
            throw new AccessDeniedException("Нельзя бронировать свою вещь");
        }

        Booking booking = BookingMapper.toBooking(dto, item, user);
        Booking saved = bookingRepository.save(booking);
        log.info("Бронирование создано: bookingId={}", saved.getId());

        return BookingMapper.toDto(saved);
    }

    @Override
    public BookingDto approveBooking(Long ownerId, Long bookingId, Boolean approved) {
        log.info("Подтверждение бронирования: ownerId={}, bookingId={}, approved={}", ownerId, bookingId, approved);

        Booking booking = getBookingOrThrow(bookingId);

        if (!booking.getItem().getOwner().getId().equals(ownerId)) {
            log.warn("Пользователь не является владельцем вещи: ownerId={}, bookingId={}", ownerId, bookingId);
            throw new AccessDeniedException("Только владелец может подтверждать бронирование");
        }

        if (booking.getStatus() != BookingStatus.WAITING) {
            log.warn("Бронирование уже обработано: bookingId={}, status={}", bookingId, booking.getStatus());
            throw new IllegalStateException("Бронирование уже подтверждено или отклонено");
        }

        if (approved == null) {
            log.warn("Не указан параметр approved для бронирования: bookingId={}", bookingId);
            throw new IllegalArgumentException("Параметр approved обязателен");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        Booking saved = bookingRepository.save(booking);
        log.info("Бронирование обновлено: bookingId={}, status={}", saved.getId(), saved.getStatus());

        return BookingMapper.toDto(saved);
    }

    @Override
    public BookingDto getBooking(Long userId, Long bookingId) {
        log.info("Получение бронирования: userId={}, bookingId={}", userId, bookingId);

        Booking booking = getBookingOrThrow(bookingId);

        if (!booking.getBooker().getId().equals(userId) &&
                !booking.getItem().getOwner().getId().equals(userId)) {
            log.warn("Нет доступа к бронированию: userId={}, bookingId={}", userId, bookingId);
            throw new AccessDeniedException("Нет доступа к бронированию");
        }

        return BookingMapper.toDto(booking);
    }

    @Override
    public List<BookingDto> getUserBookings(Long userId, String stateStr) {
        log.info("Получение бронирований пользователя: userId={}, state={}", userId, stateStr);

        getUserOrThrow(userId);
        BookingState state = BookingState.from(stateStr);
        LocalDateTime now = LocalDateTime.now();

        List<Booking> bookings;

        switch (state) {
            case CURRENT:
                bookings = bookingRepository
                        .findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, now, now);
                break;
            case PAST:
                bookings = bookingRepository
                        .findByBookerIdAndEndBeforeOrderByStartDesc(userId, now);
                break;
            case FUTURE:
                bookings = bookingRepository
                        .findByBookerIdAndStartAfterOrderByStartDesc(userId, now);
                break;
            case WAITING:
                bookings = bookingRepository
                        .findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository
                        .findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
                break;
            case ALL:
            default:
                bookings = bookingRepository
                        .findByBookerIdOrderByStartDesc(userId);
        }

        List<BookingDto> result = bookings.stream()
                .map(BookingMapper::toDto)
                .toList();

        log.info("Найдено бронирований: userId={}, state={}, count={}", userId, state, result.size());

        return result;
    }

    @Override
    public List<BookingDto> getOwnerBookings(Long ownerId, String stateStr) {
        log.info("Получение бронирований владельца: ownerId={}, state={}", ownerId, stateStr);

        getUserOrThrow(ownerId);
        BookingState state = BookingState.from(stateStr);
        LocalDateTime now = LocalDateTime.now();

        List<Booking> bookings;

        switch (state) {
            case CURRENT:
                bookings = bookingRepository
                        .findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(ownerId, now, now);
                break;
            case PAST:
                bookings = bookingRepository
                        .findByItemOwnerIdAndEndBeforeOrderByStartDesc(ownerId, now);
                break;
            case FUTURE:
                bookings = bookingRepository
                        .findByItemOwnerIdAndStartAfterOrderByStartDesc(ownerId, now);
                break;
            case WAITING:
                bookings = bookingRepository
                        .findByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository
                        .findByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.REJECTED);
                break;
            case ALL:
            default:
                bookings = bookingRepository
                        .findByItemOwnerIdOrderByStartDesc(ownerId);
        }

        List<BookingDto> result = bookings.stream()
                .map(BookingMapper::toDto)
                .toList();

        log.info("Найдено бронирований для владельца: ownerId={}, state={}, count={}",
                ownerId, state, result.size());

        return result;
    }

    private Booking getBookingOrThrow(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Бронирование с id" + id + " не найдено"));
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id" + userId + " не найден"));
    }

    private Item getItemOrThrow(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Вещь с id" + itemId + " не найдена"));
    }
}