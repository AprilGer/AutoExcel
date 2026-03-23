package org.shixuan.autoexcel.sample.dto;

import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DtoAccessorTest {
    @Test
    void shouldAccessOrderRow() {
        OrderRow row = new OrderRow();
        row.setOrderNo("x");
        row.setCustomerName("y");
        row.setAmount(BigDecimal.TEN);
        Assertions.assertEquals("x", row.getOrderNo());
        Assertions.assertEquals("y", row.getCustomerName());
        Assertions.assertEquals(BigDecimal.TEN, row.getAmount());
    }

    @Test
    void shouldAccessTemplateRow() {
        TemplateOrderRow row = new TemplateOrderRow();
        row.setOrderNo("x");
        row.setStatus("s");
        Assertions.assertEquals("x", row.getOrderNo());
        Assertions.assertEquals("s", row.getStatus());
    }
}
