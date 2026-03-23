package org.shixuan.autoexcel.core;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AutoExcelTest {
    @Test
    void shouldExportAsync() throws Exception {
        ExcelEngineRegistry registry = new ExcelEngineRegistry();
        registry.register(new AnnotationMappingEngineTest.StubAdapter());
        AutoExcel autoExcel = new AutoExcel(registry, new AnnotationMappingEngine(), Executors.newFixedThreadPool(1));
        List<AnnotationMappingEngineTest.RowBean> rows = new ArrayList<AnnotationMappingEngineTest.RowBean>();
        rows.add(new AnnotationMappingEngineTest.RowBean("n", 1));
        autoExcel.exportAsync("stub", new ByteArrayOutputStream(), AnnotationMappingEngineTest.RowBean.class, rows, Locale.CHINA).get();
        Assertions.assertTrue(true);
    }
}
