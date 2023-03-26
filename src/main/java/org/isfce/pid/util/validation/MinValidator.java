package org.isfce.pid.util.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.isfce.pid.util.validation.annotation.Min;

public class MinValidator implements ConstraintValidator<Min, Short> {

    private short minValue;

    @Override
    public void initialize(Min constraintAnnotation) {
        this.minValue = (short) constraintAnnotation.value();
    }

	@Override
	public boolean isValid(Short value, ConstraintValidatorContext context) {
		// TODO Auto-generated method stub
		return value >= minValue;
	}
}
