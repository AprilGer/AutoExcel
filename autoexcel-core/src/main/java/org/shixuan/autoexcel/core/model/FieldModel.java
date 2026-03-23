package org.shixuan.autoexcel.core.model;

import java.lang.reflect.Field;
import org.shixuan.autoexcel.annotation.ExcelColumn;
import org.shixuan.autoexcel.annotation.ExcelComment;
import org.shixuan.autoexcel.annotation.ExcelStyle;

public final class FieldModel {
    private final Field field;
    private final ExcelColumn column;
    private final ExcelComment comment;
    private final ExcelStyle style;

    public FieldModel(Field field, ExcelColumn column, ExcelComment comment, ExcelStyle style) {
        this.field = field;
        this.column = column;
        this.comment = comment;
        this.style = style;
    }

    public Field getField() {
        return field;
    }

    public ExcelColumn getColumn() {
        return column;
    }

    public ExcelComment getComment() {
        return comment;
    }

    public ExcelStyle getStyle() {
        return style;
    }
}
