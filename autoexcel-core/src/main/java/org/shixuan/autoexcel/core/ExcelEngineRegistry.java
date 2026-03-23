package org.shixuan.autoexcel.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ExcelEngineRegistry {
    private final Map<String, ExcelEngineAdapter> adapters = new ConcurrentHashMap<String, ExcelEngineAdapter>();

    public void register(ExcelEngineAdapter adapter) {
        adapters.put(adapter.name(), adapter);
    }

    public ExcelEngineAdapter get(String name) {
        return adapters.get(name);
    }
}
