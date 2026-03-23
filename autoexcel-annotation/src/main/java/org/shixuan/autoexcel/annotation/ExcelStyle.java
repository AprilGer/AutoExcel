package org.shixuan.autoexcel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelStyle {
    short backgroundColor() default -1;
    boolean bold() default false;
    short fontColor() default -1;
}
