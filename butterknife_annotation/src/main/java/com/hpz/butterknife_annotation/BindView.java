package com.hpz.butterknife_annotation;

import androidx.annotation.IdRes;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.SOURCE;
import static java.lang.annotation.ElementType.FIELD;

/**
 * Created by hpz on 2020/8/31.
 */
@Retention(SOURCE)
@Target(FIELD)
public @interface BindView {
    /**
     * View ID to which the field will be bound.
     */
    @IdRes int value();

}
