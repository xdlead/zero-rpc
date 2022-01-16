package cn.dc.zero.rpc.core.registry;


import cn.dc.zero.rpc.core.config.RegistryConfig;
import cn.dc.zero.rpc.core.exception.RpcRuntimeException;
import cn.dc.zero.rpc.core.ext.ExtensionClass;
import cn.dc.zero.rpc.core.ext.ExtensionLoaderFactory;
import cn.dc.zero.rpc.core.log.LogCodes;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author:     DC
 * @Description:  注册中心工厂类
 * @Date:    2021/12/30 22:53
 * @Version:    1.0
 */
public class RegistryFactory  {

    private final static ConcurrentMap<RegistryConfig, Registry> ALL_REGISTRIES = new ConcurrentHashMap<RegistryConfig, Registry>();

    public static synchronized Registry getRegistry(RegistryConfig registryConfig) {

        String protocol = null;
        Registry registry ;
        try {
            registry = ALL_REGISTRIES.get(registryConfig);
            if (registry == null) {
                protocol = registryConfig.getProtocol();
                ExtensionClass<Registry> ext = ExtensionLoaderFactory.getExtensionLoader(Registry.class)
                        .getExtensionClass(protocol);
                if (ext == null) {
                    throw new RpcRuntimeException(LogCodes.getLog(LogCodes.ERROR_LOAD_REGISTRY,protocol,"not found registry "));
                }
                registry = ext.getExtInstance(new Class[] { RegistryConfig.class }, new Object[] { registryConfig });
                ALL_REGISTRIES.put(registryConfig, registry);
            }
            return registry;
        } catch (RpcRuntimeException e){
            throw e;
        }catch (Throwable e) {
            throw new RpcRuntimeException(LogCodes.getLog(LogCodes.ERROR_LOAD_REGISTRY,protocol,"the process of initial registry is error"));
        }
    }


    public static void destroy() {
        Iterator<Map.Entry<RegistryConfig, Registry>> entries = ALL_REGISTRIES.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<RegistryConfig, Registry> entry = entries.next();
            Registry registry = entry.getValue();
            registry.destroy();
        }
    }
}
