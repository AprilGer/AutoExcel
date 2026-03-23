package org.shixuan.autoexcel.sample.validator;

import java.math.BigDecimal;
import org.shixuan.autoexcel.validator.ValidationContext;
import org.shixuan.autoexcel.validator.ValidationResult;
import org.shixuan.autoexcel.validator.ValueValidator;

public class PositiveAmountValidator implements ValueValidator<BigDecimal> {
    @Override
    public ValidationResult validate(BigDecimal value, ValidationContext context) {
        if (value == null) {
            return ValidationResult.fail("金额不能为空");
        }
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            return ValidationResult.fail("金额必须大于0");
        }
        return ValidationResult.ok();
    }
}
