package com.search.ai.annotations;

import com.search.ai.constants.SearchStrategy;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface StrategyType {
    SearchStrategy value();
}
