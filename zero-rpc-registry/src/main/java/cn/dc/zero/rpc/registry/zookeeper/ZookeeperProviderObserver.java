package cn.dc.zero.rpc.registry.zookeeper;



import cn.dc.zero.rpc.core.client.ProviderInfo;
import cn.dc.zero.rpc.core.config.ConsumerConfig;
import cn.dc.zero.rpc.core.listener.ProviderInfoListener;
import cn.dc.zero.rpc.core.util.CommonUtils;
import org.apache.curator.framework.recipes.cache.ChildData;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author ：d3137
 * @date ：Created in 2021/11/12 17:19
 * @description：
 * @version:
 */
public class ZookeeperProviderObserver {
    private ConcurrentMap<ConsumerConfig, List<ProviderInfoListener>> providerListenerMap = new ConcurrentHashMap<ConsumerConfig, List<ProviderInfoListener>>();

    /**
     * Add provider listener.
     *
     * @param consumerConfig the consumer config
     * @param listener       the listener
     */
    public void addProviderListener(ConsumerConfig consumerConfig, ProviderInfoListener listener) {
        if (listener != null) {
            initOrAddList(providerListenerMap, consumerConfig, listener);
        }
    }

    /**
     * Remove provider listener.
     *
     * @param consumerConfig the consumer config
     */
    public void removeProviderListener(ConsumerConfig consumerConfig) {
        providerListenerMap.remove(consumerConfig);
    }

    /**
     * Update Provider
     *
     * @param config       ConsumerConfig
     * @param providerPath Provider path of zookeeper
     * @param data         Event data
     * @throws UnsupportedEncodingException decode error
     */
    public void updateProvider(ConsumerConfig config, String providerPath, ChildData data)
            throws UnsupportedEncodingException {
        notifyListeners(config, providerPath, data, 2);
    }

    /**
     * Remove Provider
     *
     * @param config       ConsumerConfig
     * @param providerPath Provider path of zookeeper
     * @param data         Event data
     * @throws UnsupportedEncodingException decode error
     */
    public void removeProvider(ConsumerConfig config, String providerPath, ChildData data)
            throws UnsupportedEncodingException {
        notifyListeners(config, providerPath, data, 3);
    }

    /**
     * Add provider
     *
     * @param config       ConsumerConfig
     * @param providerPath Provider path of zookeeper
     * @param data         Event data
     * @throws UnsupportedEncodingException decode error
     */
    public void addProvider(ConsumerConfig config, String providerPath, ChildData data)
            throws UnsupportedEncodingException {
        notifyListeners(config, providerPath, data, 1);
    }

    private void notifyListeners(ConsumerConfig config, String providerPath, ChildData currentData, Integer eventType)
            throws UnsupportedEncodingException {
        List<ProviderInfoListener> providerInfoListeners = providerListenerMap.get(config);
        if (CommonUtils.isNotEmpty(providerInfoListeners)) {
            ProviderInfo providerInfo = ZookeeperRegistryHelper.convertUrlToProvider(providerPath,
                    currentData);
            providerInfo = ZookeeperRegistryHelper.matchProviderInfo(config, providerInfo);
            if(providerInfo == null){
                return;
            }
            for (ProviderInfoListener listener : providerInfoListeners) {
                //eventType 1:add 2:update 3.remove
                if (eventType == 1) {
                    listener.addProvider(providerInfo);
                } else  if (eventType == 2){
                    listener.updateProvider(providerInfo);
                }else  if (eventType == 3){
                    listener.removeProvider(providerInfo);
                }
            }
        }
    }

    public  <K, V> void initOrAddList(Map<K, List<V>> orginMap, K key, V needAdd) {
        List<V> listeners = orginMap.get(key);
        if (listeners == null) {
            listeners = new CopyOnWriteArrayList<V>();
            listeners.add(needAdd);
            orginMap.put(key, listeners);
        } else {
            listeners.add(needAdd);
        }
    }
}
