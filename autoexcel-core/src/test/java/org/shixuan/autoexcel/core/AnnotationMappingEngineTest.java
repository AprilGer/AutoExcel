package org.shixuan.autoexcel.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.shixuan.autoexcel.annotation.ExcelColumn;
import org.shixuan.autoexcel.annotation.ExcelSheet;
import org.shixuan.autoexcel.core.model.ImportResult;
import org.shixuan.autoexcel.core.model.SheetModel;

class AnnotationMappingEngineTest {
    @Test
    void shouldImportValidRowsAndCollectErrors() {
        AnnotationMappingEngine engine = new AnnotationMappingEngine();
        StubAdapter adapter = new StubAdapter();
        ImportResult<RowBean> result = engine.importRows(adapter, new ByteArrayInputStream(new byte[0]), RowBean.class, Locale.CHINA);
        Assertions.assertEquals(1, result.getSuccessRows().size());
        Assertions.assertEquals(1, result.getErrors().size());
    }

    @Test
    void shouldExportRows() {
        AnnotationMappingEngine engine = new AnnotationMappingEngine();
        StubAdapter adapter = new StubAdapter();
        List<RowBean> rows = new ArrayList<RowBean>();
        rows.add(new RowBean("x", 1));
        engine.exportRows(adapter, new ByteArrayOutputStream(), RowBean.class, rows, Locale.CHINA);
        Assertions.assertEquals(1, adapter.exportedRows.size());
    }

    @ExcelSheet(name = "Rows")
    static class RowBean {
        @ExcelColumn(headers = {"name"}, required = true, order = 1)
        private String name;
        @ExcelColumn(headers = {"qty"}, order = 2)
        private Integer qty;

        RowBean() {
        }

        RowBean(String name, Integer qty) {
            this.name = name;
            this.qty = qty;
        }
    }

    static class StubAdapter implements ExcelEngineAdapter {
        private final List<RowBean> exportedRows = new ArrayList<RowBean>();

        @Override
        public String name() {
            return "stub";
        }

        @Override
        public <T> List<Map<String, String>> read(InputStream inputStream, SheetModel sheetModel, Class<T> type) {
            List<Map<String, String>> data = new ArrayList<Map<String, String>>();
            Map<String, String> ok = new HashMap<String, String>();
            ok.put("name", "alpha");
            ok.put("qty", "10");
            data.add(ok);
            Map<String, String> bad = new HashMap<String, String>();
            bad.put("qty", "11");
            data.add(bad);
            return data;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> void write(java.io.OutputStream outputStream, SheetModel sheetModel, List<T> rows) {
            exportedRows.addAll((List<RowBean>) rows);
        }
    }
}
