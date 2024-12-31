package com.fdt.tianrpc.serializer;

import com.fdt.tianrpc.spi.SpiLoader;

public class SerializerFactory {

    /**
     * 类加载的时候默认加载序列化器的所有实现类
     */
    static {
        SpiLoader.load(Serializer.class);
    }

    /**
     * 默认序列化器
     */
    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();


    /**
     * 获取序列化器实例
     */
    public static Serializer getInstance(String key) {
        // 获取指定key的序列化器实例，如果没有则返回默认的序列化器
        return SpiLoader.getInstance(Serializer.class, key) == null ?
                DEFAULT_SERIALIZER : SpiLoader.getInstance(Serializer.class, key);
    }
}

