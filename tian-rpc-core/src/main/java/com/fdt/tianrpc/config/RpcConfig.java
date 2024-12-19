package com.fdt.tianrpc.config;

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

}
