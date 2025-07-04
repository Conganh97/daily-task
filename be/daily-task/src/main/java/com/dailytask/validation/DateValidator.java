package com.dailytask.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateValidator implements ConstraintValidator<ValidDate, LocalDate> {
    
    private boolean allowFuture;
    private boolean allowPast;
    private int maxDaysInPast;
    private int maxDaysInFuture;
    
    @Override
    public void initialize(ValidDate constraintAnnotation) {
        this.allowFuture = constraintAnnotation.allowFuture();
        this.allowPast = constraintAnnotation.allowPast();
        this.maxDaysInPast = constraintAnnotation.maxDaysInPast();
        this.maxDaysInFuture = constraintAnnotation.maxDaysInFuture();
    }
    
    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date == null) {
            return true; // Let @NotNull handle null validation
        }
        
        LocalDate today = LocalDate.now();
        
        // Check if date is in the future
        if (date.isAfter(today)) {
            if (!allowFuture) {
                addViolation(context, "Date cannot be in the future");
                return false;
            }
            
            if (maxDaysInFuture > 0) {
                long daysInFuture = ChronoUnit.DAYS.between(today, date);
                if (daysInFuture > maxDaysInFuture) {
                    addViolation(context, String.format("Date cannot be more than %d days in the future", maxDaysInFuture));
                    return false;
                }
            }
        }
        
        // Check if date is in the past
        if (date.isBefore(today)) {
            if (!allowPast) {
                addViolation(context, "Date cannot be in the past");
                return false;
            }
            
            if (maxDaysInPast > 0) {
                long daysInPast = ChronoUnit.DAYS.between(date, today);
                if (daysInPast > maxDaysInPast) {
                    addViolation(context, String.format("Date cannot be more than %d days in the past", maxDaysInPast));
                    return false;
                }
            }
        }
        
        return true;
    }
    
    private void addViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
} 