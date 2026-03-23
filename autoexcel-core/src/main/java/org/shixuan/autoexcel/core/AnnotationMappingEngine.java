package org.shixuan.autoexcel.core;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.shixuan.autoexcel.annotation.ExcelColumn;
import org.shixuan.autoexcel.converter.ConverterContext;
import org.shixuan.autoexcel.converter.DefaultConverters;
import org.shixuan.autoexcel.converter.ValueConverter;
import org.shixuan.autoexcel.core.i18n.MapMessageResolver;
import org.shixuan.autoexcel.core.i18n.MessageResolver;
import org.shixuan.autoexcel.core.model.FieldModel;
import org.shixuan.autoexcel.core.model.ImportResult;
import org.shixuan.autoexcel.core.model.RowError;
import org.shixuan.autoexcel.core.model.SheetModel;
import org.shixuan.autoexcel.event.ExcelLifecycleListener;
import org.shixuan.autoexcel.event.NoopExcelLifecycleListener;
import org.shixuan.autoexcel.util.ReflectionUtils;
import org.shixuan.autoexcel.validator.ValidationContext;
import org.shixuan.autoexcel.validator.ValidationResult;
import org.shixuan.autoexcel.validator.ValueValidator;

public final class AnnotationMappingEngine {
    private final MetadataScanner scanner;
    private final MessageResolver messageResolver;
    private final ExcelLifecycleListener listener;

    public AnnotationMappingEngine() {
        this(new MetadataScanner(), new MapMessageResolver(), NoopExcelLifecycleListener.INSTANCE);
    }

    public AnnotationMappingEngine(MetadataScanner scanner, MessageResolver messageResolver, ExcelLifecycleListener listener) {
        this.scanner = scanner;
        this.messageResolver = messageResolver;
        this.listener = listener;
    }

    public <T> ImportResult<T> importRows(ExcelEngineAdapter adapter, InputStream inputStream, Class<T> type, Locale locale) {
        listener.beforeImport(type);
        SheetModel sheetModel = scanner.scan(type);
        List<Map<String, String>> rows = adapter.read(inputStream, sheetModel, type);
        List<T> success = new ArrayList<T>();
        List<RowError> errors = new ArrayList<RowError>();
        int rowIndex = 0;
        for (Map<String, String> row : rows) {
            rowIndex++;
            T bean = ReflectionUtils.instantiate(type);
            boolean valid = true;
            for (FieldModel model : sheetModel.getFields()) {
                ExcelColumn column = model.getColumn();
                Field field = model.getField();
                String key = column.headers()[column.headers().length - 1];
                String raw = row.get(key);
                if (column.required() && (raw == null || raw.trim().isEmpty())) {
                    String message = messageResolver.resolve("excel.required", locale, rowIndex, field.getName());
                    errors.add(new RowError(rowIndex, field.getName(), message));
                    listener.onRowError(rowIndex, message);
                    valid = false;
                    continue;
                }
                try {
                    Object converted = convertIn(raw, field.getType(), column, locale);
                    if (!validateValue(converted, column, rowIndex, field.getName(), locale, errors)) {
                        valid = false;
                        continue;
                    }
                    ReflectionUtils.writeField(bean, field, converted);
                } catch (Exception ex) {
                    String message = messageResolver.resolve("excel.invalid", locale, rowIndex, field.getName(), ex.getMessage());
                    errors.add(new RowError(rowIndex, field.getName(), message));
                    listener.onRowError(rowIndex, message);
                    valid = false;
                }
            }
            if (valid) {
                success.add(bean);
            }
        }
        listener.afterImport(type, success.size());
        return new ImportResult<T>(success, errors);
    }

    public <T> void exportRows(ExcelEngineAdapter adapter, OutputStream outputStream, Class<T> type, List<T> rows, Locale locale) {
        listener.beforeExport(type, rows.size());
        SheetModel sheetModel = scanner.scan(type);
        List<T> convertedRows = new ArrayList<T>(rows.size());
        for (T row : rows) {
            convertedRows.add(row);
        }
        adapter.write(outputStream, sheetModel, convertedRows);
        listener.afterExport(type, rows.size());
    }

    private Object convertIn(String raw, Class<?> type, ExcelColumn column, Locale locale) {
        if (!Void.class.equals(column.converter())) {
            try {
                @SuppressWarnings("unchecked")
                ValueConverter<String, Object> converter = (ValueConverter<String, Object>) ReflectionUtils.instantiate((Class<Object>) column.converter());
                return converter.convert(raw, new ConverterContext(locale, column.format(), new HashMap<String, Object>()));
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }
        ValueConverter<String, ?> converter = DefaultConverters.importer(type);
        if (converter != null) {
            return converter.convert(raw, new ConverterContext(locale, column.format(), new HashMap<String, Object>()));
        }
        return raw;
    }

    @SuppressWarnings("unchecked")
    private boolean validateValue(Object value, ExcelColumn column, int rowIndex, String fieldName, Locale locale, List<RowError> errors) {
        if (Void.class.equals(column.validator())) {
            return true;
        }
        ValueValidator<Object> validator = (ValueValidator<Object>) ReflectionUtils.instantiate((Class<Object>) column.validator());
        ValidationResult result = validator.validate(value, new ValidationContext(rowIndex, fieldName, locale));
        if (result.isValid()) {
            return true;
        }
        String message = result.getMessage();
        errors.add(new RowError(rowIndex, fieldName, message));
        listener.onRowError(rowIndex, message);
        return false;
    }
}
