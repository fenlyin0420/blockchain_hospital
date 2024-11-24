package com.example.utils.validate.anotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({FIELD, PARAMETER})
@Constraint(validator = PositiveIntegerValidator.class)
public @interface ID {
    String message() default "必须是整数";
    Class<?>[] groups() default {};
    public abstract Class<? extends Payload>[] payload() default {};
}