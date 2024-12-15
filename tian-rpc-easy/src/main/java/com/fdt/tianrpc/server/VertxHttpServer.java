package com.fdt.tianrpc.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

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

        // 处理请求
        server.requestHandler(request -> {
            System.out.println("Received request" +
                    "method = " + request.method() + " " +
                    "uri = " + request.uri());

            request.response()
                    .putHeader("Content-Type", "text/plain")
                    .end("Hello from Vert.x HTTP server");
        });
        // 启动HTTP服务器，监听指定的端口
        server.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("HTTP server started on port " + port);
            } else {
                System.out.println("Failed to start HTTP server");
            }
        });

    }

}
