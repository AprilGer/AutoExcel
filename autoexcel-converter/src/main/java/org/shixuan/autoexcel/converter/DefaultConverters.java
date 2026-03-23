package org.shixuan.autoexcel.converter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public final class DefaultConverters {
    private static final Map<Class<?>, ValueConverter<String, ?>> IMPORTERS = new HashMap<Class<?>, ValueConverter<String, ?>>();
    private static final Map<Class<?>, ValueConverter<Object, String>> EXPORTERS = new HashMap<Class<?>, ValueConverter<Object, String>>();

    static {
        IMPORTERS.put(String.class, new ValueConverter<String, String>() {
            @Override
            public String convert(String source, ConverterContext context) {
                return source;
            }
        });
        IMPORTERS.put(Integer.class, new ValueConverter<String, Integer>() {
            @Override
            public Integer convert(String source, ConverterContext context) {
                return source == null || source.trim().isEmpty() ? null : Integer.valueOf(source.trim());
            }
        });
        IMPORTERS.put(Long.class, new ValueConverter<String, Long>() {
            @Override
            public Long convert(String source, ConverterContext context) {
                return source == null || source.trim().isEmpty() ? null : Long.valueOf(source.trim());
            }
        });
        IMPORTERS.put(BigDecimal.class, new ValueConverter<String, BigDecimal>() {
            @Override
            public BigDecimal convert(String source, ConverterContext context) {
                return source == null || source.trim().isEmpty() ? null : new BigDecimal(source.trim());
            }
        });
        IMPORTERS.put(LocalDate.class, new ValueConverter<String, LocalDate>() {
            @Override
            public LocalDate convert(String source, ConverterContext context) {
                if (source == null || source.trim().isEmpty()) {
                    return null;
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(context.getFormat().isEmpty() ? "yyyy-MM-dd" : context.getFormat());
                return LocalDate.parse(source.trim(), formatter);
            }
        });
    }

    private DefaultConverters() {
    }

    @SuppressWarnings("unchecked")
    public static <T> ValueConverter<String, T> importer(Class<T> type) {
        return (ValueConverter<String, T>) IMPORTERS.get(type);
    }

    public static ValueConverter<Object, String> exporter(Class<?> type) {
        ValueConverter<Object, String> converter = EXPORTERS.get(type);
        if (converter != null) {
            return converter;
        }
        return new ValueConverter<Object, String>() {
            @Override
            public String convert(Object source, ConverterContext context) {
                return source == null ? "" : String.valueOf(source);
            }
        };
    }
}
