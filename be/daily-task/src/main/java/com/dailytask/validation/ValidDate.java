package com.dailytask.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateValidator.class)
@Documented
public @interface ValidDate {
    String message() default "Invalid date provided";
    boolean allowFuture() default true;
    boolean allowPast() default true;
    int maxDaysInPast() default -1;
    int maxDaysInFuture() default -1;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
} 