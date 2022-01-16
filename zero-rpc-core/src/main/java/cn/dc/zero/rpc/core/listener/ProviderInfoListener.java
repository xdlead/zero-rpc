package cn.dc.zero.rpc.core.listener;



import cn.dc.zero.rpc.core.client.ProviderInfo;

import java.util.List;

/**
 * @author ：d3137
 * @date ：Created in 2021/11/1 17:58
 * @description：
 * @version:
 */
public interface ProviderInfoListener {


    void addProvider(ProviderInfo providerInfo);

    void addProviders(List<ProviderInfo> providerInfos);


    void removeProvider(ProviderInfo providerInfo);

    void removeProviders(List<ProviderInfo> providerInfos);

    void updateProvider(ProviderInfo providerInfo);
    void updateProviders(List<ProviderInfo> providerInfos);

    void updateAllProviders(List<ProviderInfo> providerInfos);
}
