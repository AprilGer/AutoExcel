package org.shixuan.autoexcel.sample.config;

import org.shixuan.autoexcel.adapter.easyexcel.EasyExcelEngineAdapter;
import org.shixuan.autoexcel.adapter.jxls.JxlsExcelEngineAdapter;
import org.shixuan.autoexcel.core.ExcelEngineAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExcelAdapterConfiguration {
    @Bean
    public ExcelEngineAdapter easyExcelEngineAdapter() {
        return new EasyExcelEngineAdapter();
    }

    @Bean
    public ExcelEngineAdapter jxlsExcelEngineAdapter() {
        return new JxlsExcelEngineAdapter();
    }
}
