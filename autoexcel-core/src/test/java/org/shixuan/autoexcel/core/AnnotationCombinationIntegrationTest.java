package org.shixuan.autoexcel.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.shixuan.autoexcel.annotation.ExcelColumn;
import org.shixuan.autoexcel.annotation.ExcelComment;
import org.shixuan.autoexcel.annotation.ExcelIgnore;
import org.shixuan.autoexcel.annotation.ExcelSheet;
import org.shixuan.autoexcel.annotation.ExcelStyle;
import org.shixuan.autoexcel.annotation.ExcelTemplate;
import org.shixuan.autoexcel.core.model.SheetModel;

class AnnotationCombinationIntegrationTest {
    @Test
    void shouldCoverAnnotationCombination() {
        MetadataScanner scanner = new MetadataScanner();
        SheetModel model = scanner.scan(CombinedBean.class);
        Assertions.assertEquals("Combined", model.getSheet().name());
        Assertions.assertEquals("tpl.xlsx", model.getTemplate().value());
        Assertions.assertEquals(2, model.getFields().size());
        Assertions.assertNotNull(model.getFields().get(0).getComment());
        Assertions.assertNotNull(model.getFields().get(0).getStyle());
    }

    @ExcelSheet(name = "Combined")
    @ExcelTemplate("tpl.xlsx")
    static class CombinedBean {
        @ExcelColumn(headers = {"主", "编号"}, order = 1)
        @ExcelComment("注释")
        @ExcelStyle(bold = true)
        private String code;

        @ExcelColumn(headers = {"主", "名称"}, order = 2)
        private String name;

        @ExcelIgnore
        private String ignored;
    }
}
