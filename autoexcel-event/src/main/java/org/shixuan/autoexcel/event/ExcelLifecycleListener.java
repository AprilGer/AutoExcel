package org.shixuan.autoexcel.event;

public interface ExcelLifecycleListener {
    void beforeImport(Class<?> type);
    void afterImport(Class<?> type, int rowCount);
    void onRowError(int rowIndex, String message);
    void beforeExport(Class<?> type, int rowCount);
    void afterExport(Class<?> type, int rowCount);
}
