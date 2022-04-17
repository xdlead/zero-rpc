package cn.dc.zero.rpc.core.config;

import cn.dc.zero.rpc.core.common.RpcConfigs;
import cn.dc.zero.rpc.core.common.RpcOptions;
import cn.dc.zero.rpc.core.context.RpcRuntimeContext;
import cn.dc.zero.rpc.core.exception.RpcRuntimeException;
import cn.dc.zero.rpc.core.log.LogCodes;
import cn.dc.zero.rpc.core.registry.Registry;
import cn.dc.zero.rpc.core.registry.RegistryFactory;
import cn.dc.zero.rpc.core.server.ProviderProxyInvoker;
import cn.dc.zero.rpc.core.server.Server;
import cn.dc.zero.rpc.core.server.ServerFactory;
import cn.dc.zero.rpc.core.util.ClassUtils;
import cn.dc.zero.rpc.core.util.CommonUtils;
import cn.dc.zero.rpc.core.util.StringUtils;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.Serializable;
import java.util.List;
import java.util.zip.Adler32;

/**
 * 服务提供者基础配置类
 * @param <T>
 */
public class ProviderConfig<T> extends  AbstractServiceConfig<ProviderConfig<T>> implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProviderConfig.class);

    /**
     * 提供者引用
     */
    private T ref;

    /**
     * 服务提供者代理
     */
    private ProviderProxyInvoker providerProxyInvoker;

    /**
     * 服务提供者权重
     */
    private Integer weight = RpcConfigs.getIntValue(RpcOptions.PROVIDER_WEIGHT);

    /**
     * 服务的配置列表，暂时支持netty-tcp和http
     */
    protected List<ServerConfig> serverConfigs;


    public T getRef() {
        return ref;
    }


    public ProviderConfig<T> setRef(T ref) {
        this.ref = ref;
        return this;
    }


    public String getUniqueId() {
        return uniqueId;
    }


    public ProviderConfig<T> setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
        return this;
    }


    public synchronized void export() {
        try {
            preExport();
            //构建代理调用链
            providerProxyInvoker = new ProviderProxyInvoker(this);
            serverConfigs.forEach(s ->{
                Server server = ServerFactory.getServer(s);
                server.init(s);
                server.registerProcessor(this,providerProxyInvoker);
                server.start();
            });
            registryConfigs.forEach(r ->{
                Registry registry = RegistryFactory.getRegistry(r);
                registry.init();
                if(registry.start()){
                    registry.registry(this);
                }else {
                    throw new RpcRuntimeException(LogCodes.getLog(LogCodes.ERROR_BUILD_PROVIDER_REGISTRY,interfaceId,
                            r.getAddress(),r.getProtocol()));
                }
            });
        }catch (Exception e) {
            if (e instanceof RpcRuntimeException) {
                throw e;
            }
            throw new RpcRuntimeException(LogCodes.getLog(LogCodes.ERROR_BUILD_PROVIDER_CONFIG,interfaceId,uniqueId),e);
        }

    }


    public List<ServerConfig> getServer() {
        return serverConfigs;
    }


    public ProviderConfig<T> setServer(List<ServerConfig> serverConfigs) {
        this.serverConfigs = serverConfigs;
        return this;
    }

    public Integer getWeight() {
        return weight;
    }

    public ProviderConfig<T> setWeight(Integer weight) {
        this.weight = weight;
        return this;
    }

    @Override
    public  Class<?> getProxyClass() {
        if (proxyClass != null) {
            return proxyClass;
        }
        try {
            if (StringUtils.isNotBlank(interfaceId)) {
                this.proxyClass = ClassUtils.forName(interfaceId);
                if(!proxyClass.isInterface()){
                    throw new RpcRuntimeException(LogCodes.getLog(LogCodes.ERROR_PROVIDER_CONFIG,"service.interfaceId",
                            interfaceId, "interfaceId must be not null"));
                }
            } else {
                //打印日志
                throw new RpcRuntimeException(LogCodes.getLog(LogCodes.ERROR_PROVIDER_CONFIG,"service.interfaceId",
                        "null", "interfaceId must be not null"));
            }
        } catch (RpcRuntimeException e) {
            throw e;
        }catch (Exception e) {
            throw new RpcRuntimeException(LogCodes.getLog(LogCodes.getLog(LogCodes.ERROR_GET_PROXY_CLASS)),e);
        }
        return proxyClass;
    }

    private void preExport(){
        Class proxyClass = getProxyClass();
        if (!proxyClass.isInstance(ref)) {
            String name = ref == null ? "null" : ref.getClass().getName();
            throw new RpcRuntimeException(LogCodes.getLog(LogCodes.ERROR_PROVIDER_CONFIG,"service.interfaceId",
                    name, "proxyClass is not match reference"));
        }
        if (CommonUtils.isEmpty(serverConfigs)) {
            throw new RpcRuntimeException(LogCodes.getLog(LogCodes.ERROR_PROVIDER_CONFIG,"service.serverConfigs",
                    JSON.toJSONString(serverConfigs), "serverConfigs must be not null"));
        }
    }
}
