package com.fdt.tianrpc.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

import static java.lang.System.*;

public class VertxHttpServer extends AbstractVerticle implements HttpServer {

    //    private final int port;
//
//    public VertxHttpServer(int port) {
//        this.port = port;
//    }
    public void doStart(int port) {

        // 创建Vert.x 实例
        Vertx vertx = Vertx.vertx();

        // 创建HTTP服务器
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        // 监听指定端口并处理请求
        server.requestHandler(new HttpServerHandler());

        // 启动HTTP服务器，开始监听指定的端口
        server.listen(port, result -> {
            if (result.succeeded()) {
                out.println("HTTP server started on port " + port);
            } else {
                out.println("Failed to start HTTP server");
            }
        });

    }

}
