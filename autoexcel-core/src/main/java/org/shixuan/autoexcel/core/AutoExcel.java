package org.shixuan.autoexcel.core;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.shixuan.autoexcel.core.model.ImportResult;
import org.shixuan.autoexcel.exception.ExcelException;

public final class AutoExcel {
    private final ExcelEngineRegistry registry;
    private final AnnotationMappingEngine mappingEngine;
    private final Executor exportExecutor;

    public AutoExcel(ExcelEngineRegistry registry, AnnotationMappingEngine mappingEngine) {
        this(registry, mappingEngine, Executors.newFixedThreadPool(2));
    }

    public AutoExcel(ExcelEngineRegistry registry, AnnotationMappingEngine mappingEngine, Executor exportExecutor) {
        this.registry = registry;
        this.mappingEngine = mappingEngine;
        this.exportExecutor = exportExecutor;
    }

    public <T> ImportResult<T> importFrom(String engine, InputStream inputStream, Class<T> type, Locale locale) {
        return mappingEngine.importRows(resolveEngine(engine), inputStream, type, locale);
    }

    public <T> void exportTo(String engine, OutputStream outputStream, Class<T> type, List<T> rows, Locale locale) {
        mappingEngine.exportRows(resolveEngine(engine), outputStream, type, rows, locale);
    }

    public <T> CompletableFuture<Void> exportAsync(String engine, OutputStream outputStream, Class<T> type, List<T> rows, Locale locale) {
        return CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                exportTo(engine, outputStream, type, rows, locale);
            }
        }, exportExecutor);
    }

    private ExcelEngineAdapter resolveEngine(String engine) {
        ExcelEngineAdapter adapter = registry.get(engine);
        if (adapter == null) {
            throw new ExcelException("No excel engine found: " + engine);
        }
        return adapter;
    }
}
