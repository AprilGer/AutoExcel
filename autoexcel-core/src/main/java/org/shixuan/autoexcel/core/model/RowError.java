package org.shixuan.autoexcel.core.model;

public final class RowError {
    private final int rowIndex;
    private final String field;
    private final String message;

    public RowError(int rowIndex, String field, String message) {
        this.rowIndex = rowIndex;
        this.field = field;
        this.message = message;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}
