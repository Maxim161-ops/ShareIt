package ru.practicum.shareit.booking.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.dto.BookingCreateDto;

public class BookingDateValidator implements ConstraintValidator<ValidBookingDates, BookingCreateDto> {

    @Override
    public boolean isValid(BookingCreateDto dto, ConstraintValidatorContext context) {
        if (dto == null || dto.getStart() == null || dto.getEnd() == null) {
            return true;
        }

        return dto.getEnd().isAfter(dto.getStart());
    }
}

