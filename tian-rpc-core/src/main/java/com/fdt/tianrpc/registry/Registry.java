package com.fdt.tianrpc.registry;

import com.fdt.tianrpc.config.RegistryConfig;
import com.fdt.tianrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface Registry {

    /**
     * 初始化
     * @param registryConfig 注册中心配置
     */
    void init(RegistryConfig registryConfig) throws Exception;

    /**
     * 服务注册(提供者)
     * @param serviceMetaInfo 服务元信息
     * @throws Exception 注册异常
     */
    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
     * 服务注销(提供者)
     * @param serviceMetaInfo 服务元信息
     */
    void unRegister(ServiceMetaInfo serviceMetaInfo) throws ExecutionException, InterruptedException;

    /**
     * 服务发现(消费者)
     * @param serviceKey 服务key
     * @return 服务元信息列表
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

    /**
     * 注册中心销毁
     */
    void destroy();
}
