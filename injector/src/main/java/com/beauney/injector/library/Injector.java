package com.beauney.injector.library;

import android.content.Context;
import android.view.View;

import com.beauney.injector.library.annotation.ContentView;
import com.beauney.injector.library.annotation.ViewInject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author zengjiantao
 * @since 2020-08-19
 */
public class Injector {
    /**
     * 依赖注入总入口
     *
     * @param context
     */
    public static void inject(Context context) {
        //必须要先注入layout，再注入view
        injectLayout(context);
        injectView(context);
    }

    /**
     * 注入layout
     *
     * @param context
     */
    private static void injectLayout(Context context) {
        Class<?> clazz = context.getClass();
        //拿到Activity类上面的注解
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if (contentView != null) {
            int layoutId = contentView.value();
            try {
                Method method = clazz.getMethod("setContentView", int.class);
                method.invoke(context, layoutId);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 注入view
     *
     * @param context
     */
    private static void injectView(Context context) {
        Class<?> clazz = context.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ViewInject viewInject = field.getAnnotation(ViewInject.class);
            if (viewInject != null) {
                int viewId = viewInject.value();
                try {
                    Method method = clazz.getMethod("findViewById", int.class);
                    View view = (View) method.invoke(context, viewId);
                    field.setAccessible(true);
                    field.set(context, view);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
