package org.shixuan.autoexcel.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "autoexcel")
public class AutoExcelProperties {
    private String defaultEngine = "poi";
    private int asyncThreads = 2;

    public String getDefaultEngine() {
        return defaultEngine;
    }

    public void setDefaultEngine(String defaultEngine) {
        this.defaultEngine = defaultEngine;
    }

    public int getAsyncThreads() {
        return asyncThreads;
    }

    public void setAsyncThreads(int asyncThreads) {
        this.asyncThreads = asyncThreads;
    }
}
