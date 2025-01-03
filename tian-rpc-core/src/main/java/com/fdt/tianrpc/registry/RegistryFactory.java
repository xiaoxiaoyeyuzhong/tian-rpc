package com.fdt.tianrpc.registry;

import com.fdt.tianrpc.spi.SpiLoader;

public class RegistryFactory {

    /**
     * 加载所有的注册中心实现
     */
    static {
        SpiLoader.load(Registry.class);
    }

    /**
     * 默认注册中心
     */
    private static final Registry DEFAULT_REGISTRY = new EtcdRegistry();


    /**
     * 获取注册中心实例
     */
    public static Registry getInstance(String key) {
        // 获取指定key的注册中心实例，如果没有则返回默认的注册中心
        return SpiLoader.getInstance(Registry.class, key) == null ?
                DEFAULT_REGISTRY : SpiLoader.getInstance(Registry.class, key);
    }
}

