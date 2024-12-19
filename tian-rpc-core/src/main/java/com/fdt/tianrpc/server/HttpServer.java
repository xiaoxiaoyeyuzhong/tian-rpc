package com.fdt.tianrpc.server;

/**
 * Http服务器接口
 */
public interface HttpServer {

    /**
     * 统一的服务器启动方法
     */
    void doStart(int port);

}

