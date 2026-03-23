package org.shixuan.autoexcel.converter;

import java.util.Locale;
import java.util.Map;

public final class ConverterContext {
    private final Locale locale;
    private final String format;
    private final Map<String, Object> attributes;

    public ConverterContext(Locale locale, String format, Map<String, Object> attributes) {
        this.locale = locale;
        this.format = format;
        this.attributes = attributes;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getFormat() {
        return format;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
