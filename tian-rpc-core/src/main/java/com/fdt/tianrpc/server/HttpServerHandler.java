package com.fdt.tianrpc.server;

import com.fdt.RpcApplication;
import com.fdt.tianrpc.model.RpcRequest;
import com.fdt.tianrpc.model.RpcResponse;
import com.fdt.tianrpc.registry.LocalRegistry;
import com.fdt.tianrpc.serializer.JdkSerializer;
import com.fdt.tianrpc.serializer.Serializer;
import com.fdt.tianrpc.serializer.SerializerFactory;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * http请求处理
 */
@Slf4j
public class HttpServerHandler implements Handler<HttpServerRequest> {

    /**
     * 处理请求
     * @param request
     */
    @Override
    public void handle(HttpServerRequest request) {

        // 指定序列化器
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());
        log.debug(String.format("http请求处理使用%s序列化器",serializer.toString()));
        //记录日志
        System.out.println("Received request" +
                "method = " + request.method() + " " +
                "uri = " + request.uri());

        // 异步处理 HTTP 请求
        request.bodyHandler(body ->{
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            try {
                // 构造请求体对象
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 构造响应结果对象
            RpcResponse rpcResponse = new RpcResponse();
            // 如果请求为空，直接返回
            if (rpcRequest == null) {
                rpcResponse.setMessage("rpcRequest is null");
                doResponse(request, rpcResponse, serializer);
                return;
            }

            try{
                // 获取消费者要调用的服务实现类，并通过反射调用
                // 根据服务名获取实现类
                Class<?> impClass = LocalRegistry.get(rpcRequest.getServiceName());
                // 根据方法名和请求参数类型列表获取实现类的对应方法
                Method method = impClass.getMethod(rpcRequest.getMethodName(),rpcRequest.getParameterTypes());

                //自 Java 9 以后，Class.newInstance() 被标记为过时，并建议使用 Constructor 来替代。
                //这是因为 newInstance() 不支持异常处理，且其内部调用的 Constructor 可能抛出更多类型的异常。

                // 获取构造器，
                Constructor<?> constructor = impClass.getConstructor();  // 获取无参构造器
                // 创建constructor对象，要通过反射调用方法，需要传递类的实例
                Object instance = constructor.newInstance();
                // 传递参数列表，调用对应方法，获取结果
                Object result = method.invoke(instance,rpcRequest.getArgs());

                //封装返回结果
                rpcResponse.setData(result);
                rpcResponse.setDataType(result.getClass());
                rpcResponse.setMessage("success");

            }catch (Exception e){
                // 打印堆栈信息，帮助开发者追踪错误，解决问题
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }
            // 响应结果
            doResponse(request, rpcResponse, serializer);
        });
    }

    /**
     * 响应结果
     * @param request 请求对象
     * @param rpcResponse 响应对象
     * @param serializer 序列化器
     */
    private void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {
        HttpServerResponse httpServerResponse = request.response()
                .putHeader("content-type","application/json");

        try{
            byte[] serialized = serializer.serialize(rpcResponse);
            // 在 Vert.x 中，Buffer 用于处理和存储二进制数据，特别适合于高并发的异步 I/O 场景。
            httpServerResponse.end(Buffer.buffer(serialized));
        } catch (IOException e) {
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }
    }
}
