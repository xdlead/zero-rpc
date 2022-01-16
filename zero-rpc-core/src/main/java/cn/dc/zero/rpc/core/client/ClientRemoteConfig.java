package cn.dc.zero.rpc.core.client;


import cn.dc.zero.rpc.core.config.ConsumerConfig;

/**
 * @author ：d3137
 * @date ：Created in 2021/10/28 11:49
 * @description：
 * @version:
 */
public class ClientRemoteConfig {

    protected ConsumerConfig consumerConfig;

    protected ProviderInfo providerInfo;

    public ClientRemoteConfig(ConsumerConfig consumerConfig,ProviderInfo providerInfo) {
        this.consumerConfig = consumerConfig;
        this.providerInfo = providerInfo;
    }

    public ConsumerConfig getConsumerConfig() {
        return consumerConfig;
    }

    public void setConsumerConfig(ConsumerConfig consumerConfig) {
        this.consumerConfig = consumerConfig;
    }

    public ProviderInfo getProviderInfo() {
        return providerInfo;
    }

    public void setProviderInfo(ProviderInfo providerInfo) {
        this.providerInfo = providerInfo;
    }
}
