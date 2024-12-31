package com.fdt.tianrpc.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdt.tianrpc.model.RpcRequest;
import com.fdt.tianrpc.model.RpcResponse;

import java.io.IOException;

/**
 * JSON序列化器
 */
public class JsonSerializer implements Serializer{
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public <T> byte[] serialize(T object) throws IOException {
        return OBJECT_MAPPER.writeValueAsBytes(object);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        T object = OBJECT_MAPPER.readValue(bytes, type);
        if(object instanceof RpcRequest){
            return handleRequest((RpcRequest)object, type);
        }
        if(object instanceof RpcResponse){
            return handleResponse((RpcResponse)object, type);
        }
        return object;
    }

    /**
     * 处理请求
     * 由于Object的原始对象会被擦除，导致反序列化时会被作为LinkedHashMap无法转换成原始对象
     * 因此做了特殊处理
     *
     * @param request 请求对象
     * @param type 目标类型
     * @return T
     * @param <T>
     * @throws IOException 读取字节数组异常
     */
    private<T> T handleRequest(RpcRequest request, Class<T> type) throws IOException {
        Class<?>[] parameterTypes = request.getParameterTypes();  // 获取RPC请求中参数的类型
        Object[] args = request.getArgs();  // 获取RPC请求中实际的参数值,获取的是引用而不是拷贝

        // 循环处理每一个参数的类型
        for(int i = 0; i < parameterTypes.length; i++){
            Class<?> clazz = parameterTypes[i];  // 获取当前参数的期望类型
            // 如果当前参数的实际类型与期望类型不一致，则需要进行类型转换
            if(!clazz.isAssignableFrom(args[i].getClass())){
                byte[] argsBytes = OBJECT_MAPPER.writeValueAsBytes(args[i]);  // 将参数转换为字节数组（JSON）
                args[i] = OBJECT_MAPPER.readValue(argsBytes, clazz);  // 将字节数组反序列化为期望类型的对象
            }
        }
        return type.cast(request);  // 将RpcRequest对象强制转换为传入的类型并返回
    }

    /**
     * 处理响应
     * @param response 响应对象
     * @param type 参数类型
     * @return
     * @param <T>
     * @throws IOException 读取字节数组异常
     */
    private<T> T handleResponse(RpcResponse response, Class<T> type) throws IOException {
        // 处理响应数据
        byte[] dataBytes = OBJECT_MAPPER.writeValueAsBytes(response.getData()); // 将数据转换成字节数组
        response.setData(OBJECT_MAPPER.readValue(dataBytes, response.getDataType())); // 将字节数组反序列化为期望类型的对象
        return type.cast(response);
    }



}
