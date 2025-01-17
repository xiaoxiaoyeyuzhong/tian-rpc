package com.fdt.tianrpc.config;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

@Data
public class RegistryConfig {

    /**
     * 注册中心类型
     */
    private String registry = "etcd";

    /**
     * 注册中心地址,etcd默认和客户端交互的端口是2379
     */
    private String address = "http://localhost:2379";

    /**
     * 注册中心用户名
     */
    private String username;

    /**
     * 注册中心密码
     */
    private String password;

    /**
     * 超时时间（单位毫秒）
     */
    private Long timeout = 10000L;

    /**
     * 获取完整服务地址，因为地址前缀可能会被忽略不写
     */

    public String getAddress(){
        if(!StrUtil.contains(address, "http")){
            return String.format("http://%s",address);
        }
        return String.format("%s",address);
    }

}
