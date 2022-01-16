package cn.dc.zero.rpc.core.listener;


import cn.dc.zero.rpc.core.client.ProviderInfo;
import cn.dc.zero.rpc.core.config.ConsumerConfig;

import java.util.List;

/**
 * @author ：d3137
 * @date ：Created in 2021/11/26 16:33
 * @description：
 * @version:
 */
public class DefaultProviderInfoListener implements ProviderInfoListener{

    /**
     * Origin provider info listener
     */
    private ConsumerConfig consumerConfig;


    public DefaultProviderInfoListener(ConsumerConfig consumerConfig) {
        this.consumerConfig = consumerConfig;
    }

    @Override
    public void addProvider(ProviderInfo providerInfo) {
        consumerConfig.addProvider(providerInfo);
    }

    @Override
    public void addProviders(List<ProviderInfo> providerInfos) {
        consumerConfig.addProviders(providerInfos);
    }

    @Override
    public void removeProvider(ProviderInfo providerInfo) {
        consumerConfig.removeProvider(providerInfo);
    }

    @Override
    public void removeProviders(List<ProviderInfo> providerInfos) {
        consumerConfig.removeProviders(providerInfos);
    }

    @Override
    public void updateProvider(ProviderInfo providerInfo) {
        consumerConfig.updateProvider(providerInfo);
    }

    @Override
    public void updateProviders(List<ProviderInfo> providerInfos) {
        consumerConfig.updateProviders(providerInfos);
    }

    @Override
    public void updateAllProviders(List<ProviderInfo> providerInfos) {
        consumerConfig.updateAllProviders(providerInfos);
    }
}
