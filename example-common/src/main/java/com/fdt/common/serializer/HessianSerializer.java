package com.fdt.common.serializer;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.fdt.tianrpc.serializer.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HessianSerializer implements Serializer {
    @Override
    public <T> byte[] serialize(T object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 获取输出流对象
        Hessian2Output hessianOutput = new Hessian2Output(byteArrayOutputStream);
        // 将对象序列化写入输出流
        hessianOutput.writeObject(object);
        hessianOutput.flush();
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        // 获取输入流对象
        Hessian2Input hessianInput = new Hessian2Input(byteArrayInputStream);
        // 将字节数组反序列化为对象并返回
        return (T) hessianInput.readObject();
    }

}
