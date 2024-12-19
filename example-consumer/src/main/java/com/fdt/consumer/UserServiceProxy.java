package com.fdt.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.fdt.common.model.User;
import com.fdt.common.service.UserService;
import com.fdt.tianrpc.model.RpcRequest;
import com.fdt.tianrpc.model.RpcResponse;
import com.fdt.tianrpc.serializer.JdkSerializer;
import com.fdt.tianrpc.serializer.Serializer;

/**
 * 静态代理
 */
public class UserServiceProxy implements UserService {

    @Override
    public User getUser(User user) {
        // 指定序列化器
        Serializer serializer = new JdkSerializer();
        // 构建请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .parameterTypes(new Class[]{User.class}) //将User类传入，通过反射获取方法参数类型
                .args(new Object[]{user})
                .build();

        // 序列化请求，发送请求
        try{
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            byte[] result;
            try{
                HttpResponse httpresponse = HttpRequest.post("http://localhost:8080")
                        .body(bodyBytes)
                        .execute();
                result = httpresponse.bodyBytes();
                // 反序列化响应结果
                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                // 将结果转换成User类实例，返回结果
                return (User) rpcResponse.getData();
            }catch (Exception e){
                e.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
