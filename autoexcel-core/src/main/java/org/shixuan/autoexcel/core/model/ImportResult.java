package org.shixuan.autoexcel.core.model;

import java.util.Collections;
import java.util.List;

public final class ImportResult<T> {
    private final List<T> successRows;
    private final List<RowError> errors;

    public ImportResult(List<T> successRows, List<RowError> errors) {
        this.successRows = successRows;
        this.errors = errors;
    }

    public List<T> getSuccessRows() {
        return Collections.unmodifiableList(successRows);
    }

    public List<RowError> getErrors() {
        return Collections.unmodifiableList(errors);
    }
}
