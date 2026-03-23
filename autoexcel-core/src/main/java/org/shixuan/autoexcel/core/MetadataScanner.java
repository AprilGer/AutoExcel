package org.shixuan.autoexcel.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.shixuan.autoexcel.annotation.ExcelColumn;
import org.shixuan.autoexcel.annotation.ExcelComment;
import org.shixuan.autoexcel.annotation.ExcelIgnore;
import org.shixuan.autoexcel.annotation.ExcelSheet;
import org.shixuan.autoexcel.annotation.ExcelStyle;
import org.shixuan.autoexcel.annotation.ExcelTemplate;
import org.shixuan.autoexcel.core.model.FieldModel;
import org.shixuan.autoexcel.core.model.SheetModel;

public final class MetadataScanner {
    public <T> SheetModel scan(Class<T> type) {
        ExcelSheet sheet = type.getAnnotation(ExcelSheet.class);
        ExcelTemplate template = type.getAnnotation(ExcelTemplate.class);
        if (sheet == null) {
            throw new IllegalArgumentException("Missing @ExcelSheet on type: " + type.getName());
        }
        List<FieldModel> fieldModels = new ArrayList<FieldModel>();
        for (Field field : type.getDeclaredFields()) {
            if (field.getAnnotation(ExcelIgnore.class) != null) {
                continue;
            }
            ExcelColumn column = field.getAnnotation(ExcelColumn.class);
            if (column == null) {
                continue;
            }
            fieldModels.add(new FieldModel(field, column, field.getAnnotation(ExcelComment.class), field.getAnnotation(ExcelStyle.class)));
        }
        Collections.sort(fieldModels, new Comparator<FieldModel>() {
            @Override
            public int compare(FieldModel o1, FieldModel o2) {
                return Integer.compare(o1.getColumn().order(), o2.getColumn().order());
            }
        });
        return new SheetModel(sheet, template, fieldModels);
    }
}
