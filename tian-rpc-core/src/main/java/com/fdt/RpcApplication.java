package com.fdt;

import com.fdt.tianrpc.config.RegistryConfig;
import com.fdt.tianrpc.config.RpcConfig;
import com.fdt.tianrpc.constant.RpcConstant;
import com.fdt.tianrpc.registry.Registry;
import com.fdt.tianrpc.registry.RegistryFactory;
import com.fdt.tianrpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : fdt
 * RPC框架应用启动类
 * 也是holder，存放项目用到的变量，是双检锁单例模式的实现
 */
@Slf4j
public class RpcApplication {

    // 使用volatile关键字，保证线程安全
    private static volatile RpcConfig rpcConfig;

    /**
     * 框架初始化，支持传入自定义的配置
     * @param newRpcConfig Rpc配置
     */
    public static void init(RpcConfig newRpcConfig){
        rpcConfig = newRpcConfig;
        log.info("rpc init,config = {}",newRpcConfig.toString());

        // 注册中心初始化
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        try {
            registry.init(registryConfig);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("registry init,registry = {}",registryConfig.getRegistry());

    }

    /**
     * 初始化
     */
    public static void init(){
        RpcConfig newRpcConfig;
        try{
            // 读取配置文件,使用默认前缀
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e){
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    /**
     * 获取配置
     */
    public static RpcConfig getRpcConfig(){
        // 双检锁，保证线程安全
        if(rpcConfig == null){
            synchronized (RpcApplication.class){
                if(rpcConfig == null){
                    init();
                }
            }
        }
        return rpcConfig;
    }


}
