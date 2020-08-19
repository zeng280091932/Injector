package com.beauney.injector.library.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zengjiantao
 * @since 2020-08-19
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventBase(
        listenerSetter = "setOnLongClickListener",
        listenerType = View.OnLongClickListener.class,
        callBackMethod = "onLongClick"
)
public @interface OnLongClick {
    int[] value() default -1;
}
