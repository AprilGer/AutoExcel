package org.shixuan.autoexcel.validator;

import java.util.Locale;

public final class ValidationContext {
    private final int rowIndex;
    private final String fieldName;
    private final Locale locale;

    public ValidationContext(int rowIndex, String fieldName, Locale locale) {
        this.rowIndex = rowIndex;
        this.fieldName = fieldName;
        this.locale = locale;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Locale getLocale() {
        return locale;
    }
}
