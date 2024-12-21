package com.fdt.provider;


import com.fdt.common.model.User;
import com.fdt.common.service.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public User getUser(User user) {
        System.out.println("用户名称" + user.getName());
        return user;
    }

    @Override
    public int getNumber() {
        return 1;
    }
}
