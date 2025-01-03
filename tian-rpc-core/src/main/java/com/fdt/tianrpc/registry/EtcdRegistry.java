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
import java.util.concurrent.ExecutionException;
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
    public void unRegister(ServiceMetaInfo serviceMetaInfo) throws ExecutionException, InterruptedException {
        kvClient.delete(ByteSequence.from(ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey(),StandardCharsets.UTF_8))
                .get();
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {

        // 搜索前缀,如果ETCD_ROOT_PATH最后加了/,这里就不用加/
        String searchPrefix = ETCD_ROOT_PATH + serviceKey;

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
