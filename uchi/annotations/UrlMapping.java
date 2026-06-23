package uchi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import uchi.utils.MethodEnum;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UrlMapping {
    String url();
    MethodEnum method() default MethodEnum.GET;
}
