package com.fdt.provider;

import com.fdt.RpcApplication;
import com.fdt.common.service.UserService;
import com.fdt.tianrpc.registry.LocalRegistry;
import com.fdt.tianrpc.server.HttpServer;
import com.fdt.tianrpc.server.VertxHttpServer;
import io.vertx.core.Vertx;

/**
 * @author fdt
 * 简易的服务提供者示例
 */
public class EasyProviderExample {
    public static void main(String[] args) {

        // RPC框架初始化
        RpcApplication.init();

       // 注册服务
        LocalRegistry.register(UserService.class.getName(),UserServiceImpl.class);

        // 启动web服务
        HttpServer httpServer = new VertxHttpServer();
//        httpServer.doStart(8080);
        // 使用配置文件的服务器端口,当RpcApplication实例为空，getRpcConfig()方法会进行注册
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
        // 创建Vert.x 实例并部署Verticle
//        Vertx vertx = Vertx.vertx();
//        vertx.deployVerticle(new VertxHttpServer(), res -> {
//            if (res.succeeded()) {
//                System.out.println("Verticle deployed successfully");
//            } else {
//                System.out.println("Failed to deploy Verticle: " + res.cause());
//            }
//        });

    }
}
