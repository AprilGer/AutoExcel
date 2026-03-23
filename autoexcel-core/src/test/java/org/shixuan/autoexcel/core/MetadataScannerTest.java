package org.shixuan.autoexcel.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.shixuan.autoexcel.annotation.ExcelColumn;
import org.shixuan.autoexcel.annotation.ExcelSheet;
import org.shixuan.autoexcel.core.model.SheetModel;

class MetadataScannerTest {
    @Test
    void shouldScanOrderedColumns() {
        MetadataScanner scanner = new MetadataScanner();
        SheetModel sheetModel = scanner.scan(TestRow.class);
        Assertions.assertEquals("Demo", sheetModel.getSheet().name());
        Assertions.assertEquals(2, sheetModel.getFields().size());
        Assertions.assertEquals("id", sheetModel.getFields().get(0).getField().getName());
    }

    @ExcelSheet(name = "Demo")
    static class TestRow {
        @ExcelColumn(headers = {"A"}, order = 2)
        private String name;

        @ExcelColumn(headers = {"B"}, order = 1)
        private String id;
    }
}
