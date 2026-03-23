package org.shixuan.autoexcel.core;

import java.util.ServiceLoader;

public final class AutoExcelFactory {
    private AutoExcelFactory() {
    }

    public static AutoExcel createDefault() {
        ExcelEngineRegistry registry = new ExcelEngineRegistry();
        ServiceLoader<ExcelEngineAdapter> loader = ServiceLoader.load(ExcelEngineAdapter.class);
        for (ExcelEngineAdapter adapter : loader) {
            registry.register(adapter);
        }
        return new AutoExcel(registry, new AnnotationMappingEngine());
    }
}
