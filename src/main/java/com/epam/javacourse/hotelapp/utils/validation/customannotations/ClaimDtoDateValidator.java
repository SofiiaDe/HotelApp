package com.epam.javacourse.hotelapp.utils.validation.customannotations;

import com.epam.javacourse.hotelapp.dto.ClaimDto;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class ClaimDtoDateValidator implements ConstraintValidator<ClaimDtoRangeCheck, ClaimDto> {

    @Override
    public void initialize(ClaimDtoRangeCheck date) {
    }

    @Override
    public boolean isValid(ClaimDto claimDto, ConstraintValidatorContext constraintValidatorContext) {
        if (claimDto.getCheckinDate() == null || claimDto.getCheckoutDate() == null) {
            return true;
        }
        return claimDto.getCheckinDate().isBefore(claimDto.getCheckoutDate());
    }
}
