package org.shixuan.autoexcel.sample.dto;

import java.math.BigDecimal;
import org.shixuan.autoexcel.annotation.ExcelColumn;
import org.shixuan.autoexcel.annotation.ExcelComment;
import org.shixuan.autoexcel.annotation.ExcelSheet;
import org.shixuan.autoexcel.annotation.ExcelStyle;
import org.shixuan.autoexcel.sample.validator.PositiveAmountValidator;

@ExcelSheet(name = "Orders", headerRow = 0, dataStartRow = 2)
public class OrderRow {
    @ExcelColumn(headers = {"订单信息", "订单号"}, required = true, order = 1)
    @ExcelComment("唯一订单标识")
    @ExcelStyle(bold = true)
    private String orderNo;

    @ExcelColumn(headers = {"客户信息", "客户名称"}, required = true, order = 2)
    private String customerName;

    @ExcelColumn(headers = {"金额信息", "金额"}, required = true, order = 3, validator = PositiveAmountValidator.class)
    private BigDecimal amount;

    public OrderRow() {
    }

    public OrderRow(String orderNo, String customerName, BigDecimal amount) {
        this.orderNo = orderNo;
        this.customerName = customerName;
        this.amount = amount;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
