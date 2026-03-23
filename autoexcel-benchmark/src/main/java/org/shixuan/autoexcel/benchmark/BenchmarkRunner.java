package org.shixuan.autoexcel.benchmark;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.shixuan.autoexcel.adapter.easyexcel.EasyExcelEngineAdapter;
import org.shixuan.autoexcel.adapter.poi.PoiExcelEngineAdapter;
import org.shixuan.autoexcel.core.AnnotationMappingEngine;
import org.shixuan.autoexcel.core.AutoExcel;
import org.shixuan.autoexcel.core.ExcelEngineRegistry;

public class BenchmarkRunner {
    public static void main(String[] args) throws Exception {
        int size = args.length == 0 ? 20000 : Integer.parseInt(args[0]);
        List<BenchmarkRow> rows = mockRows(size);
        BenchmarkRunner runner = new BenchmarkRunner();
        long nativePoi = runner.nativePoi(rows);
        long autoPoi = runner.autoExcelPoi(rows);
        long autoEasy = runner.autoExcelEasy(rows);
        String report = "size=" + size
                + "\nnative-poi-ms=" + nativePoi
                + "\nautoexcel-poi-ms=" + autoPoi
                + "\nautoexcel-easyexcel-ms=" + autoEasy;
        System.out.println(report);
    }

    private long nativePoi(List<BenchmarkRow> rows) throws Exception {
        long start = System.currentTimeMillis();
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
        workbook.createSheet("Benchmark");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        workbook.write(output);
        workbook.close();
        workbook.dispose();
        return System.currentTimeMillis() - start;
    }

    private long autoExcelPoi(List<BenchmarkRow> rows) {
        ExcelEngineRegistry registry = new ExcelEngineRegistry();
        registry.register(new PoiExcelEngineAdapter());
        AutoExcel autoExcel = new AutoExcel(registry, new AnnotationMappingEngine());
        long start = System.currentTimeMillis();
        autoExcel.exportTo("poi", new ByteArrayOutputStream(), BenchmarkRow.class, rows, Locale.CHINA);
        return System.currentTimeMillis() - start;
    }

    private long autoExcelEasy(List<BenchmarkRow> rows) {
        ExcelEngineRegistry registry = new ExcelEngineRegistry();
        registry.register(new EasyExcelEngineAdapter());
        AutoExcel autoExcel = new AutoExcel(registry, new AnnotationMappingEngine());
        long start = System.currentTimeMillis();
        autoExcel.exportTo("easyexcel", new ByteArrayOutputStream(), BenchmarkRow.class, rows, Locale.CHINA);
        return System.currentTimeMillis() - start;
    }

    private static List<BenchmarkRow> mockRows(int size) {
        List<BenchmarkRow> rows = new ArrayList<BenchmarkRow>();
        for (int i = 0; i < size; i++) {
            rows.add(new BenchmarkRow("B-" + i, BigDecimal.valueOf(i)));
        }
        return rows;
    }
}
