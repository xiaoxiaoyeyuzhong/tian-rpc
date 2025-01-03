package com.fdt.tianrpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.fdt.RpcApplication;
import com.fdt.tianrpc.config.RpcConfig;
import com.fdt.tianrpc.constant.RpcConstant;
import com.fdt.tianrpc.model.RpcRequest;
import com.fdt.tianrpc.model.RpcResponse;
import com.fdt.tianrpc.model.ServiceMetaInfo;
import com.fdt.tianrpc.registry.Registry;
import com.fdt.tianrpc.registry.RegistryFactory;
import com.fdt.tianrpc.serializer.Serializer;
import com.fdt.tianrpc.serializer.SerializerFactory;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 动态代理
 */
@Slf4j
public class ServiceProxy implements InvocationHandler {
    // 当调用代理对象的方法时，会转为调用invoke方法
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
        Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());
        log.debug(String.format("动态代理使用%s序列化器",serializer.toString()));
        // 构建请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        // 序列化请求，发送请求
        try{
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            // 从注册中心获取服务地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            // todo 服务版本现在使用的是默认值,应该从配置文件中获取
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if(CollUtil.isEmpty(serviceMetaInfoList)){
                throw new RuntimeException("未发现服务");
            }
            // todo 现在直接取了第一个服务地址，后续需要负载均衡
            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfoList.get(0);

            // 发送请求
            try(HttpResponse httpResponse = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress())
                        .body(bodyBytes)
                        .execute()){
                byte[] result = httpResponse.bodyBytes();
                // 反序列化响应结果
                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                // 将结果转换成User类实例，返回结果
                return rpcResponse.getData();
            } catch (Exception e){
                e.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
