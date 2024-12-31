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
     * 需要扫描的路径,注意顺序，先加载的会被后加载的覆盖，如果要让用户自定义的优先级高于系统，则应该放在后面
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
            List<URL> resources = ResourceUtil.getResources(scanDir + loadClassName);
            // 读取每个资源文件的内容
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
        log.info(keyClassMap.toString());
    }
}
