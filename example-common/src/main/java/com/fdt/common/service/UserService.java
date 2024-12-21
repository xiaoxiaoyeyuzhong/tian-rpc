package com.fdt.common.service;

import com.fdt.common.model.User;

public interface UserService {

    /**
     * 获取用户信息
     * @param user
     * @return User
     */
    User getUser(User user);

    /**
     * 返回数字
     */
    int getNumber();
}
