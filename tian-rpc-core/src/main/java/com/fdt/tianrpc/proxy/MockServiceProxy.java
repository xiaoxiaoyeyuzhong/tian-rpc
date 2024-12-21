package com.fdt.tianrpc.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Mock服务代理(使用JDK动态代理实现)
 */
@Slf4j
public class MockServiceProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 根据方法的返回值类型，生成特定的返回值
        Class<?> methodReturnType = method.getReturnType();
        log.info("mock invoke {}", methodReturnType);
        return getDefaultObject(methodReturnType);
    }

    /**
     * 根据方法返回值类型生成默认返回值
     * @param methodReturnType
     * @return
     */
    private Object getDefaultObject(Class<?> methodReturnType) {

        // 基本类型
        if(methodReturnType.isPrimitive()){
            if(methodReturnType == boolean.class){
                return false;
            }
            if(methodReturnType == byte.class){
                return (byte)0;
            }
            if(methodReturnType == short.class){
                return (short)0;
            }
            if(methodReturnType == int.class){
                return 0;
            }
            if(methodReturnType == long.class){
                return 0L;
            }
            if(methodReturnType == float.class){
                return 0.0f;
            }
            if(methodReturnType == double.class){
                return 0.0d;
            }
        }
        // 对象类型
        return null;
        
    }
}
