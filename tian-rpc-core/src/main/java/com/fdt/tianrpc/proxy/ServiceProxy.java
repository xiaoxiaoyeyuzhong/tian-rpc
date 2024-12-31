package com.fdt.tianrpc.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.fdt.RpcApplication;
import com.fdt.tianrpc.model.RpcRequest;
import com.fdt.tianrpc.model.RpcResponse;
import com.fdt.tianrpc.serializer.Serializer;
import com.fdt.tianrpc.serializer.SerializerFactory;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 动态代理
 */
@Slf4j
public class ServiceProxy implements InvocationHandler {
    // 当调用代理对象的方法时，会转为调用invoke方法
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
        Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());
        log.debug(String.format("动态代理使用%s序列化器",serializer.toString()));
        // 构建请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        // 序列化请求，发送请求
        try{
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            // todo 使用注册中心和服务发现机制，获取服务地址
            try(HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")
                        .body(bodyBytes)
                        .execute()){
                byte[] result = httpResponse.bodyBytes();
                // 反序列化响应结果
                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                // 将结果转换成User类实例，返回结果
                return rpcResponse.getData();
            } catch (Exception e){
                e.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
