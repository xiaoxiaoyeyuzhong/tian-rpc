# 1. 什么是RPC

1. Remote Procedure Call，远程过程调用，一种计算机通信协议，底层可以是http，也可以是tcp
2. 重点是让用户像调用本地方法一样调用远程的服务。你不需要关心服务的地址在哪，要如何操作，才能调用服务，只需要像点外卖一样，下单，得到服务，简单，轻松。
3. 在公司，部门之间要调用服务，肯定是引入一个sdk，配置一下就能使用最方便，当其他部门的服务变更了，只要方法名，参数没变，你的调用就不用变。
4. 一般RPC用于服务器之间的通信，因为服务器之间的通信要求性能较高



# 2.RPC引入

## 2.1 开始

​		我们一开始写代码，如果用到别的服务器的代码服务，会发送网络请求，写接口地址，参数等信息，然后得到请求。

## 2.2 改进

​		我不想记其他服务器的地址，而且有时候地址一直变，也不好记，那能不能有一个统一的接口，不变，我要什么服务都请求它，然后通过参数告诉它我要什么服务，不是方便多了。

## 2.3 RPC

​		那能不能再简单点，网络请求由代理对象发送，我只要说明我需要什么服务，传递什么参数，就可以得到服务。但是要实现RPC，我们要解决许多问题。

### 2.4.1 服务注册发现

服务的消费者怎么知道有这个服务呢？那我们要有一个注册中心，让服务的提供者把服务信息注册到注册中心中，这样消费者就知道有这个服务了，由注册中心保存服务的地址，消费者只要知道服务存在即可。

### 2.4.2 负载均衡

消费者通过某种机制，从多个提供相同服务的提供者中选择一个可用的。

### 2.4.3 容错机制

失败了怎么办？可以重试，或者降级。

# 3. 工具的版本

1. java>=11，在idea的文件-项目结构-SDK可以选择项目的java版本

看到55分钟。

# 4. 简易版RPC实现

## 4.1 项目创建

使用Spring Initializr创建，创建完毕后修改pom.xml的spring，java版本，然后右键模块，点击模块设置，设置SDK为11.

### 4.1.1 example-common

```xml
不要这个，我们不是web应用
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.7.0</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.fdt</groupId>
    <artifactId>example-common</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>example-common</name>
    <description>example-common</description>
    <url/>
    <licenses>
        <license/>
    </licenses>
    <developers>
        <developer/>
    </developers>
    <scm>
        <connection/>
        <developerConnection/>
        <tag/>
        <url/>
    </scm>
    <properties>
        <java.version>11</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

</project>

```

### 4.1.2 example-consumer

### 4.1.3 example-provider

```xml
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>com.fdt</groupId>
            <artifactId>example-common</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>

        <dependency>
            <groupId>com.fdt</groupId>
            <artifactId>tian-rpc-easy</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>

        <!--https://hutool.cn/docs/#/ 网络请求发送工具包-->
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>5.8.16</version>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <!--可选，其他模块引用了本模块，不会自动引入lombok-->
            <optional>true</optional>
            <!--编译可用，运行时外部提供-->
            <scope>provided</scope>
        </dependency>

    </dependencies>
```



### 4.1.4 tian-rpc-easy

1. 4. 

2. pom.xml

   ```xml
   不要这个
   <build>
       <plugins>
           <plugin>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-maven-plugin</artifactId>
           </plugin>
       </plugins>
   </build>
   ```

   

   ```xml
       <dependencies>
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter</artifactId>
           </dependency>
   
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-test</artifactId>
               <scope>test</scope>
           </dependency>
   
           <dependency>
               <groupId>io.vertx</groupId>
               <artifactId>vertx-core</artifactId>
               <version>4.5.1</version>
           </dependency>
   
           <!--https://hutool.cn/docs/#/ 网络请求发送工具包-->
           <dependency>
               <groupId>cn.hutool</groupId>
               <artifactId>hutool-all</artifactId>
               <version>5.8.16</version>
           </dependency>
   
           <dependency>
               <groupId>org.projectlombok</groupId>
               <artifactId>lombok</artifactId>
               <!--可选，其他模块引用了本模块，不会自动引入lombok-->
               <optional>true</optional>
               <!--编译可用，运行时外部提供-->
               <scope>provided</scope>
           </dependency>
   
       </dependencies>
   ```

   

3. 



## 4.2 Web服务器

### 4.2.1 Vert.x

#### 4.2.1.1 介绍Vert.x

> [Vert.x Starter - Create new Eclipse Vert.x applications](https://start.vertx.io/)

```xml
     <dependency>
            <groupId>io.vertx</groupId>
            <artifactId>vertx-core</artifactId>
            <version>4.5.1</version>
        </dependency>
```

我们选择了高性能的Vert.X服务器，在它的官网上提到了自己的优点，区别于传统的基于阻塞I/O的堆栈和框架，可以用更少的资源处理更多请求。可以增加部署密度。

1. 基于非阻塞I/0堆栈和框架：不需要等别的请求完成再处理下一个请求。
2. 更少资源处理更多请求：由于非阻塞，所以一个线程可以处理多个并发请求，不需要像传统的框架为每个请求分配独立线程，节省性能，处理更多请求
3. 适合各种执行环境，比如虚拟机，容器，这种环境资源受限，节省性能的Vert.X就很适合它们。
4. 可以增加部署密度：使用Vert.X,由于使用的资源比别的服务器少，就可以部署更多实例。



#### 4.2.1.2 使用Vert.x

> tian-rpc-easy

1. HttpServer

   ```java
   public interface HttpServer {
   
       /**
        * 统一的服务器启动方法
        */
       void doStart(int port);
   
   }
   ```

   

2. VertxHttpServer.java

   1. 错误的代码

      ```java
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
      
              // 监听端口，处理请求
              server.requestHandler(request -> {
                  System.out.println("Received request" +
                          "method = " + request.method() + " " +
                          "uri = " + request.uri());
      
                  request.response()
                          .putHeader("Content-Type", "text/plain")
                          .end("Hello from Vert.x HTTP server");
      
                  // 启动HTTP服务器，监听指定的端口
                  server.listen(port, result -> {
                      if (result.succeeded()) {
                          System.out.println("HTTP server started on port " + port);
                      } else {
                          System.out.println("Failed to start HTTP server");
                      }
                  });
              });
          }
      
      }
      
      ```

      

   2. 正确的代码

      ```java
      package com.fdt.tianrpc.server;
      
      import io.vertx.core.Vertx;
      
      public class VertxHttpServer implements HttpServer {
      
          public void doStart(int port) {
              // 创建 Vert.x 实例
              Vertx vertx = Vertx.vertx();
      
              // 创建 HTTP 服务器
              io.vertx.core.http.HttpServer server = vertx.createHttpServer();
      
              // 处理请求
              server.requestHandler(request -> {
                  // 处理 HTTP 请求
                  System.out.println("Received request: " + request.method() + " " + request.uri());
      
                  // 发送 HTTP 响应
                  request.response()
                          .putHeader("content-type", "text/plain")
                          .end("Hello from Vert.x HTTP server!");
              });
      
              // 启动 HTTP 服务器并监听指定端口
              server.listen(port, result -> {
                  if (result.succeeded()) {
                      System.out.println("Server is now listening on port " + port);
                  } else {
                      System.err.println("Failed to start server: " + result.cause());
                  }
              });
          }
      }
      
      ```

      

   3. 注意，server.requestHandler(request -> {});和server.listen(port, result -> {});是分开的，不是谁包含谁的关系，顺序执行，但是回调只会在触发的时候执行，之前我把server.listen(port, result -> {});写在server.requestHandler(request -> {})里面，结果直接跳出去了，什么都不执行，可能是因为http服务器没启动去监听端口，要注意，如果括号或者逗号和教程不同，记得仔细比较，留个心眼。

> example-provider

1. 使用，测试localhost:8080

   ```java
   public class EasyProviderExample {
       public static void main(String[] args) {
          // todo 提供服务
           // 启动web服务
           HttpServer httpServer = new VertxHttpServer();
           httpServer.doStart(8080);
           }
   ```

   

2. 

## 4.3 本地服务注册器

> tian-rpc-easy

1. 使用线程安全的ConcurrentHashMap存储服务注册信息，key是服务名称，value是服务实现类，本地服务注册器和注册中心不要搞混，本地注册服务器侧重根据服务名称提供服务实现类，注册中心侧重服务管理和提供给消费者，要记录服务地址之类的

   ```java
   package com.fdt.tianrpc.registry;
   
   import java.util.Map;
   import java.util.concurrent.ConcurrentHashMap;
   
   /**
    * 本地注册中心
    */
   public class LocalRegistry {
   
       /**
        * 注册信息存储器
        */
       private static final Map<String,Class<?>> map = new ConcurrentHashMap<>();
   
       /**
        * 注册服务
        * @param serviceName 服务名称
        * @param impClass 服务实现类
        */
       public static void register(String serviceName,Class<?> impClass){
           map.put(serviceName,impClass);
       }
   
       /**
        * 获取服务
        * @param serviceName
        * @return 服务实现类
        */
       public static Class<?> get(String serviceName){
           return map.get(serviceName);
       }
   
       /**
        * 删除服务
        * @param serviceName 服务名称
        */
       public static void remove(String serviceName) {
           map.remove(serviceName);
       }
   }
   
   ```

   

2. 

## 4.4 序列化器

​		服务注册后，我们就要实现根据请求参数，取出消费者要调用的服务名称，获取提供者的服务这个逻辑了，但是首先我们要进行数据的序列化和反序列化。我们这里选择java原生的序列化器。序列化的目的是方便传输、存储和跨平台兼容，反序列化的目的是还原对象，进行进一步的操作。

- 序列化：将java对象转为可以网络传输的字节数组
- 反序列化：将字节数组转为java对象。

```java
package com.fdt.tianrpc.serializer;


import java.io.IOException;

/**
 * 序列化器接口
 */
public interface Serializer {

    /**
     * 序列化
     *
     * @param object
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> byte[] serialize(T object) throws IOException;

    /**
     * 反序列化
     *
     * @param bytes
     * @param type
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> T deserialize(byte[] bytes, Class<T> type) throws IOException;
}


```



```java
package com.fdt.tianrpc.serializer;

import java.io.*;

/**
 * JDK 序列化器
 */
public class JdkSerializer implements Serializer {

    /**
     * 序列化
     *
     * @param object
     * @param <T>
     * @return
     * @throws IOException
     */
    @Override
    public <T> byte[] serialize(T object) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.close();
        return outputStream.toByteArray();
    }

    /**
     * 反序列化
     *
     * @param bytes
     * @param type
     * @param <T>
     * @return
     * @throws IOException
     */
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        try {
            return (T) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            objectInputStream.close();
        }
    }
}


```

## 4.5 请求处理器——提供者处理调用

> Object和Class<?>的区别，前者表示类的实例，后者表示类对象，可以用于反射时动态获取类的属性，方法信息。

1. 请求类

   ```java
   package com.fdt.tianrpc.model;
   
   import lombok.AllArgsConstructor;
   import lombok.Builder;
   import lombok.Data;
   import lombok.NoArgsConstructor;
   
   import java.io.Serializable;
   
   @Data
   @Builder
   @AllArgsConstructor
   @NoArgsConstructor
   public class RpcRequest implements Serializable {
       /**
        * 服务名称
        */
       private String serviceName;
   
       /**
        * 方法名称
        */
       private String methodName;
   
       /**
        * 参数类型列表
        */
       private Class<?>[] parameterTypes;
   
       /**
        * 参数列表
        */
       private Object[] args;
   }
   
   ```

   

2. 响应类

   ```java
   package com.fdt.tianrpc.model;
   
   import lombok.AllArgsConstructor;
   import lombok.Builder;
   import lombok.Data;
   import lombok.NoArgsConstructor;
   
   import java.io.Serializable;
   
   @Data
   @Builder
   @AllArgsConstructor
   @NoArgsConstructor
   public class RpcResponse implements Serializable {
   
       /**
        * 响应数据
        */
       private Object data;
   
       /**
        * 响应数据类型
        */
       private Class<?> dataType;
   
       /**
        * 响应信息
        */
       private String message;
       /**
        * 错误信息
        */
       private String exception;
   }
   
   ```

   

3. HttpServerHandler

   1. 业务流程

      1. 反序列化请求数据为对象，从请求中获取服务名称信息
      2. 根据服务名称，在注册器中找到对应的服务实现类
      3. 通过反射机制调用服务实现类，获取调用结果
      4. 对结果进行封装和序列化，放到响应中。

   2. 代码

      1. HttpServerHandler.java

         ```java
         package com.fdt.tianrpc.server;
         
         import com.fdt.tianrpc.model.RpcRequest;
         import com.fdt.tianrpc.model.RpcResponse;
         import com.fdt.tianrpc.registry.LocalRegistry;
         import com.fdt.tianrpc.serializer.JdkSerializer;
         import com.fdt.tianrpc.serializer.Serializer;
         import io.vertx.core.Handler;
         import io.vertx.core.buffer.Buffer;
         import io.vertx.core.http.HttpServerRequest;
         import io.vertx.core.http.HttpServerResponse;
         
         import java.io.IOException;
         import java.lang.reflect.Constructor;
         import java.lang.reflect.Method;
         
         /**
          * http请求处理
          */
         public class HttpServerHandler implements Handler<HttpServerRequest> {
         
             /**
              * 处理请求
              * @param request
              */
             @Override
             public void handle(HttpServerRequest request) {
         
                 // 指定序列化器
                 final Serializer serializer = new JdkSerializer();
         
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
         
         ```

         

      2. VertxHttpServer

         ```java
         // 监听指定端口并处理请求
         server.requestHandler(new HttpServerHandler());
         ```

         

   3. 不同web服务器的请求处理器实现方式不同，Vert.x是实现Handler<HttpServerRequest>接口来自定义请求处理器，并通过request.bodyHandler处理请求

   

4. 

## 4.6 代理——消费者发起调用

​		我们做RPC框架，目的就是简化消费者调用服务的成本，所以不可能让消费者把实现类沾到项目中，或让消费者自己发起网络请求，所以我们要做代理，而代理分为静态代理和动态代理。

- 静态代理：为每一个特定类型的接口或对象，编写一个代理类，实现消费者要调用的接口，只是实现方式为发起网络请求。
- 动态代理

而消费者发起调用的流程如下

1. 构建请求体
2. 序列化请求体
3. 发送请求
4. 获取响应
5. 反序列化响应
6. 返回结果

### 4.6.1 静态代理

1. 代码

   ```java
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
   
   ```

   

2. 



### 4.6.2 动态代理

1. 通过反射机制和工厂模式，传入服务类，创建动态代理对象
2. 当消费者调用动态代理对象对应的服务类方法的时候，会转而调用代理对象的invoke()方法，与静态代理类似，这里就是开始进行序列化，请求，获取响应，反序列化的流程。
3. 比起静态代理，动态代理只需要写一个动态代理类和工厂类即可调用服务提供者对应类的方法了。

- ServiceProxy.java

```java
package com.fdt.tianrpc.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.fdt.tianrpc.model.RpcRequest;
import com.fdt.tianrpc.model.RpcResponse;
import com.fdt.tianrpc.serializer.JdkSerializer;
import com.fdt.tianrpc.serializer.Serializer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * JDK动态代理
 */
public class ServiceProxy implements InvocationHandler {
    // 当调用代理对象的方法时，会转为调用invoke方法
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
        Serializer serializer = new JdkSerializer();
        // 构建请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        // 序列化请求，发送请求
        try{
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            // todo 使用注册中心和服务发现机制，获取服务地址
            try(HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")
                        .body(bodyBytes)
                        .execute()){
                byte[] result = httpResponse.bodyBytes();
                // 反序列化响应结果
                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                // 将结果转换成User类实例，返回结果
                return rpcResponse.getData();
            } catch (Exception e){
                e.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

```

- ServiceProxyFactory.java

  ```java
  package com.fdt.tianrpc.proxy;
  
  import java.lang.reflect.Proxy;
  
  public class ServiceProxyFactory {
      /**
       * 根据服务类获取代理对象
       * @param serviceClass
       * @return
       * @param <T>
       */
      public static <T> T getProxy(Class<T> serviceClass){
          return (T) Proxy.newProxyInstance(
                  serviceClass.getClassLoader(),
                  new Class[]{serviceClass},
                  new ServiceProxy()
          );
      }
  }
  
  ```

  

- 

# 5. 扩展版RPC实现

## 5.1 全局配置加载

### 5.1.1 需求分析

​		在简易版的RPC框架中，我们写死了服务的地址，端口等信息，如果我们要给其他人使用这个RPC框架，这样做显然很麻烦，我们不能指望服务的消费者和提供者去修改我们的代码，它们往往只关心服务是否能正常注册或者获取。

​		那我们就要给服务消费者和提供者提供一种全局的自定义配置方式，比如编写配置文件，那我们要提供什么配置信息给它们呢？首先要有下面的几个，但是要注意，服务器不一定有主机名，是否应该加入ip地址的配置？

- name: RPC框架名称
- version: RPC框架版本号
- serverHost: 服务器主机名
- serverPort: 服务器端口号

后面可以加入下面的配置

- 注册中心地址
- 服务接口
- 序列化方式
- 网络通信协议
- 超时设置
- 负载均衡策略
- 服务端线程模型：如何处理客户端请求



### 5.1.2 项目初始化

复制rpc-easy项目，改名，加入父项目的模块中，

### 5.1.3 配置加载

- 

### 5.1.4 维护全局配置对象

​		使用设计模式的**单例模式**，在引入RPC项目的项目启动后加载一个全局对象，不重复创建对象，减少性能开销，这里使用了双检锁单例模式。

#### 5.1.4.1 双检锁单例模式

​		双检锁单例模式（Double-Checked Locking Singleton Pattern）是一种用于实现线程安全的单例模式的优化方式，主要用于在多线程环境下确保某个类只有一个实例。

​		在普通的单例模式中，通常需要通过锁（如 `synchronized`）来保证线程安全，但每次获取单例对象时都需要加锁，导致性能下降。双检锁单例模式通过减少加锁的次数来优化这一问题。

**双检锁单例模式的工作原理：**

1. **第一次检查**：在进入同步代码块之前，先检查实例是否已经创建。如果已经创建，直接返回单例对象，这样避免了每次都加锁的开销。
2. **加锁**：如果实例为空，才会进入同步块，在同步块内再次检查实例是否为空。第二次检查是必要的，因为在多线程环境下，多个线程可能会同时进入第一次检查并等待锁，因此需要在同步块内再检查一次，确保只有一个线程能够创建实例。



#### 5.1.4.2 volatile关键字

​		`volatile` 是 Java 中的一个关键字，用来声明一个变量为“易变的”，即该变量可能会被多个线程同时访问和修改。它确保变量的最新值在所有线程中都是可见的，解决了由于多线程环境下可能发生的缓存一致性问题。

**`volatile` 关键字的作用：**

1. **保证变量的可见性**：
   在多线程环境中，线程对变量的修改不会立即对其他线程可见，可能会导致多个线程操作一个变量时出现不一致的情况。使用 `volatile` 修饰的变量，保证当一个线程修改该变量的值时，其他线程能够立即看到这个修改。
2. **禁止指令重排**：
   `volatile` 还可以防止 JVM 对代码进行指令重排优化。指令重排是编译器和 CPU 的优化行为，可能会导致程序的执行顺序与代码中的顺序不一致，导致线程之间的同步问题。`volatile` 变量可以保证在写入变量之前，所有的操作都已经完成；在读取 `volatile` 变量之后，所有的操作都会按照程序中指定的顺序执行。

#### 5.1.4.3 代码

##### RPC

- RpcApplication.java(RPC项目的启动入口，也是holder)

```java
package com.fdt;

import com.fdt.tianrpc.config.RpcConfig;
import com.fdt.tianrpc.constant.RpcConstant;
import com.fdt.tianrpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : fdt
 * RPC框架应用启动类
 * 也是holder，存放项目用到的变量，是双检锁单例模式的实现
 */
@Slf4j
public class RpcApplication {

    // 使用volatile关键字，保证线程安全
    private static volatile RpcConfig rpcConfig;

    /**
     * 框架初始化，支持传入自定义的配置
     * @param newRpcConfig Rpc配置
     */
    public static void init(RpcConfig newRpcConfig){
        rpcConfig = newRpcConfig;
        log.info("rpc init,config = {}",newRpcConfig.toString());
    }

    /**
     * 初始化
     */
    public static void init(){
        RpcConfig newRpcConfig;
        try{// 读取配置文件,使用默认前缀
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e){
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    /**
     * 获取配置
     */
    public static RpcConfig getRpcConfig(){
        // 双检锁，保证线程安全
        if(rpcConfig == null){
            synchronized (RpcApplication.class){
                if(rpcConfig == null){
                    init();
                }
            }
        }
        return rpcConfig;
    }


}

```

- RpcConfig.java

  ```java
  package com.fdt.tianrpc.config;
  
  import lombok.Data;
  
  /**
   * RPC配置类
   */
  @Data
  public class RpcConfig {
  
      /**
       * 名称
       */
      private String name = "tian-rpc";
  
      /**
       * 版本号
       */
      private String version = "1.0.0";
  
      /**
       * 服务器地址
       */
      private String serverHost = "localhost";
  
      /**
       * 服务器端口
       */
      private int serverPort = 8080;
  
  }
  
  ```

  

- RpcConstant.java(记得写配置的时候不能写错，现在RpcApplication加载配置的的前缀是这个)

  ```java
  package com.fdt.tianrpc.constant;
  
  /**
   * RPC框架的相关变量
   */
  public interface RpcConstant {
  
      /**
       * 默认的配置前缀
       */
      String DEFAULT_CONFIG_PREFIX = "tian-rpc";
  }
  ```

  

##### provider

- EasyProviderExample.java

```java
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
        // 使用配置文件的服务器端口,当RpcApplication实例为空，getRpcConfig()方法会进行注册
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());

    }
}

```

- application.properties

```properties
tian-rpc.name=tianrpc
tian-rpc.version=2.0
tian-rpc.serverPort=8084
```



##### consumer

- ConsumerExample.java

```java
package com.fdt.consumer;

import com.fdt.tianrpc.config.RpcConfig;
import com.fdt.tianrpc.constant.RpcConstant;
import com.fdt.tianrpc.utils.ConfigUtils;

/**
 * @author : fdt
 * 使用扩展的RPC框架的消费者示例
 */
public class ConsumerExample {

    public static void main(String[] args){
        // 模拟使用默认前缀（即RpcConstant中定义的）
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        System.out.println(rpc);
    }
}

```

### 5.1.5 扩展

- 支持多种格式的配置文件
- 支持监听配置文件修改，自动更新配置对象
  - 使用Hutool工具类的props.autoLoad()
- 配置文件支持中文
- 支持配置提示，用户可以知道有哪些可配置项
- 配置分组
  - 嵌套配置类

## 5.2 接口Mock

### 5.2.1 需求分析

​		当我们使用RPC框架调用服务提供者的服务时，实际也要发生网络请求，也会遇到网络不畅通的时候，为了提高这时候服务消费者的体验，我们可以返回一些假数据，模拟远程的服务；

​		当我们真实的服务还未完全开发出来，但是服务消费者想要提前跑通调用流程，那之前我们会怎么做呢？把未完成的代码注释掉，返回null，但是使用mock，我们不需要注释代码，服务消费者可以自行调试调用流程，服务提供者继续开发服务。

​		既方便了服务消费者，也方便了服务提供者。

### 5.2.2 实现思路

​		我们之前是如何实现RPC远程调用的呢？通过工厂模式、反射、动态代理、及配置文件的配置，生成一个代理对象，去调用远程服务，那类似的，我们可以用动态代理，根据消费者想要调用方法的返回值类型，生成一个返回固定值的代理对象，就能够实现Mock的功能了。

### 5.2.3 实现过程

1. RPC配置新增mock开关，服务消费者可以选择开启或关闭。
2. 新增Mock服务代理，当mock开关开启，在构建代理的时候使用Mock服务代理。



**rpc-core**

- RpcConfig.java

  ```java
  /**
       * Mock 开关，默认关闭(Mock，模拟调用)
       */
      private boolean mock = false;
  ```

- MockServiceProxy.java

  ```java
  package com.fdt.tianrpc.proxy;
  
  import lombok.extern.slf4j.Slf4j;
  
  import java.lang.reflect.InvocationHandler;
  import java.lang.reflect.Method;
  
  /**
   * Mock服务代理(使用JDK动态代理实现)
   */
  @Slf4j
  public class MockServiceProxy implements InvocationHandler {
  
      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
          // 根据方法的返回值类型，生成特定的返回值
          Class<?> methodReturnType = method.getReturnType();
          log.info("mock invoke {}", methodReturnType);
          return getDefaultObject(methodReturnType);
      }
  
      /**
       * 根据方法返回值类型生成默认返回值
       * @param methodReturnType
       * @return
       */
      private Object getDefaultObject(Class<?> methodReturnType) {
  
          // 基本类型
          if(methodReturnType.isPrimitive()){
              if(methodReturnType == boolean.class){
                  return false;
              }
              if(methodReturnType == byte.class){
                  return (byte)0;
              }
              if(methodReturnType == short.class){
                  return (short)0;
              }
              if(methodReturnType == int.class){
                  return 0;
              }
              if(methodReturnType == long.class){
                  return 0L;
              }
              if(methodReturnType == float.class){
                  return 0.0f;
              }
              if(methodReturnType == double.class){
                  return 0.0d;
              }
          }
          // 对象类型
          return null;
          
      }
  }
  
  ```

  

- ServiceProxyFactory.java

  ```java
  package com.fdt.tianrpc.proxy;
  
  import com.fdt.RpcApplication;
  
  import java.lang.reflect.Proxy;
  
  public class ServiceProxyFactory {
      /**
       * 根据服务类获取代理对象
       * @param serviceClass
       * @return
       * @param <T>
       */
      public static <T> T getProxy(Class<T> serviceClass){
  
          // 如果是mock模式，则返回mock代理对象
          if(RpcApplication.getRpcConfig().isMock()){
              return getMockProxy(serviceClass);
          }
          // 否则返回正常代理对象
          return (T) Proxy.newProxyInstance(
                  serviceClass.getClassLoader(),
                  new Class[]{serviceClass},
                  new ServiceProxy()
          );
      }
  
      /**
       * 获取mock代理对象
       * @param serviceClass 服务类
       * @param <T>
       * @return
       */
      public static <T> T getMockProxy(Class<T> serviceClass){
          return (T) Proxy.newProxyInstance(
                  serviceClass.getClassLoader(),
                  new Class[]{serviceClass},
                  new MockServiceProxy()
          );
      }
  }
  
  ```

  

- 

### 5.2.4 注意事项

​		由于我们还没有注册中心，所以动态代理中是写死了服务器地址和端口，因此，我们只是通过全局配置加载，加载了我们配置文件的内容，却还没有将配置应用到实际的代理中。

### 5.2.5 扩展

- 完善Mock逻辑，支持更多返回值类型，生成对应的默认返回值
  - 使用Faker等伪造数据生成库，模拟数据。
- 服务提供者Mock功能，让服务提供者自主选择返回真实或模拟数据

## 5.3 序列化器与SPI机制

### 5.3.1 问题修复

#### 5.3.1.1 序列化器问题

1. Hessian序列化器问题，看评论区，serialize方法加flush方法？HessianInput换成Hessian2Input？

   1. 从 Java 9 开始，Java 引入了 **模块化系统**，并加强了对内部 API 的访问限制。反射访问 `java.lang.Throwable` 类的私有字段（比如 `detailMessage`）属于非法的反射访问行为，因此会触发警告。这个警告表明你的代码或使用的库（在这种情况下是 **Hessian**）在尝试进行非法的反射访问。
      1. Hessian 序列化库在实现过程中，可能会使用反射来访问一些字段，但这种做法在 Java 9 及以后版本中会被视为非法反射操作。修改代码，将 序列化器的序列化、反序列化方法的Hessian2Output替换HessianInput
      2. Kryo同样存在这种问题，但是在Maven仓库查看，引入的已经是最新的版本（5.6.X），但依旧存在这种问题，未能解决
   
2. newInstance方法被弃用

   ```java
   // 自 Java 9 以后，Class.newInstance() 被标记为过时，并建议使用 Constructor 来替代。
   // 这是因为 newInstance() 不支持异常处理，且其内部调用的 Constructor 可能抛出更多类型的异常。
   // 获取构造器，
   Constructor<?> constructor = impClass.getConstructor();  // 获取无参构造器
   // 创建constructor对象，要通过反射调用方法，需要传递类的实例
   Object instance = constructor.newInstance();
   ```




#### 5.3.1.2 多线程和多进程

1. 我之前一直不清楚，多线程是什么？一台机器启动多个程序吗？不是的。
2. 像idea启动两个程序，它们都有自己的JAVA虚拟机，不共享内存，这是多进程，所以对于服务的消费者和提供者，他们自己的配置不会影响别人，在自己的进程中，如果启动了多个线程，单例模式才起作用，不用重复创建对象。
3. 而多线程是指同一进程内，有多个线程一起执行，共享内存

### 5.3.2 主流序列化方式

1. JSON
   1. 优点
      1. 易读性好可读性强，人类容易理解，调试
      2. 跨语言支持好
   2. 缺点
      1. 序列化后数据量大，因为使用的是文本格式
      2. 对于复杂数据结构和循环的支持不好，性能不好，还可能会失败
2. Hessian
   1. 优点
      1. 二进制序列化，数据量小，网络传输的效率高
      2. 跨语言支持好，适合于分布式系统
   2. 缺点
      1. 性能比JSON略低
      2. 对象需要实现serializable接口，限制可序列化的对象范围。
3. Kryo
   1. 优点
      1. 高性能，序列化，反序列化都很快
      2. 支持循环引用，自定义序列化器，适用于复杂的对象
      3. 对比Hessian，不需要实现serializable接口，适用范围大
   2. 缺点
      1. 只适用于java
      2. 序列化的格式，人类很难看懂，不好阅读和调试
4. Protobuf
   1. 优点
      1. 同样是二进制序列化，数据量小
      2. 跨语言支持
      3. 支持版本化，向前/向后兼容，新老版本可以互相通信
   2. 缺点
      1. 配置较复杂，要定义数据结构的消息格式
      2. 序列化格式不易阅读

### 5.3.3 需求分析和知识补充

1. 我们为什么要做动态设置序列化器，自定义序列化器呢？
   1. 我们要提供给用户选择，但是又不能奢求用户自行修改我们的代码，所以，我们需要让用户用简单的方式进行序列化器更换。
   2. 我们开发RPC框架，不可能考虑到所有人的需求，所以要提供给用户自定义序列化器的方式。

1. 我们如何动态设置序列化器，自定义序列化器？

   1. 通过配置的方式动态设置序列化器，参考Dubbo。

      > [Hessian | Apache Dubbo](https://cn.dubbo.apache.org/zh-cn/overview/mannual/java-sdk/reference-manual/serialization/hessian/#2-使用方式)

   2. 通过SPI机制实现自定义序列化器。

      1. SPI（Service Provider Interface）服务提供接口，java中的重要机制，它允许开发者将自己的实现通过配置的方式注册到框架中，框架通过反射机制进行调用，而不需要去改动框架原有的代码，比如数据库驱动 JDBC，就使用了SPI机制，通过配置的方式读取不同的数据库驱动，比如MySQL数据库。

### 5.3.4 动态设置序列化器

1. Java自带的SPI接口

   1. 在resources目录下创建META-INF/services目录，创建名称为要实现接口路径的文件，再把实现类的路径写在文件里

   2. 在代码中使用，获取所有的实现类对象，然后判断使用哪个。

      ```java
      // 指定序列化器
      Serializer serializer = null;
      ServiceLoader<Serializer> serviceLoader = ServiceLoader.load(Serializer.class);
      for (Serializer service : serviceLoader) {
          serializer = service;
      }
      
      ```

      

2. 自定义我们的SPI实现

   1. 虽然java自带了SPI实现，但是如果符合条件的实现类有多个，我们不能用它指定要使用哪个实现类。所以我们要自定义SPI实现，用户在配置文件填写序列化器的实现类路径，我们得到一个路径到实现类的映射，就可以动态设置序列化器了。
   2. 为什么说java自带的SPI实现不能选择使用哪个实现类？
      1. serviceLoader面向自动化使用，不提供选择，只会把所有实现类加载出来，你使用代码进行判断。

#### 5.3.3.1 各种序列化器实现

> JsonSerializer

```java
package com.fdt.tianrpc.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdt.tianrpc.model.RpcRequest;
import com.fdt.tianrpc.model.RpcResponse;

import java.io.IOException;

/**
 * JSON序列化器
 */
public class JsonSerializer implements Serializer{
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public <T> byte[] serialize(T object) throws IOException {
        return OBJECT_MAPPER.writeValueAsBytes(object);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        T object = OBJECT_MAPPER.readValue(bytes, type);
        if(object instanceof RpcRequest){
            return handleRequest((RpcRequest)object, type);
        }
        if(object instanceof RpcResponse){
            return handleResponse((RpcResponse)object, type);
        }
        return object;
    }

    /**
     * 处理请求
     * 由于Object的原始对象会被擦除，导致反序列化时会被作为LinkedHashMap无法转换成原始对象
     * 因此做了特殊处理
     *
     * @param request 请求对象
     * @param type 目标类型
     * @return T
     * @param <T>
     * @throws IOException 读取字节数组异常
     */
    private<T> T handleRequest(RpcRequest request, Class<T> type) throws IOException {
        Class<?>[] parameterTypes = request.getParameterTypes();  // 获取RPC请求中参数的类型
        Object[] args = request.getArgs();  // 获取RPC请求中实际的参数值,获取的是引用而不是拷贝

        // 循环处理每一个参数的类型
        for(int i = 0; i < parameterTypes.length; i++){
            Class<?> clazz = parameterTypes[i];  // 获取当前参数的期望类型
            // 如果当前参数的实际类型与期望类型不一致，则需要进行类型转换
            if(!clazz.isAssignableFrom(args[i].getClass())){
                byte[] argsBytes = OBJECT_MAPPER.writeValueAsBytes(args[i]);  // 将参数转换为字节数组（JSON）
                args[i] = OBJECT_MAPPER.readValue(argsBytes, clazz);  // 将字节数组反序列化为期望类型的对象
            }
        }
        return type.cast(request);  // 将RpcRequest对象强制转换为传入的类型并返回
    }

    /**
     * 处理响应
     * @param response 响应对象
     * @param type 参数类型
     * @return
     * @param <T>
     * @throws IOException 读取字节数组异常
     */
    private<T> T handleResponse(RpcResponse response, Class<T> type) throws IOException {
        // 处理响应数据
        byte[] dataBytes = OBJECT_MAPPER.writeValueAsBytes(response.getData()); // 将数据转换成字节数组
        response.setData(OBJECT_MAPPER.readValue(dataBytes, response.getDataType())); // 将字节数组反序列化为期望类型的对象
        return type.cast(response);
    }



}

```



> KryoSerializer

```java
package com.fdt.tianrpc.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


public class KryoSerializer implements Serializer{

    private static final ThreadLocal<Kryo> KRYO_THREAD_LOCAL = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        // 设置动态类注册为false，不提前注册所有类，防止安全问题
        kryo.setRegistrationRequired(false);
        return kryo;
    });

    @Override
    public <T> byte[] serialize(T object){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 创建输出流
        Output output = new Output(byteArrayOutputStream);
        // 将对象序列化写入输出流
        KRYO_THREAD_LOCAL.get().writeObject(output, object);
        output.close();
        // 返回序列化后的字节数组
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type){
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        // 创建输入流
        Input input =new Input(byteArrayInputStream);
        // 将字节数组反序列化为对象
        T result = KRYO_THREAD_LOCAL.get().readObject(input, type);
        input.close();
        return result;
    }
}
```



> HessianSerializer

```java
package com.fdt.tianrpc.serializer;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HessianSerializer implements Serializer{
    @Override
    public <T> byte[] serialize(T object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 获取输出流对象
        HessianOutput hessianOutput = new HessianOutput(byteArrayOutputStream);
        // 将对象序列化写入输出流
        hessianOutput.writeObject(object);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        // 获取输入流对象
        HessianInput hessianInput = new HessianInput(byteArrayInputStream);
        // 将字节数组反序列化为对象并返回
        return (T) hessianInput.readObject();
    }

}

```



#### 5.3.3.2 序列化器工厂

这里还是用HashMap硬编码了系统内部的键和实现类。

```
package com.fdt.tianrpc.serializer;

import java.util.HashMap;
import java.util.Map;

public class SerializerFactory {

    /**
     * 序列化映射（用于实现单例）
     */
    private static final Map<String, Serializer> KEY_SERIALIZER_MAP = new HashMap<String, Serializer>(){
        {
            // 使用双括号新建对象并使用多次put操作初始化
            put(SerializerKeys.JSON, new JsonSerializer());
            put(SerializerKeys.HESSIAN, new HessianSerializer());
            put(SerializerKeys.KRYO, new KryoSerializer());
            put(SerializerKeys.JDK, new JdkSerializer());
        }
    };

    /**
     * 默认序列化器
     */
    private static final Serializer DEFAULT_SERIALIZER = KEY_SERIALIZER_MAP.get("jdk");


    /**
     * 获取序列化器实例
     */
    public static Serializer getInstance(String key) {
        // 返回输入键的对应序列化器，如果找不到对应的序列化器，返回默认序列化器
        return KEY_SERIALIZER_MAP.getOrDefault(key, DEFAULT_SERIALIZER);
    }
}


```



### 5.3.5 自定义序列化器

#### 5.3.5.1 配置文件

​		既然我们要让用户通过配置的方指定自己的自定义序列化器，同样也可以通过同样的方式扩展我们系统内部的序列化器。而系统内置的SPI机制会加载resources资源目录下的META-INF/services目录，我们可以效仿它。

- 内部SPI：META-INF/rpc/system,放置系统内置的序列化器资源

  - 编写名为com.fdt.tianrpc.serializer.Serializer的文件

    ```
    hessian=com.fdt.tianrpc.serializer.HessianSerializer
    kryo=com.fdt.tianrpc.serializer.KryoSerializer
    ```

    

- 用户自定义SPI：META-INF/rpc/custom，放置用户自定义的序列化器资源，可以放到common模块的META-INF/rpc/custom目录。

​		这样我们就不需要在序列化器工厂中使用HashMap硬编码系统内部的序列化器了。



#### 5.3.5.2 SpiLoader（配置读取，加载实现类）

> SpiLoader

获取配置文件是用ResourceUtil.getResources而不是文件路径，因为我们的框架将来是以依赖的方式被导入的。

```java
package com.fdt.tianrpc.spi;


import cn.hutool.core.io.resource.ResourceUtil;
import com.fdt.tianrpc.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Spi加载器，加载配置文件和实现类
 */
@Slf4j
public class SpiLoader {

    /**
     * 存储已加载的类，接口名 => (key => 实现类)
     */
    private static Map<String, Map<String, Class<?>>> loaderMap = new ConcurrentHashMap<>();

    /**
     * 对象实例存储，避免重复new对象
     */
    private static Map<String, Object> instanceCache = new ConcurrentHashMap<>();

    /**
     * 常量：系统的SPI目录
     */
    private static final String RPC_SYSTEM_SPI_DIR = "META-INF/rpc/system/";

    /**
     * 常量：用户自定义的SPI目录
     */
    private static final String RPC_USER_SPI_DIR = "META-INF/rpc/custom/";

    /**
     * 需要扫描的路径
     */
    private static final String[] SCAN_DIRS = new String[]{RPC_SYSTEM_SPI_DIR, RPC_USER_SPI_DIR};

    /**
     * 动态加载的类列表
     */
    private static final List<Class<?>> LOAD_CLASS_LIST = Arrays.asList(Serializer.class);

    /**
     * 加载所有SPI
     */
    public static void loadAllSpi() {
        log.info("开始加载所有SPI");
        for (Class<?> aClass : LOAD_CLASS_LIST) {
            load(aClass);
        }
    }

    /**
     * 获取某个接口的实例
     *
     * @param tClass 接口类
     * @param key    接口实现类的key
     * @param <T>
     * @return
     */
    public static <T> T getInstance(Class<?> tClass, String key) {
        String tClassName = tClass.getName();
        Map<String, Class<?>> keyClassMap = loaderMap.get(tClassName);
        if (keyClassMap == null) {
            throw new RuntimeException(String.format("SpiLoader未加载%s类型", tClassName));
        }
        if (!keyClassMap.containsKey(key)) {
            throw new RuntimeException(String.format("SpiLoader的%s不存在key=%s的实现类", tClassName, key));
        }
        // 获取要加载的实现类型
        Class<?> impClass = keyClassMap.get(key);
        // 从实例的缓存中加载指定类型的实例
        String impClassName = impClass.getName();
        if (!instanceCache.containsKey(impClassName)) {
            try {
                Constructor<?> constructor = impClass.getConstructor();
                Object instance = constructor.newInstance();
                instanceCache.put(impClassName, instance);
                log.info("加载SPI实例：{}", impClassName);
            }
            // 捕获多种异常
            catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                   InvocationTargetException e) {
                String errorMsg = String.format("SpiLoader加载%s实例失败", impClassName);
                log.error(errorMsg, e);
            }

        }
        return (T) instanceCache.get(impClassName);
    }

    /**
     * 加载某个类型的SPI
     *
     * @param loadClass 接口类
     */
    public static void load(Class<?> loadClass) {
        String loadClassName = loadClass.getName();
        log.info("开始加载类型为{}的SPI", loadClassName);
        // 扫描路径，用户自定义的SPI优先级高于系统SPI
        Map<String,Class<?>> keyClassMap = new HashMap<>();
        for (String scanDir : SCAN_DIRS) {
            List<URL> resources = ResourceUtil.getResources(SCAN_DIRS + loadClassName);
            // 读取每个资源文件的内容，需保证用户自定义的同类型SPI覆盖系统SPI
            for (URL resource : resources) {
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        // 将每一行从=号处分割，左边为key，右边为value
                        String[] strArray = line.split("=");
                        if(strArray.length > 1){
                            String key = strArray[0];
                            String value = strArray[1];
                            keyClassMap.put(key, loadClass.forName(value));
                        }
                    }
                } catch (Exception e) {
                    log.error(String.format("SpiLoader加载%s失败", loadClassName), e);
                }
                loaderMap.put(loadClassName, keyClassMap);
            }
        }
    }
}

```



#### 5.3.5.3 重构序列化器工厂

```java
package com.fdt.tianrpc.serializer;

import com.fdt.tianrpc.spi.SpiLoader;

public class SerializerFactory {

    /**
     * 类加载的时候默认加载序列化器的所有实现类
     */
    static {
        SpiLoader.load(Serializer.class);
    }

    /**
     * 默认序列化器
     */
    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();


    /**
     * 获取序列化器实例
     */
    public static Serializer getInstance(String key) {
        // 获取指定key的序列化器实例，如果没有则返回默认的序列化器
        return SpiLoader.getInstance(Serializer.class, key) == null ?
                DEFAULT_SERIALIZER : SpiLoader.getInstance(Serializer.class, key);
    }
}


```



## 5.4 注册中心

### 5.4.1 需求分析

1. 为什么需要注册中心？
   1. 回看我们简易版的RPC框架，动态代理时服务的地址端口都是硬编码的，虽然服务消费者和提供者都可以通过配置修改服务地址和端口，但是服务消费者如何知道服务在哪呢？
      1. 可能通过线下约定的方式
      2. 但是服务提供者的服务地址和端口可能是动态变化的，这种情况下，每次变化后都要重新修改，就很麻烦了。
   2. 通过注册中心，服务消费者就不需要关心服务地址和端口了，更方便了。



### 5.4.2 技术选型

1. 主流的注册中心的实现中间件有zookeeper、redis等，而我们这次要用的是Etcd，一款云原生的高性能中间件，使用Go语言实现、开源的、分布式键值存储系统。
2. Etcd拥有简单的API、可视化界面、数据过期机制、数据监听和通知机制，对于注册中心来说，是一款完美的中间件。

### 5.4.3 代码实现

#### 5.4.3.1 etcd安装和引入

1. etcd

   > [Releases · etcd-io/etcd](https://github.com/etcd-io/etcd/releases)

   1. 使用etcd.exe启动后,etcd默认占用2379和2380两个端口
      1. 构建后的脚本
         1. etcd：服务本身
         2. etcdctl：客户端，操作etcd，进行数据读写等操作
         3. etcdutl：备份复原工具
      2. 占用端口的作用
         1. 2379：提供HTTP API服务，和etcdctl交互
         2. 2380：集群中节点通讯

2. etcdkeeper

   > [Releases · evildecay/etcdkeeper](https://github.com/evildecay/etcdkeeper/releases)

   1. etcd的一种网页可视化界面工具

   2. 启动命令

      ```bash
      # window系统指定在8081端口启动
      .\etcdkeeper.exe -p 8081
      ```

   3. 浏览器键入

      ```web-idl
      http://localhost:8081/etcdkeeper/
      ```

   4. 

3. jetcd

   1. etcd的java客户端，在代码中操控etcd

   2. ```
      <!-- https://mvnrepository.com/artifact/io.etcd/jetcd-core -->
      <dependency>
          <groupId>io.etcd</groupId>
          <artifactId>jetcd-core</artifactId>
          <version>0.7.7</version>
      </dependency>
      
      ```

   3. 客户端内包含多种不同用处的客户端，下面三种常用

      1. kvClient：用于操作etcd的键值对，可以设置、获取、删除、列出目录
      2. leaseClient：管理etcd的租约机制，租约是etcd的一种时间切片，为键值对分配生存时间，到期自动删除键值对，该客户端可以创建、获取、续约和撤销租约
      3. watchClient：监视etcd键的变化，一旦变化，它就能接收通知

4. 测试代码

   > get()用于阻塞线程，因为etcd的操作是异步的，不会直接返回结果，返回的是Future对象，在操作进行的时候可以执行其他操作，但是某些数据操作如果尚未执行就到了下一步，可能会出现问题，这时就要阻塞前程，等待结果。

   ```java
   package com.fdt.tianrpc.registry;
   
   import io.etcd.jetcd.ByteSequence;
   import io.etcd.jetcd.Client;
   import io.etcd.jetcd.KV;
   import io.etcd.jetcd.kv.GetResponse;
   
   import java.util.concurrent.CompletableFuture;
   import java.util.concurrent.ExecutionException;
   
   public class EtcdRegistry {
   
       public static void main(String[] args) throws ExecutionException, InterruptedException {
           // 创建客户端
           Client client = Client.builder().endpoints("http://localhost:2379")
                   .build();
           // 创建键值客户端
           KV kvClient = client.getKVClient();
   
           // 创建键值对
           ByteSequence key = ByteSequence.from("test_key".getBytes());
           ByteSequence value = ByteSequence.from("test_value".getBytes());
   
           // 将键值对放入键值客户端
           kvClient.put(key, value).get();
   
           // 获取键值对
           CompletableFuture<GetResponse> getFuture = kvClient.get(key);
   
           // 得到键值对
           GetResponse getResponse = getFuture.get();
   
           System.out.println(getResponse.getKvs().get(0).getValue().toString());
   
           // 删除键值对
           kvClient.delete(key).get();
       }
   }
   
   ```

   

#### 5.4.3.2 存储结构

一般两种存储结构，如何选择要看技术选型，zookeeper和etcd支持层级查询，可以选择第一种，层次分明，redis支持列表查询，可选择第二种。最后，记得给key设置默认过期时间，当服务宕机后，无效的key会被自动移除

1. 层级结构：将服务比作文件夹，服务节点是文件夹中的文件，则文件夹的命名规则为：/业务前缀/服务名，文件的key命名规则为：/业务前缀/服务名/服务节点地址
2. 列表结构：一个服务对应一个列表，服务是key，列表整体是value

### 5.4.4 问题解决

1. 测试注册中心的unRegister()方法的时候，发现无法注销服务，看教程评论区，有人说是unRegister()方法内使用的delete最后没加get(),由于etcd是异步操作的，方法不会等待删除完成，而是返回了CompletableFuture对象，然后测试方法执行到末尾，接着可能是调用了EtcdRegistry的destroy方法，销毁了连接注册中心的客户端，导致注销失败。
2. 测试服务发现的时候，使用了断言，但是教程使用的是Assert.assertNotNull(serviceMetaInfoList);，即使没有发现服务也返回true，而使用Assert.assertFalse(serviceMetaInfoList.isEmpty())就正常了，因为方法返回的时候会创建一个新的List，不会返回null。
3. git提交问题
   1. 在项目里放了一个笔记文件，md结尾，结果点击提交没用，点击提交那里的小零件（设置），不勾选分析代码，就没问题
   2. git提交的注释最好简短，且不要写两个字就换行，在GitHub上，第一眼只能看到第一行
4. 注册中心基础版完整测试问题
   1. 找不到服务
      1. 第一眼以为是服务租约太短，还未启动消费者，租约就结束了，导致找不到，但是快速测试一遍发现还是找不到。
      2. 使用etcdkepper查看注册的服务，发现有一个null，回看注册的时候设置的键，发现是服务版本为空；原来是消费者使用服务发现的时候，用默认的版本进行发现，但是提供者进行服务注册的时候没有提供服务版本，导致找不到服务，所以给ServiceMetaInfo加上版本常量（后面 再改进），注册的时候不填版本就有了默认的版本号，消费者同理。

   2. 修改注册中心配置的address
      1. 预期的错误是因为地址更改注册中心客户端初始化会失败，但是实际的问题是address不写前缀，如**http://**的话，就无法解析这个地址了。

   3. 修改注册中心的地址配置
      1. 预期是会因为地址错误而无法注册和服务发现等，但是实际上程序一直在等待，询问chat后，意识到可能是get()直接阻塞了程序运行，而我没有给它设置超时时间，这就导致了阻塞没有时限，程序卡死了，给get()加了5秒超时时间后，地址错误就会报错了。



### 5.4.5 基本实现

#### 5.4.5.1 注册中心配置和实现类

> RegistryConfig.java

```java
package com.fdt.tianrpc.config;

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
}

```



>  RpcConfig.java

```java
    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig = new RegistryConfig();
```



> ServiceMetaInfo.java

```java
package com.fdt.tianrpc.model;

/**
 * 服务元信息（注册信息）
 */
public class ServiceMetaInfo {

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务版本
     */
    private String serviceVersion;

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
        return String.format("%s:%s:%s",getServiceKey(), serviceHost, servicePort);
    }
}


```



> Registry.java

```java
package com.fdt.tianrpc.registry;

import com.fdt.tianrpc.config.RegistryConfig;
import com.fdt.tianrpc.model.ServiceMetaInfo;

import java.util.List;

public interface Registry {

    /**
     * 初始化
     * @param registryConfig 注册中心配置
     */
    void init(RegistryConfig registryConfig);

    /**
     * 服务注册(提供者)
     * @param serviceMetaInfo 服务元信息
     * @throws Exception 注册异常
     */
    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
     * 服务注销(提供者)
     * @param serviceMetaInfo 服务元信息
     */
    void unRegister(ServiceMetaInfo serviceMetaInfo);

    /**
     * 服务发现(消费者)
     * @param serviceKey 服务key
     * @return 服务元信息列表
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

    /**
     * 注册中心销毁
     */
    void destroy();
}

```



> EtcdRegistry

```java
package com.fdt.tianrpc.registry;

import cn.hutool.json.JSONUtil;
import com.fdt.tianrpc.config.RegistryConfig;
import com.fdt.tianrpc.model.ServiceMetaInfo;
import com.google.protobuf.ByteString;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class EtcdRegistry implements Registry{

    private Client client;
    private KV kvClient;

    /**
     * 根节点
     */
    public static final String ETCD_ROOT_PATH = "/rpc/";

    @Override
    public void init(RegistryConfig registryConfig) {
        client = Client.builder().endpoints(registryConfig.getAddress())
                .connectTimeout(Duration.ofMillis(registryConfig.getTimeout()))
                .build();
        kvClient = client.getKVClient();
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {

        Lease leaseClient = client.getLeaseClient();

        // 创建租约，时间30秒，使用get()阻塞线程，确保获取正确的租约id
        long leaseId = leaseClient.grant(30).get().getID();

        // 设置服务键值对
        String registryKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registryKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);

        // 将键值对和租约联系起来，租约结束，删除键值对
        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key,value,putOption).get();
    }

    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {
        kvClient.delete(ByteSequence.from(ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey(),StandardCharsets.UTF_8));
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {

        // 搜索前缀,结尾处加/
        String searchPrefix = ETCD_ROOT_PATH + serviceKey + "/";

        try{
            // isPrefix=true,启用前缀匹配，根据前缀搜索
            GetOption getOption= GetOption.builder().isPrefix(true).build();
            List<KeyValue> keyValues = kvClient.get(
                    ByteSequence.from(searchPrefix,StandardCharsets.UTF_8),
                    getOption)
                    .get()
                    .getKvs();

            // 解析服务信息
            return keyValues.stream()
                    .map(keyValue -> {
                                String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                                return JSONUtil.toBean(value,ServiceMetaInfo.class);
                            }
                    )
                    .collect(Collectors.toList());
        }catch (Exception e){
            throw new RuntimeException("获取服务列表失败",e);
        }
    }

    @Override
    public void destroy() {
        System.out.println("本节点下线");
        // 释放资源
        if(kvClient != null){
            kvClient.close();
        }
        if(client != null){
            client.close();
        }
    }
}

```

#### 5.4.5.2 支持多种和自定义注册中心

我们之前已经跑通过SPI机制了，这次就模仿上次，编写工厂类和resources下的配置，进行读取即可。

> RegistryKeys.java

```java
package com.fdt.tianrpc.registry;

public class RegistryKeys {
    String ETCD = "etcd";
    String ZOOKEEPER = "zookeeper";
}

```



> RegistryFactory.java

```java
package com.fdt.tianrpc.registry;

import com.fdt.tianrpc.spi.SpiLoader;

public class RegistryFactory {

    /**
     * 加载所有的注册中心实现
     */
    static {
        SpiLoader.load(Registry.class);
    }

    /**
     * 默认注册中心
     */
    private static final Registry DEFAULT_REGISTRY = new EtcdRegistry();


    /**
     * 获取注册中心实例
     */
    public static Registry getInstance(String key) {
        // 获取指定key的注册中心实例，如果没有则返回默认的注册中心
        return SpiLoader.getInstance(Registry.class, key) == null ?
                DEFAULT_REGISTRY : SpiLoader.getInstance(Registry.class, key);
    }
}


```



> RpcApplication.java

```java
 // 修改init(RpcConfig newRpcConfig)
 public static void init(RpcConfig newRpcConfig){
        rpcConfig = newRpcConfig;
        log.info("rpc init,config = {}",newRpcConfig.toString());

        // 注册中心初始化
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info("registry init,registry = {}",registryConfig.getRegistry());

    }
```



> src/main/resources/META-INF/rpc/system/com.fdt.tianrpc.registry.Registry

```
etcd=com.fdt.tianrpc.registry.EtcdRegistry
```



#### 5.4.5.3 注册中心基础版基本测试

> ServiceProxy.java

```java
package com.fdt.tianrpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.fdt.RpcApplication;
import com.fdt.tianrpc.config.RpcConfig;
import com.fdt.tianrpc.constant.RpcConstant;
import com.fdt.tianrpc.model.RpcRequest;
import com.fdt.tianrpc.model.RpcResponse;
import com.fdt.tianrpc.model.ServiceMetaInfo;
import com.fdt.tianrpc.registry.Registry;
import com.fdt.tianrpc.registry.RegistryFactory;
import com.fdt.tianrpc.serializer.Serializer;
import com.fdt.tianrpc.serializer.SerializerFactory;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 动态代理
 */
@Slf4j
public class ServiceProxy implements InvocationHandler {
    // 当调用代理对象的方法时，会转为调用invoke方法
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
        Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());
        log.debug(String.format("动态代理使用%s序列化器",serializer.toString()));
        // 构建请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        // 序列化请求，发送请求
        try{
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            // 从注册中心获取服务地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            // todo 服务版本现在使用的是默认值,应该从配置文件中获取
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if(CollUtil.isEmpty(serviceMetaInfoList)){
                throw new RuntimeException("未发现服务");
            }
            // todo 现在直接取了第一个服务地址，后续需要负载均衡
            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfoList.get(0);

            // 发送请求
            try(HttpResponse httpResponse = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress())
                        .body(bodyBytes)
                        .execute()){
                byte[] result = httpResponse.bodyBytes();
                // 反序列化响应结果
                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                // 将结果转换成User类实例，返回结果
                return rpcResponse.getData();
            } catch (Exception e){
                e.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

```

> RegistryTest.java

```java
package com.fdt.tianrpc.registry;

import com.fdt.tianrpc.config.RegistryConfig;
import com.fdt.tianrpc.model.ServiceMetaInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class RegistryTest {

    final Registry registry = new EtcdRegistry();

    @Before
    public void init() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("http://localhost:2379");
        registry.init(registryConfig);
    }

    @Test
    public void register() throws Exception {
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        // 模拟服务提供者注册服务信息

        // 服务1
        serviceMetaInfo.setServiceName("fdtService");
        serviceMetaInfo.setServiceVersion("1.0.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1234);
        registry.register(serviceMetaInfo);

        serviceMetaInfo = new ServiceMetaInfo();
        //服务2
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("1.0.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1235);
        registry.register(serviceMetaInfo);

        serviceMetaInfo = new ServiceMetaInfo();
        //服务1的2.0版本
        serviceMetaInfo.setServiceName("fdtService");
        serviceMetaInfo.setServiceVersion("2.0.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1236);
        registry.register(serviceMetaInfo);
    }

    @Test
    public void unRegister() throws ExecutionException, InterruptedException {
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("fdtService");
        serviceMetaInfo.setServiceVersion("1.0.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1234);
        // 注销服务，记得kvClient.delete要加入get()，否则删除还未完成就结束了
        registry.unRegister(serviceMetaInfo);
    }

    @Test
    public void serviceDiscovery() {
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("fdtService");
        serviceMetaInfo.setServiceVersion("1.0.0");
        String serviceKey = serviceMetaInfo.getServiceKey();
        List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceKey);
        Assert.assertFalse(serviceMetaInfoList.isEmpty());
    }


}

```

#### 5.4.5.4 注册中心基础版完整测试

> ProviderExample

```java
package com.fdt.provider;

import com.fdt.RpcApplication;
import com.fdt.common.service.UserService;
import com.fdt.tianrpc.config.RegistryConfig;
import com.fdt.tianrpc.config.RpcConfig;
import com.fdt.tianrpc.model.ServiceMetaInfo;
import com.fdt.tianrpc.registry.LocalRegistry;
import com.fdt.tianrpc.registry.Registry;
import com.fdt.tianrpc.registry.RegistryFactory;
import com.fdt.tianrpc.server.HttpServer;
import com.fdt.tianrpc.server.VertxHttpServer;

public class ProviderExample {
    public static void main(String[] args) {
        // RPC框架初始化
        RpcApplication.init();

        // 本地服务注册
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);

        //注册服务到注册中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());

        try{
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // web服务启动
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(rpcConfig.getServerPort());
    }
}

```

> application.properties

```properties
tian-rpc.registryConfig.username=fengdetian
tian-rpc.registryConfig.password=who
```

>EtcdRegistry.java

```java
   // 添加超时报错逻辑，当超时后仍然无法连接到注册中心时，抛出错误
   public void init(RegistryConfig registryConfig) {

            client = Client.builder().endpoints(registryConfig.getAddress())
                    .connectTimeout(Duration.ofMillis(registryConfig.getTimeout()))
                    .build();
            kvClient = client.getKVClient();

        try {
            ByteSequence rootPath = ByteSequence.from(ETCD_ROOT_PATH, StandardCharsets.UTF_8);
            GetResponse response = client.getKVClient().get(rootPath).get(5, TimeUnit.SECONDS);  // 设置超时为5秒
            String kv = String.valueOf(response.getKvs());
            System.out.println("获取根路径的内容:kv= " + kv);
        } catch (Exception e) {
            throw new RuntimeException("Etcd注册中心连接失败", e);
        }

    }
```



### 5.4.6 优化

## 5.5 自定义协议

## 5.6 负载均衡

## 5.7 重试机制

## 5.8 容错机制

## 5.9 启动机制和注解驱动

## 5.10 其他扩展思路