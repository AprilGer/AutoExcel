package org.shixuan.autoexcel.validator;

public interface ValueValidator<T> {
    ValidationResult validate(T value, ValidationContext context);
}
