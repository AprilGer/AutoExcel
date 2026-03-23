package org.shixuan.autoexcel.core.i18n;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class MapMessageResolver implements MessageResolver {
    private final Map<String, String> zhCn = new HashMap<String, String>();
    private final Map<String, String> enUs = new HashMap<String, String>();

    public MapMessageResolver() {
        zhCn.put("excel.required", "第{0}行字段{1}不能为空");
        zhCn.put("excel.invalid", "第{0}行字段{1}格式错误: {2}");
        enUs.put("excel.required", "Row {0} field {1} is required");
        enUs.put("excel.invalid", "Row {0} field {1} invalid: {2}");
    }

    @Override
    public String resolve(String key, Locale locale, Object... args) {
        Map<String, String> source = locale != null && locale.getLanguage().startsWith("zh") ? zhCn : enUs;
        String pattern = source.containsKey(key) ? source.get(key) : key;
        return MessageFormat.format(pattern, args);
    }
}
