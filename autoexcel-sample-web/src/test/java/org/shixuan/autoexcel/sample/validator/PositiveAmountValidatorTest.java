package org.shixuan.autoexcel.sample.validator;

import java.math.BigDecimal;
import java.util.Locale;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.shixuan.autoexcel.validator.ValidationContext;

class PositiveAmountValidatorTest {
    @Test
    void shouldValidateAmount() {
        PositiveAmountValidator validator = new PositiveAmountValidator();
        Assertions.assertTrue(validator.validate(BigDecimal.ONE, new ValidationContext(1, "amount", Locale.CHINA)).isValid());
        Assertions.assertFalse(validator.validate(BigDecimal.ZERO, new ValidationContext(1, "amount", Locale.CHINA)).isValid());
        Assertions.assertFalse(validator.validate(null, new ValidationContext(1, "amount", Locale.CHINA)).isValid());
    }
}
