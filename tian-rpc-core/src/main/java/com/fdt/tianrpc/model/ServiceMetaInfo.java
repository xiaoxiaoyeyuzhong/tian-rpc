package com.fdt.tianrpc.model;

import cn.hutool.core.util.StrUtil;
import com.fdt.tianrpc.constant.RpcConstant;
import lombok.Data;

/**
 * 服务元信息（注册信息）
 */
@Data
public class ServiceMetaInfo {

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务版本
     */
    private String serviceVersion = RpcConstant.DEFAULT_SERVICE_VERSION;

    /**
     * 服务域名
     */
    private String serviceHost;

    /**
     * 服务端口
     */
    private Integer servicePort;

    /**
     * todo 服务分组
     */
    private String serviceGroup = "default";

    /**
     * 获取服务的键
     */
    public String getServiceKey(){
        // todo 扩展服务分组
        return String.format("%s:%s", serviceName, serviceVersion);
    }


    /**
     * 获取服务节点的键
     */
    public String getServiceNodeKey(){
        return String.format("%s/%s:%s",getServiceKey(), serviceHost, servicePort);
    }

    /**
     * 获取完整服务地址，因为地址前缀可能会被忽略不写
     */
    public String getServiceAddress(){
        if(!StrUtil.contains(serviceHost, "http")){
            return String.format("http://%s:%s", serviceHost, servicePort);
        }
        return String.format("%s:%s", serviceHost, servicePort);
    }
}

