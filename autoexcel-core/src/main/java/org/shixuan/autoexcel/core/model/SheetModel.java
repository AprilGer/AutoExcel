package org.shixuan.autoexcel.core.model;

import java.util.List;
import org.shixuan.autoexcel.annotation.ExcelSheet;
import org.shixuan.autoexcel.annotation.ExcelTemplate;

public final class SheetModel {
    private final ExcelSheet sheet;
    private final ExcelTemplate template;
    private final List<FieldModel> fields;

    public SheetModel(ExcelSheet sheet, ExcelTemplate template, List<FieldModel> fields) {
        this.sheet = sheet;
        this.template = template;
        this.fields = fields;
    }

    public ExcelSheet getSheet() {
        return sheet;
    }

    public ExcelTemplate getTemplate() {
        return template;
    }

    public List<FieldModel> getFields() {
        return fields;
    }
}
