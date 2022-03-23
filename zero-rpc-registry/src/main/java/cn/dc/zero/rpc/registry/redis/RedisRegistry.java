package cn.dc.zero.rpc.registry.redis;

import cn.dc.zero.rpc.core.client.ProviderInfo;
import cn.dc.zero.rpc.core.config.ConsumerConfig;
import cn.dc.zero.rpc.core.config.ProviderConfig;
import cn.dc.zero.rpc.core.config.RegistryConfig;
import cn.dc.zero.rpc.core.registry.Registry;

import java.util.List;

/**
 * @Author: DC
 * @Description: redis 注册器
 * @Date: 2022/2/28 20:59
 * @Version: 1.0
 */
public class RedisRegistry  extends Registry {

    protected RedisRegistry(RegistryConfig registryConfig) {
        super(registryConfig);
    }


    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void registry(ProviderConfig providerConfig) {

    }

    @Override
    public boolean start() {
        return false;
    }

    @Override
    public void unRegistry(ProviderConfig providerConfig) {

    }

    @Override
    public List<ProviderInfo> subscribe(ConsumerConfig consumerConfig) {
        return null;
    }

    @Override
    public void unSubscribe(ConsumerConfig consumerConfig) {

    }
}
