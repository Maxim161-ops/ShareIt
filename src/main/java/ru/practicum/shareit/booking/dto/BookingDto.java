package ru.practicum.shareit.booking.dto;

/**
 * TODO Sprint add-bookings.
 */
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingStatus;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {

    private Long id;

    @NotNull(message = "Время начала бронирования обязательно")
    @Future(message = "Время начала должно быть в будущем")
    private LocalDateTime start;

    @NotNull(message = "Время окончания бронирования обязательно")
    @Future(message = "Время окончания должно быть в будущем")
    private LocalDateTime end;

    @NotNull(message = "ID вещи обязателен")
    private Long itemId;

    @NotNull
    private Long bookerId;

    private BookingStatus status;
}
