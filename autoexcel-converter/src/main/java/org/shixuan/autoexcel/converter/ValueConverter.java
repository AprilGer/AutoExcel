package org.shixuan.autoexcel.converter;

public interface ValueConverter<S, T> {
    T convert(S source, ConverterContext context);
}
