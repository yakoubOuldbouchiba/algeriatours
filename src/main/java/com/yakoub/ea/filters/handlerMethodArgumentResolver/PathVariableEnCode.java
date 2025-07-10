package com.yakoub.ea.filters.handlerMethodArgumentResolver;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PathVariableEnCode {
    String value() default "";
}
