package org.shixuan.autoexcel.sample.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExcelAdapterConfigurationTest {
    @Test
    void shouldCreateAdapters() {
        ExcelAdapterConfiguration configuration = new ExcelAdapterConfiguration();
        Assertions.assertNotNull(configuration.easyExcelEngineAdapter());
        Assertions.assertNotNull(configuration.jxlsExcelEngineAdapter());
    }
}
