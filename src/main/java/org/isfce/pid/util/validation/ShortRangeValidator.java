package org.isfce.pid.util.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.isfce.pid.util.validation.annotation.ShortRange;

public class ShortRangeValidator implements ConstraintValidator<ShortRange, Short> {
	private short min;
    //private short max;

    @Override
    public void initialize(ShortRange constraintAnnotation) {
        this.min = constraintAnnotation.min();
        //this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(Short value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value >= min;// && value <= max;
    }
}
