package org.isfce.pid.util.validation.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.isfce.pid.util.validation.ShortRangeValidator;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = ShortRangeValidator.class)
@Documented
public @interface ShortRange {
    String message() default "{org.isfce.pid.util.validation.shortRange}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
    
    short min();
    //short max();
}
