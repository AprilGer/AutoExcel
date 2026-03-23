package org.shixuan.autoexcel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelColumn {
    String[] headers();
    int order() default Integer.MAX_VALUE;
    boolean required() default false;
    String format() default "";
    Class<?> converter() default Void.class;
    Class<?> validator() default Void.class;
    String i18nKey() default "";
}
