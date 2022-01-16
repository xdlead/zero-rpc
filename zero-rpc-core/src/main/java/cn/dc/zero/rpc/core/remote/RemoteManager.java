package cn.dc.zero.rpc.core.remote;



import cn.dc.zero.rpc.core.client.ClientRemoteConfig;
import cn.dc.zero.rpc.core.client.ProviderInfo;
import cn.dc.zero.rpc.core.ext.ExtensionLoaderFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ：d3137
 * @date ：Created in 2021/10/28 15:01
 * @description：
 * @version:
 */
public class RemoteManager {

    private final  static ConcurrentHashMap<ProviderInfo,Remote> remote_map = new ConcurrentHashMap<>();

    public static  Remote getRemote(ProviderInfo providerInfo){
        Remote  remote;
        if(remote_map.get(providerInfo) != null){
            remote = remote_map.get(providerInfo);
            return remote;
        }
        remote = ExtensionLoaderFactory.getExtensionLoader(Remote.class).getExtension(providerInfo.getProtocol(),
                 new Class[]{ClientRemoteConfig.class},new Object[]{new ClientRemoteConfig(providerInfo.getConsumerConfig(),providerInfo)});
        remote.init();
        remote.connect();
        remote_map.put(providerInfo,remote);
        return remote;

    }
}
