package com.fdt.consumer;

import com.fdt.common.model.User;
import com.fdt.common.service.UserService;

/**
 * @author fdt
 * 简易消费者示例
 */
public class EasyConsumerExample {

    public static void main(String[] args)
    {
       // todo 获取UserService的实现类对象
        UserService userService = null;
        User user = new User();
        user.setName("fdt");
        // todo 调用UserService的getUser方法
        User newUser = userService.getUser(user);
        if(newUser != null){
            System.out.println("用户名：" + newUser.getName());
        }else{
            System.out.println("user==null");
        }
    }
}
