package org.shixuan.autoexcel.benchmark;

import java.math.BigDecimal;
import org.shixuan.autoexcel.annotation.ExcelColumn;
import org.shixuan.autoexcel.annotation.ExcelSheet;

@ExcelSheet(name = "Benchmark", headerRow = 0, dataStartRow = 2)
public class BenchmarkRow {
    @ExcelColumn(headers = {"基础", "编码"}, order = 1)
    private String code;

    @ExcelColumn(headers = {"基础", "金额"}, order = 2)
    private BigDecimal amount;

    public BenchmarkRow() {
    }

    public BenchmarkRow(String code, BigDecimal amount) {
        this.code = code;
        this.amount = amount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
