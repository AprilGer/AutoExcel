package org.shixuan.autoexcel.core.i18n;

import java.util.Locale;

public interface MessageResolver {
    String resolve(String key, Locale locale, Object... args);
}
