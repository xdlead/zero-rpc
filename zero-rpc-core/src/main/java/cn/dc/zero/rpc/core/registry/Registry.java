package cn.dc.zero.rpc.core.registry;



import cn.dc.zero.rpc.core.base.Lifecycle;
import cn.dc.zero.rpc.core.client.ProviderInfo;
import cn.dc.zero.rpc.core.config.ConsumerConfig;
import cn.dc.zero.rpc.core.config.ProviderConfig;
import cn.dc.zero.rpc.core.config.RegistryConfig;
import cn.dc.zero.rpc.core.ext.Extensible;

import java.util.List;

@Extensible(singleton = false)
public abstract class Registry implements Lifecycle {

    protected RegistryConfig registryConfig;

    protected Registry(RegistryConfig registryConfig) {
        this.registryConfig = registryConfig;
    }

    public abstract  void registry(ProviderConfig providerConfig);

    public abstract boolean start();

    public abstract void unRegistry(ProviderConfig providerConfig);

    public abstract List<ProviderInfo> subscribe(ConsumerConfig consumerConfig);

    public abstract void unSubscribe(ConsumerConfig consumerConfig);

}
