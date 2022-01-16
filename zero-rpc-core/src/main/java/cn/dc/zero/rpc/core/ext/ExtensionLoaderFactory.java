package cn.dc.zero.rpc.core.ext;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
/**
 * @Author:     DC
 * @Description:  自定义SPI工厂
 * @Date:    2021/12/30 22:47
 * @Version:    1.0
 */
public class ExtensionLoaderFactory {
    private ExtensionLoaderFactory() {
    }

    /**
     * extension loader cache
     */
    private static final ConcurrentMap<Class, ExtensionLoader> LOADER_MAP = new ConcurrentHashMap<Class, ExtensionLoader>();

    
    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> clazz, ExtensionLoaderListener<T> listener) {
        ExtensionLoader<T> loader = LOADER_MAP.get(clazz);
        if (loader == null) {
            synchronized (ExtensionLoaderFactory.class) {
                loader = LOADER_MAP.get(clazz);
                if (loader == null) {
                    loader = new ExtensionLoader<T>(clazz, listener);
                    LOADER_MAP.put(clazz, loader);
                }
            }
        }
        return loader;
    }

    /**
    * @description: 获取扩展类加载器
    * @param: [clazz]
    * @return: cn.dc.zero.rpc.core.ext.ExtensionLoader<T>
    * @author: DC
    */
    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> clazz) {
        return getExtensionLoader(clazz, null);
    }
}

