package com.epam.javacourse.hotelapp.utils.validation.customannotations;

import com.epam.javacourse.hotelapp.dto.BookingDto;
import com.epam.javacourse.hotelapp.dto.ClaimDto;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class BookingDtoDateValidator implements ConstraintValidator<BookingDtoRangeCheck, BookingDto> {

    @Override
    public void initialize(BookingDtoRangeCheck date) {
    }

    @Override
    public boolean isValid(BookingDto bookingDto, ConstraintValidatorContext constraintValidatorContext) {
        if (bookingDto.getCheckin() == null || bookingDto.getCheckout() == null) {
            return true;
        }
        return bookingDto.getCheckin().isBefore(bookingDto.getCheckout());
    }
}
