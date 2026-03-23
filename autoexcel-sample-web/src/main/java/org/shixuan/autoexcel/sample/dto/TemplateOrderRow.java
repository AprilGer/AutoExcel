package org.shixuan.autoexcel.sample.dto;

import org.shixuan.autoexcel.annotation.ExcelColumn;
import org.shixuan.autoexcel.annotation.ExcelSheet;
import org.shixuan.autoexcel.annotation.ExcelTemplate;

@ExcelSheet(name = "Template")
@ExcelTemplate("模板导出:\n${rows}")
public class TemplateOrderRow {
    @ExcelColumn(headers = {"订单号"}, order = 1)
    private String orderNo;

    @ExcelColumn(headers = {"状态"}, order = 2)
    private String status;

    public TemplateOrderRow() {
    }

    public TemplateOrderRow(String orderNo, String status) {
        this.orderNo = orderNo;
        this.status = status;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
