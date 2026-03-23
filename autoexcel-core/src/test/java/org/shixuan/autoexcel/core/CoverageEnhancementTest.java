package org.shixuan.autoexcel.core;

import java.io.ByteArrayInputStream;
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
import org.shixuan.autoexcel.converter.ConverterContext;
import org.shixuan.autoexcel.converter.ValueConverter;
import org.shixuan.autoexcel.core.model.ImportResult;
import org.shixuan.autoexcel.core.model.RowError;
import org.shixuan.autoexcel.core.model.SheetModel;
import org.shixuan.autoexcel.event.ExcelLifecycleListener;
import org.shixuan.autoexcel.validator.ValidationContext;
import org.shixuan.autoexcel.validator.ValidationResult;
import org.shixuan.autoexcel.validator.ValueValidator;

class CoverageEnhancementTest {
    @Test
    void shouldCoverCustomConverterValidatorAndListener() {
        RecordingListener listener = new RecordingListener();
        AnnotationMappingEngine engine = new AnnotationMappingEngine(new MetadataScanner(), new org.shixuan.autoexcel.core.i18n.MapMessageResolver(), listener);
        CustomAdapter adapter = new CustomAdapter();
        ImportResult<ConvertBean> result = engine.importRows(adapter, new ByteArrayInputStream(new byte[0]), ConvertBean.class, Locale.CHINA);
        Assertions.assertEquals(1, result.getSuccessRows().size());
        Assertions.assertEquals(2, result.getErrors().size());
        RowError rowError = result.getErrors().get(0);
        Assertions.assertTrue(rowError.getRowIndex() > 0);
        Assertions.assertNotNull(rowError.getField());
        Assertions.assertNotNull(rowError.getMessage());
        engine.exportRows(adapter, new java.io.ByteArrayOutputStream(), ConvertBean.class, result.getSuccessRows(), Locale.CHINA);
        Assertions.assertTrue(listener.beforeImportCalled);
        Assertions.assertTrue(listener.afterImportCalled);
        Assertions.assertTrue(listener.beforeExportCalled);
        Assertions.assertTrue(listener.afterExportCalled);
    }

    @Test
    void shouldCoverFactoryAndMissingEngine() {
        AutoExcel autoExcel = AutoExcelFactory.createDefault();
        Assertions.assertThrows(RuntimeException.class, () -> autoExcel.exportTo("missing", new java.io.ByteArrayOutputStream(), ConvertBean.class, new ArrayList<ConvertBean>(), Locale.CHINA));
    }

    @ExcelSheet(name = "c")
    static class ConvertBean {
        @ExcelColumn(headers = {"val"}, required = true, converter = HexConverter.class, validator = MinValueValidator.class)
        private Integer value;
    }

    static class HexConverter implements ValueConverter<String, Integer> {
        @Override
        public Integer convert(String source, ConverterContext context) {
            if (source == null) {
                return null;
            }
            return Integer.parseInt(source, 16);
        }
    }

    static class MinValueValidator implements ValueValidator<Integer> {
        @Override
        public ValidationResult validate(Integer value, ValidationContext context) {
            if (value == null || value < 10) {
                return ValidationResult.fail("too small");
            }
            return ValidationResult.ok();
        }
    }

    static class RecordingListener implements ExcelLifecycleListener {
        boolean beforeImportCalled;
        boolean afterImportCalled;
        boolean beforeExportCalled;
        boolean afterExportCalled;

        @Override
        public void beforeImport(Class<?> type) {
            beforeImportCalled = true;
        }

        @Override
        public void afterImport(Class<?> type, int rowCount) {
            afterImportCalled = true;
        }

        @Override
        public void onRowError(int rowIndex, String message) {
        }

        @Override
        public void beforeExport(Class<?> type, int rowCount) {
            beforeExportCalled = true;
        }

        @Override
        public void afterExport(Class<?> type, int rowCount) {
            afterExportCalled = true;
        }
    }

    static class CustomAdapter implements ExcelEngineAdapter {
        @Override
        public String name() {
            return "x";
        }

        @Override
        public <T> List<Map<String, String>> read(InputStream inputStream, SheetModel sheetModel, Class<T> type) {
            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            Map<String, String> ok = new HashMap<String, String>();
            ok.put("val", "a");
            list.add(ok);
            Map<String, String> invalidFormat = new HashMap<String, String>();
            invalidFormat.put("val", "XYZ");
            list.add(invalidFormat);
            Map<String, String> invalidBusiness = new HashMap<String, String>();
            invalidBusiness.put("val", "1");
            list.add(invalidBusiness);
            return list;
        }

        @Override
        public <T> void write(java.io.OutputStream outputStream, SheetModel sheetModel, List<T> rows) {
        }
    }
}
