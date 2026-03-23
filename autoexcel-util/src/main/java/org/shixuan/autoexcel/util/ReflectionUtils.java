package org.shixuan.autoexcel.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public final class ReflectionUtils {
    private ReflectionUtils() {
    }

    public static <T> T instantiate(Class<T> type) {
        try {
            Constructor<T> constructor = type.getDeclaredConstructor();
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            return constructor.newInstance();
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot instantiate type " + type.getName(), ex);
        }
    }

    public static Object readField(Object target, Field field) {
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            return field.get(target);
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot read field " + field.getName(), ex);
        }
    }

    public static void writeField(Object target, Field field, Object value) {
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            field.set(target, value);
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot write field " + field.getName(), ex);
        }
    }
}
