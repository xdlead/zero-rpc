package cn.dc.zero.rpc.core.client;
;

import cn.dc.zero.rpc.core.config.ConsumerConfig;
import cn.dc.zero.rpc.core.ext.Extensible;
import cn.dc.zero.rpc.core.remote.RpcRequest;

import java.util.List;

/**
 * @author ：d3137
 * @date ：Created in 2021/11/10 9:59
 * @description：负载均衡
 * @version:
 */
@Extensible(singleton = false)
public abstract class LoadBalancer {

    protected final ConsumerConfig consumerConfig;

    protected LoadBalancer(ConsumerConfig consumerConfig) {
        this.consumerConfig = consumerConfig;
    }

    public abstract ProviderInfo select(RpcRequest request, List<ProviderInfo> providerInfos);

    protected int getWeight(ProviderInfo providerInfo) {
        // 从provider中或得到相关权重,默认值100
        return providerInfo.getWeight() < 0 ? 0 : providerInfo.getWeight();
    }
}
