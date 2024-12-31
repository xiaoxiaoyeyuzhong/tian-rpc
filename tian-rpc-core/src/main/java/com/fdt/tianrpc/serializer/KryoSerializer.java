package com.fdt.tianrpc.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


public class KryoSerializer implements Serializer{

    private static final ThreadLocal<Kryo> KRYO_THREAD_LOCAL = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        // 设置动态类注册为false，不提前注册所有类，防止安全问题
        kryo.setRegistrationRequired(false);
        return kryo;
    });

    @Override
    public <T> byte[] serialize(T object){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 创建输出流
        Output output = new Output(byteArrayOutputStream);
        // 将对象序列化写入输出流
        KRYO_THREAD_LOCAL.get().writeObject(output, object);
        output.close();
        // 返回序列化后的字节数组
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type){
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        // 创建输入流
        Input input =new Input(byteArrayInputStream);
        // 将字节数组反序列化为对象
        T result = KRYO_THREAD_LOCAL.get().readObject(input, type);
        input.close();
        return result;
    }
}