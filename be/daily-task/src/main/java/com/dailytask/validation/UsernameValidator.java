package com.dailytask.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {
    
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9._-]{3,50}$");
    
    @Override
    public void initialize(ValidUsername constraintAnnotation) {
        // No initialization needed
    }
    
    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        // Check basic pattern
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            return false;
        }
        
        // Additional business rules
        if (username.startsWith(".") || username.endsWith(".")) {
            addViolation(context, "Username cannot start or end with a dot");
            return false;
        }
        
        if (username.startsWith("-") || username.endsWith("-")) {
            addViolation(context, "Username cannot start or end with a hyphen");
            return false;
        }
        
        if (username.startsWith("_") || username.endsWith("_")) {
            addViolation(context, "Username cannot start or end with an underscore");
            return false;
        }
        
        if (username.contains("..") || username.contains("--") || username.contains("__")) {
            addViolation(context, "Username cannot contain consecutive special characters");
            return false;
        }
        
        // Check for reserved usernames
        String lowerUsername = username.toLowerCase();
        if (isReservedUsername(lowerUsername)) {
            addViolation(context, "This username is reserved and cannot be used");
            return false;
        }
        
        return true;
    }
    
    private boolean isReservedUsername(String username) {
        String[] reservedUsernames = {
            "admin", "administrator", "root", "system", "user", "guest", "anonymous",
            "api", "www", "mail", "email", "support", "help", "info", "contact",
            "service", "test", "demo", "sample", "example", "null", "undefined"
        };
        
        for (String reserved : reservedUsernames) {
            if (reserved.equals(username)) {
                return true;
            }
        }
        return false;
    }
    
    private void addViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
} 