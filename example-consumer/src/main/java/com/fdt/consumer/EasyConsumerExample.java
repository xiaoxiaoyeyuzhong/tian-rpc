package com.fdt.consumer;

import com.fdt.common.model.User;
import com.fdt.common.service.UserService;
import com.fdt.tianrpc.proxy.ServiceProxyFactory;

/**
 * @author fdt
 * 简易消费者示例
 */
public class EasyConsumerExample {

    public static void main(String[] args)
    {
       // 获取UserService的实现类对象
        // 通过静态代理获取方法实现类
        UserService userService = new UserServiceProxy();
        User user = new User();
        user.setName("fdt");
        // 调用UserService的getUser方法
        User newUser = userService.getUser(user);
        if(newUser != null){
            System.out.println("用户名：" + newUser.getName());
        }else{
            System.out.println("user==null");
        }
    }
}
