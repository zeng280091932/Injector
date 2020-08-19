package com.beauney.injector.library.annotation;

import android.view.View;
import android.widget.AdapterView;

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
        listenerSetter = "setOnItemClickListener",
        listenerType = AdapterView.OnItemClickListener.class,
        callBackMethod = "onItemClick"
)
public @interface OnItemClick {
    int[] value() default -1;
}
