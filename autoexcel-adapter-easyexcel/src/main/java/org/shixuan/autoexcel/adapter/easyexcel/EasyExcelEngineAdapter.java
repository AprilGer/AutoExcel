package org.shixuan.autoexcel.adapter.easyexcel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.shixuan.autoexcel.core.ExcelEngineAdapter;
import org.shixuan.autoexcel.core.model.FieldModel;
import org.shixuan.autoexcel.core.model.SheetModel;
import org.shixuan.autoexcel.util.ReflectionUtils;

public class EasyExcelEngineAdapter implements ExcelEngineAdapter {
    @Override
    public String name() {
        return "easyexcel";
    }

    @Override
    public <T> List<Map<String, String>> read(InputStream inputStream, SheetModel sheetModel, Class<T> type) {
        List<Map<String, String>> rows = new ArrayList<Map<String, String>>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String headerLine = reader.readLine();
            if (headerLine == null) {
                return rows;
            }
            String[] headers = headerLine.split(",");
            String line;
            while ((line = reader.readLine()) != null) {
                String[] cells = line.split(",");
                Map<String, String> map = new HashMap<String, String>();
                for (int i = 0; i < headers.length && i < cells.length; i++) {
                    map.put(headers[i], cells[i]);
                }
                rows.add(map);
            }
            return rows;
        } catch (Exception ex) {
            throw new IllegalStateException("EasyExcel adapter read failed", ex);
        }
    }

    @Override
    public <T> void write(OutputStream outputStream, SheetModel sheetModel, List<T> rows) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            for (int i = 0; i < sheetModel.getFields().size(); i++) {
                FieldModel field = sheetModel.getFields().get(i);
                writer.write(field.getColumn().headers()[field.getColumn().headers().length - 1]);
                if (i != sheetModel.getFields().size() - 1) {
                    writer.write(",");
                }
            }
            writer.newLine();
            for (T row : rows) {
                for (int i = 0; i < sheetModel.getFields().size(); i++) {
                    Object value = ReflectionUtils.readField(row, sheetModel.getFields().get(i).getField());
                    writer.write(value == null ? "" : String.valueOf(value));
                    if (i != sheetModel.getFields().size() - 1) {
                        writer.write(",");
                    }
                }
                writer.newLine();
            }
            writer.flush();
        } catch (Exception ex) {
            throw new IllegalStateException("EasyExcel adapter write failed", ex);
        }
    }
}
