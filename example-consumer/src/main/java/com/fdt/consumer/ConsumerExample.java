package com.fdt.consumer;

import com.fdt.tianrpc.config.RpcConfig;
import com.fdt.tianrpc.constant.RpcConstant;
import com.fdt.tianrpc.utils.ConfigUtils;

/**
 * @author : fdt
 * 使用扩展的RPC框架的消费者示例
 */
public class ConsumerExample {

    public static void main(String[] args){
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        System.out.println(rpc);
    }
}
