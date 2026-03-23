package org.shixuan.autoexcel.event;

/**
 * 空实现（No-Operation）的 Excel 生命周期监听器。
 * 实现 {@link ExcelLifecycleListener} 接口，但所有回调方法均为空实现，
 * 用于在不关心导入/导出事件时，作为默认或占位监听器使用。
 */
public final class NoopExcelLifecycleListener implements ExcelLifecycleListener {
    public static final NoopExcelLifecycleListener INSTANCE = new NoopExcelLifecycleListener();

    private NoopExcelLifecycleListener() {
    }

    @Override
    public void beforeImport(Class<?> type) {
    }

    @Override
    public void afterImport(Class<?> type, int rowCount) {
    }

    @Override
    public void onRowError(int rowIndex, String message) {
    }

    @Override
    public void beforeExport(Class<?> type, int rowCount) {
    }

    @Override
    public void afterExport(Class<?> type, int rowCount) {
    }
}
