package com.fdt.tianrpc.config;

import com.fdt.tianrpc.serializer.SerializerKeys;
import lombok.Data;

/**
 * RPC配置类
 */
@Data
public class RpcConfig {

    /**
     * 名称
     */
    private String name = "tian-rpc";

    /**
     * 版本号
     */
    private String version = "1.0.0";

    /**
     * 服务器地址
     */
    private String serverHost = "localhost";

    /**
     * 服务器端口
     */
    private int serverPort = 8080;

    /**
     * Mock 开关，默认关闭(Mock，模拟调用)
     */
    private boolean mock = false;

    /**
     * 序列化器
     */
    private String serializer = SerializerKeys.JDK;

    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig = new RegistryConfig();

}
