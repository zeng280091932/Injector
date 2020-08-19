package com.beauney.injector.library.proxy;

import android.content.Context;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author zengjiantao
 * @since 2020-08-19
 */
public class ListenerInvocationHandler implements InvocationHandler {

    private Context mContext;

    private Map<String, Method> mMethodMap;

    public ListenerInvocationHandler(Context context, Map<String, Method> methodMap) {
        mContext = context;
        mMethodMap = methodMap;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        Method methodProxy = mMethodMap.get(methodName);
        if (methodProxy != null) {
            return methodProxy.invoke(mContext, args);
        } else {
            return method.invoke(proxy, args);
        }
    }
}
