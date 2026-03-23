package org.shixuan.autoexcel.core;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import org.shixuan.autoexcel.core.model.SheetModel;

public interface ExcelEngineAdapter {
    String name();

    <T> List<Map<String, String>> read(InputStream inputStream, SheetModel sheetModel, Class<T> type);

    <T> void write(OutputStream outputStream, SheetModel sheetModel, List<T> rows);
}
