package org.isfce.pid.util.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Min;

public class ShortMinValidator implements ConstraintValidator<Min, Short> {
private short minValue;
	
	public void initialize(Min constraintAnnotation) {
		this.minValue = (short) constraintAnnotation.value();
	}
	
	public boolean isValid(Short value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		return value >= minValue;
	}
}
