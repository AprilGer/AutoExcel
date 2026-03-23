package org.shixuan.autoexcel.spring;

import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.shixuan.autoexcel.adapter.poi.PoiExcelEngineAdapter;
import org.shixuan.autoexcel.core.AnnotationMappingEngine;
import org.shixuan.autoexcel.core.AutoExcel;
import org.shixuan.autoexcel.core.ExcelEngineAdapter;
import org.shixuan.autoexcel.core.ExcelEngineRegistry;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AutoExcelProperties.class)
public class AutoExcelAutoConfiguration {
    @Bean
    public ExcelEngineAdapter poiExcelEngineAdapter() {
        return new PoiExcelEngineAdapter();
    }

    @Bean
    @ConditionalOnMissingBean
    public ExcelEngineRegistry excelEngineRegistry(ObjectProvider<List<ExcelEngineAdapter>> adaptersProvider) {
        ExcelEngineRegistry registry = new ExcelEngineRegistry();
        List<ExcelEngineAdapter> adapters = adaptersProvider.getIfAvailable();
        if (adapters != null) {
            for (ExcelEngineAdapter adapter : adapters) {
                registry.register(adapter);
            }
        }
        ServiceLoader<ExcelEngineAdapter> loader = ServiceLoader.load(ExcelEngineAdapter.class);
        for (ExcelEngineAdapter adapter : loader) {
            registry.register(adapter);
        }
        return registry;
    }

    @Bean
    @ConditionalOnMissingBean
    public AnnotationMappingEngine annotationMappingEngine() {
        return new AnnotationMappingEngine();
    }

    @Bean
    @ConditionalOnMissingBean
    public Executor autoExcelExecutor(AutoExcelProperties properties) {
        return Executors.newFixedThreadPool(Math.max(1, properties.getAsyncThreads()));
    }

    @Bean
    @ConditionalOnMissingBean
    public AutoExcel autoExcel(ExcelEngineRegistry registry, AnnotationMappingEngine mappingEngine, Executor autoExcelExecutor) {
        return new AutoExcel(registry, mappingEngine, autoExcelExecutor);
    }
}
