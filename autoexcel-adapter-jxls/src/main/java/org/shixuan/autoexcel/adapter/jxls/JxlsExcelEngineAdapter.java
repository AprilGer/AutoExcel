package org.shixuan.autoexcel.adapter.jxls;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.shixuan.autoexcel.core.ExcelEngineAdapter;
import org.shixuan.autoexcel.core.model.FieldModel;
import org.shixuan.autoexcel.core.model.SheetModel;
import org.shixuan.autoexcel.util.ReflectionUtils;

public class JxlsExcelEngineAdapter implements ExcelEngineAdapter {
    @Override
    public String name() {
        return "jxls";
    }

    @Override
    public <T> List<Map<String, String>> read(InputStream inputStream, SheetModel sheetModel, Class<T> type) {
        return new ArrayList<Map<String, String>>();
    }

    @Override
    public <T> void write(OutputStream outputStream, SheetModel sheetModel, List<T> rows) {
        try {
            String template = "${rows}";
            if (sheetModel.getTemplate() != null) {
                template = sheetModel.getTemplate().value();
            }
            StringBuilder body = new StringBuilder();
            for (T row : rows) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (FieldModel field : sheetModel.getFields()) {
                    map.put(field.getField().getName(), ReflectionUtils.readField(row, field.getField()));
                }
                body.append(map.toString()).append('\n');
            }
            String out = template.replace("${rows}", body.toString());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(out.getBytes(StandardCharsets.UTF_8));
            baos.writeTo(outputStream);
        } catch (Exception ex) {
            throw new IllegalStateException("JXLS adapter write failed", ex);
        }
    }
}
