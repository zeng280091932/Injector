package com.beauney.injector.library;

import android.content.Context;
import android.view.View;

import com.beauney.injector.library.annotation.ContentView;
import com.beauney.injector.library.annotation.EventBase;
import com.beauney.injector.library.annotation.ViewInject;
import com.beauney.injector.library.proxy.ListenerInvocationHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

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
        injectEvent(context);
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

    /**
     * 注入事件
     *
     * @param context
     */
    private static void injectEvent(Context context) {
        Class<?> clazz = context.getClass();
        //获取Activity里面 所有方法
        Method[] methods = clazz.getDeclaredMethods();
        //遍历Activity所有方法
        for (Method method : methods) {
            //获取方法上所有的注解
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                //获取注解 annotationType   OnClick  OnLongClick
                Class<?> annotationType = annotation.annotationType();
                EventBase eventBase = annotationType.getAnnotation(EventBase.class);
                if (eventBase == null) {
                    continue;
                }

                //开始获取事件三要素  通过反射注入进去
                String listenerSetter = eventBase.listenerSetter();
                Class<?> listenerType = eventBase.listenerType();
                String callBackMethod = eventBase.callBackMethod();

                Map<String, Method> methodMap = new HashMap<>();
                methodMap.put(callBackMethod, method);

                try {
                    Method valueMethod = annotationType.getMethod("value");
                    int[] viewIds = (int[]) valueMethod.invoke(annotation);
                    for (int viewId : viewIds) {
                        Method findViewById = clazz.getMethod("findViewById", int.class);
                        View view = (View) findViewById.invoke(context, viewId);
                        if (view == null) {
                            continue;
                        }

                        Method listenerSetterMethod = view.getClass().getMethod(listenerSetter, listenerType);
                        //通过动态代理生成listenerType（例如：OnClickListener）类
                        ListenerInvocationHandler listenerInvocationHandler = new ListenerInvocationHandler(context, methodMap);
                        Object proxy = Proxy.newProxyInstance(
                                listenerType.getClassLoader(),
                                new Class[]{listenerType},
                                listenerInvocationHandler);

                        //类比 于  textView.setOnClickListener(new View.OnClickListener)......
                        listenerSetterMethod.invoke(view, proxy);
                    }
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
