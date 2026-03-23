package org.shixuan.autoexcel.adapter.poi;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.shixuan.autoexcel.annotation.ExcelComment;
import org.shixuan.autoexcel.annotation.ExcelStyle;
import org.shixuan.autoexcel.core.ExcelEngineAdapter;
import org.shixuan.autoexcel.core.model.FieldModel;
import org.shixuan.autoexcel.core.model.SheetModel;
import org.shixuan.autoexcel.util.ReflectionUtils;

public class PoiExcelEngineAdapter implements ExcelEngineAdapter {
    @Override
    public String name() {
        return "poi";
    }

    @Override
    public <T> List<Map<String, String>> read(InputStream inputStream, SheetModel sheetModel, Class<T> type) {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        try {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheet(sheetModel.getSheet().name());
            if (sheet == null) {
                sheet = workbook.getSheetAt(0);
            }
            int headerRowIndex = Math.max(sheetModel.getSheet().headerRow(), sheetModel.getSheet().dataStartRow() - 1);
            int dataStartRow = sheetModel.getSheet().dataStartRow();
            Row header = sheet.getRow(headerRowIndex);
            Map<Integer, String> headerMap = new HashMap<Integer, String>();
            for (int i = 0; i < header.getLastCellNum(); i++) {
                Cell cell = header.getCell(i);
                headerMap.put(i, cell == null ? "" : cell.toString());
            }
            for (int i = dataStartRow; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                Map<String, String> map = new HashMap<String, String>();
                for (Map.Entry<Integer, String> entry : headerMap.entrySet()) {
                    Cell cell = row.getCell(entry.getKey());
                    map.put(entry.getValue(), cell == null ? "" : cell.toString());
                }
                result.add(map);
            }
            workbook.close();
            return result;
        } catch (Exception ex) {
            throw new IllegalStateException("POI read failed", ex);
        }
    }

    @Override
    public <T> void write(OutputStream outputStream, SheetModel sheetModel, List<T> rows) {
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
        workbook.setCompressTempFiles(true);
        try {
            Sheet sheet = workbook.createSheet(sheetModel.getSheet().name());
            Row header0 = sheet.createRow(sheetModel.getSheet().headerRow());
            Row header1 = sheet.createRow(sheetModel.getSheet().headerRow() + 1);
            int col = 0;
            for (FieldModel fieldModel : sheetModel.getFields()) {
                String[] headers = fieldModel.getColumn().headers();
                Cell top = header0.createCell(col);
                top.setCellValue(headers[0]);
                Cell bottom = header1.createCell(col);
                bottom.setCellValue(headers[headers.length - 1]);
                applyHeaderMetadata(workbook, sheet, top, fieldModel);
                if (headers.length > 1) {
                    sheet.addMergedRegion(new CellRangeAddress(header0.getRowNum(), header1.getRowNum(), col, col));
                }
                col++;
            }
            int rowIndex = sheetModel.getSheet().dataStartRow();
            for (T rowObj : rows) {
                Row row = sheet.createRow(rowIndex++);
                int c = 0;
                for (FieldModel fieldModel : sheetModel.getFields()) {
                    Cell cell = row.createCell(c++);
                    Object value = ReflectionUtils.readField(rowObj, fieldModel.getField());
                    cell.setCellValue(value == null ? "" : String.valueOf(value));
                }
            }
            workbook.write(outputStream);
        } catch (Exception ex) {
            throw new IllegalStateException("POI write failed", ex);
        } finally {
            try {
                workbook.close();
            } catch (Exception ignored) {
            }
            workbook.dispose();
        }
    }

    private void applyHeaderMetadata(Workbook workbook, Sheet sheet, Cell cell, FieldModel fieldModel) {
        ExcelStyle styleAnno = fieldModel.getStyle();
        if (styleAnno != null) {
            CellStyle style = workbook.createCellStyle();
            if (styleAnno.backgroundColor() >= 0) {
                style.setFillForegroundColor(styleAnno.backgroundColor());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            }
            Font font = workbook.createFont();
            font.setBold(styleAnno.bold());
            if (styleAnno.fontColor() >= 0) {
                font.setColor(styleAnno.fontColor());
            }
            style.setFont(font);
            cell.setCellStyle(style);
        }
        ExcelComment commentAnno = fieldModel.getComment();
        if (commentAnno != null) {
            CreationHelper helper = workbook.getCreationHelper();
            Drawing<?> drawing = sheet.createDrawingPatriarch();
            Comment comment = drawing.createCellComment(helper.createClientAnchor());
            RichTextString string = helper.createRichTextString(commentAnno.value());
            comment.setString(string);
            cell.setCellComment(comment);
        }
    }
}
