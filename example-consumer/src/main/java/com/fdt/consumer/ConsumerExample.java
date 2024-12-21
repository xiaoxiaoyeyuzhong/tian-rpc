package com.fdt.consumer;

import com.fdt.common.model.User;
import com.fdt.common.service.UserService;
import com.fdt.tianrpc.config.RpcConfig;
import com.fdt.tianrpc.constant.RpcConstant;
import com.fdt.tianrpc.proxy.ServiceProxyFactory;
import com.fdt.tianrpc.utils.ConfigUtils;

/**
 * @author : fdt
 * 使用扩展的RPC框架的消费者示例
 */
public class ConsumerExample {

    public static void main(String[] args){
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        System.out.println(rpc);

        // 获取UserService的实现类对象
        // 通过动态代理获取方法实现类
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("fdt");
        // 调用UserService的getUser方法
        User newUser = userService.getUser(user);
        if(newUser != null){
            System.out.println("用户名：" + newUser.getName());
        }else{
            System.out.println("user==null");
        }

        long number = userService.getNumber();
        System.out.println("number:" + number);
    }


}
