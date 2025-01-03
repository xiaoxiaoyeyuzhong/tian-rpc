package com.fdt.tianrpc.registry;

import com.fdt.tianrpc.config.RegistryConfig;
import com.fdt.tianrpc.model.ServiceMetaInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class RegistryTest {

    final Registry registry = new EtcdRegistry();

    @Before
    public void init() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("http://localhost:2379");
        registry.init(registryConfig);
    }

    @Test
    public void register() throws Exception {
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        // 模拟服务提供者注册服务信息

        // 服务1
        serviceMetaInfo.setServiceName("fdtService");
        serviceMetaInfo.setServiceVersion("1.0.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1234);
        registry.register(serviceMetaInfo);

        serviceMetaInfo = new ServiceMetaInfo();
        //服务2
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("1.0.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1235);
        registry.register(serviceMetaInfo);

        serviceMetaInfo = new ServiceMetaInfo();
        //服务1的2.0版本
        serviceMetaInfo.setServiceName("fdtService");
        serviceMetaInfo.setServiceVersion("2.0.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1236);
        registry.register(serviceMetaInfo);
    }

    @Test
    public void unRegister() throws ExecutionException, InterruptedException {
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("fdtService");
        serviceMetaInfo.setServiceVersion("1.0.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1234);
        // 注销服务，记得kvClient.delete要加入get()，否则删除还未完成就结束了
        registry.unRegister(serviceMetaInfo);
    }

    @Test
    public void serviceDiscovery() {
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("fdtService");
        serviceMetaInfo.setServiceVersion("1.0.0");
        String serviceKey = serviceMetaInfo.getServiceKey();
        List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceKey);
        Assert.assertFalse(serviceMetaInfoList.isEmpty());
    }


}
