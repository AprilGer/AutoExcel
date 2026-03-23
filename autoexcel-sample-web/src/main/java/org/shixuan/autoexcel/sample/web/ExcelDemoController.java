package org.shixuan.autoexcel.sample.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.shixuan.autoexcel.core.AutoExcel;
import org.shixuan.autoexcel.core.model.ImportResult;
import org.shixuan.autoexcel.core.model.RowError;
import org.shixuan.autoexcel.sample.dto.OrderRow;
import org.shixuan.autoexcel.sample.dto.TemplateOrderRow;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/excel")
public class ExcelDemoController {
    private final AutoExcel autoExcel;
    private final Map<String, CompletableFuture<byte[]>> asyncTasks = new ConcurrentHashMap<String, CompletableFuture<byte[]>>();

    public ExcelDemoController(AutoExcel autoExcel) {
        this.autoExcel = autoExcel;
    }

    @GetMapping("/export/single")
    public ResponseEntity<byte[]> exportSingle() throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        autoExcel.exportTo("poi", output, OrderRow.class, mockRows(50), Locale.SIMPLIFIED_CHINESE);
        return excelResponse("single.xlsx", output.toByteArray());
    }

    @GetMapping("/export/chunk")
    public ResponseEntity<byte[]> exportChunk(@RequestParam(defaultValue = "1000") int size) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        List<OrderRow> all = mockRows(size);
        List<OrderRow> merged = new ArrayList<OrderRow>();
        int chunk = 200;
        for (int i = 0; i < all.size(); i += chunk) {
            int end = Math.min(i + chunk, all.size());
            merged.addAll(all.subList(i, end));
        }
        autoExcel.exportTo("poi", output, OrderRow.class, merged, Locale.SIMPLIFIED_CHINESE);
        return excelResponse("chunk.xlsx", output.toByteArray());
    }

    @GetMapping("/export/async/start")
    public ResponseEntity<String> exportAsyncStart(@RequestParam(defaultValue = "1000") int size) {
        String taskId = UUID.randomUUID().toString().replace("-", "");
        CompletableFuture<byte[]> future = CompletableFuture.supplyAsync(() -> {
            try {
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                autoExcel.exportTo("poi", output, OrderRow.class, mockRows(size), Locale.SIMPLIFIED_CHINESE);
                return output.toByteArray();
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        });
        asyncTasks.put(taskId, future);
        return ResponseEntity.ok(taskId);
    }

    @GetMapping("/export/async/{taskId}")
    public ResponseEntity<byte[]> exportAsyncResult(@PathVariable String taskId) throws Exception {
        CompletableFuture<byte[]> future = asyncTasks.get(taskId);
        if (future == null || !future.isDone()) {
            return ResponseEntity.accepted().build();
        }
        byte[] bytes = future.get();
        asyncTasks.remove(taskId);
        return excelResponse("async.xlsx", bytes);
    }

    @GetMapping("/export/template")
    public ResponseEntity<byte[]> exportTemplate() throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        List<TemplateOrderRow> rows = new ArrayList<TemplateOrderRow>();
        rows.add(new TemplateOrderRow("T-001", "SUCCESS"));
        rows.add(new TemplateOrderRow("T-002", "FAIL"));
        autoExcel.exportTo("jxls", output, TemplateOrderRow.class, rows, Locale.CHINA);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=template.txt")
                .body(output.toByteArray());
    }

    @PostMapping("/import/error-writeback")
    public ResponseEntity<byte[]> importAndWriteBack(@RequestParam("file") MultipartFile file) throws Exception {
        ImportResult<OrderRow> result = autoExcel.importFrom("poi", new ByteArrayInputStream(file.getBytes()), OrderRow.class, Locale.SIMPLIFIED_CHINESE);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Errors");
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("rowIndex");
        header.createCell(1).setCellValue("field");
        header.createCell(2).setCellValue("message");
        int index = 1;
        for (RowError error : result.getErrors()) {
            Row row = sheet.createRow(index++);
            row.createCell(0).setCellValue(error.getRowIndex());
            row.createCell(1).setCellValue(error.getField());
            row.createCell(2).setCellValue(error.getMessage());
        }
        workbook.write(output);
        workbook.close();
        return excelResponse("error-writeback.xlsx", output.toByteArray());
    }

    private List<OrderRow> mockRows(int size) {
        List<OrderRow> rows = new ArrayList<OrderRow>();
        for (int i = 1; i <= size; i++) {
            rows.add(new OrderRow("ORD-" + i, "客户-" + i, BigDecimal.valueOf(i * 10L)));
        }
        return rows;
    }

    private ResponseEntity<byte[]> excelResponse(String fileName, byte[] bytes) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .body(bytes);
    }
}
