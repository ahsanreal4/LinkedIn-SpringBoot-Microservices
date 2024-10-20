package com.linkedIn.users_service.validators.atLeastOneNotNull;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class AtLeastOneNotNullValidator implements ConstraintValidator<AtLeastOneNotNull, Object> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (obj == null) {
            return false; // Consider null object as invalid
        }

        // Use reflection to check if at least one field is not null
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true); // Make private fields accessible
            try {
                if (field.get(obj) != null) {
                    return true; // Found at least one non-null field
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return false; // No non-null fields found
    }
}
