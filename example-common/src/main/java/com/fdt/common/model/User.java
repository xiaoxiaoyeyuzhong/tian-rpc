package com.fdt.common.model;

import java.io.Serializable;

/**
 * @author fdt
 * 用户实体类,继承 Serializable接口,方便序列化
 */
public class User implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }
}
