package org.isfce.pid.util.validation.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.isfce.pid.util.validation.MinValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = MinValidator.class)
@Documented
public @interface Min {

    String message() default "{javax.validation.constraints.Min.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    short value();
}
