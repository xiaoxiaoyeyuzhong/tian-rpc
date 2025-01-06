package com.fdt.provider;

import com.fdt.RpcApplication;
import com.fdt.common.service.UserService;
import com.fdt.tianrpc.config.RegistryConfig;
import com.fdt.tianrpc.config.RpcConfig;
import com.fdt.tianrpc.model.ServiceMetaInfo;
import com.fdt.tianrpc.registry.LocalRegistry;
import com.fdt.tianrpc.registry.Registry;
import com.fdt.tianrpc.registry.RegistryFactory;
import com.fdt.tianrpc.server.HttpServer;
import com.fdt.tianrpc.server.VertxHttpServer;

public class ProviderExample {
    public static void main(String[] args) {
        // RPC框架初始化
        RpcApplication.init();

        // 本地服务注册
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);

        //注册服务到注册中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();

        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());

        try{
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // web服务启动
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(rpcConfig.getServerPort());
    }
}
