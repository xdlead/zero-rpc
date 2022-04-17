package cn.dc.zero.rpc.core.registry;

import cn.dc.zero.rpc.core.client.ProviderInfoAttrs;
import cn.dc.zero.rpc.core.common.RpcConstants;
import cn.dc.zero.rpc.core.config.ProviderConfig;
import cn.dc.zero.rpc.core.config.ServerConfig;
import cn.dc.zero.rpc.core.util.CommonUtils;
import cn.dc.zero.rpc.core.util.NetUtils;
import cn.dc.zero.rpc.core.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
*
* @description: 注册工具类
*
* @author: DC
*
* @create: 2021/12/30
**/
public class RegistryUtils {

    public static <K, V> void initOrAddList(Map<K, List<V>> orginMap, K key, V needAdd) {
        List<V> listeners = orginMap.get(key);
        if (listeners == null) {
            listeners = new CopyOnWriteArrayList<V>();
            listeners.add(needAdd);
            orginMap.put(key, listeners);
        } else {
            listeners.add(needAdd);
        }
    }

    /**
    * @description: url路径转换
    * @param: [providerConfig]
    * @return: java.util.List<java.lang.String>
    * @author: DC
    */
    public static List<String> convertProviderToUrls(ProviderConfig providerConfig) {
        List<String> urls = new ArrayList<>();
        List<ServerConfig> configs = providerConfig.getServer();
        if(configs != null && !configs.isEmpty()){
            configs.forEach(s ->{
                StringBuilder sb = new StringBuilder(200);
                if(s != null){

                    String host = s.getHost(); // 虚拟ip
                    if (host == null) {
                        host = NetUtils.getLocalHost();
                    }
                    Integer port = s.getPort();

                    Map<String, String> metaData = convertProviderToMap(providerConfig, s);
                    sb.append(RpcConstants.RPC_IDENTIFIER).append("://").append(host).append(":")
                            .append(port).append("?version=1.0")
                            .append(convertMap2Pair(metaData));
                    urls.add(sb.toString());

                }
            });
        }
        return urls;
    }



    public static Map<String, String> convertProviderToMap(ProviderConfig providerConfig, ServerConfig server) {
        Map<String, String> metaData = new HashMap<String, String>();
        metaData.put(RpcConstants.CONFIG_KEY_UNIQUEID, providerConfig.getUniqueId());
        metaData.put(RpcConstants.CONFIG_KEY_INTERFACE, providerConfig.getInterfaceId());
        metaData.put(ProviderInfoAttrs.ATTR_WEIGHT, String.valueOf(providerConfig.getWeight()));
        metaData.put(RpcConstants.CONFIG_KEY_SERIALIZATION, server.getSerializer());
        metaData.put(RpcConstants.CONFIG_KEY_SERVER_TYPE,server.getServerType());

        metaData.put(RpcConstants.CONFIG_KEY_NODE_ID,server.getNodeInfo().getNodeId());
        metaData.put(RpcConstants.CONFIG_KEY_NODE_IDENTIFY,server.getNodeInfo().getNodeIdentify());
        metaData.put(RpcConstants.CONFIG_KEY_NODE_GROUP,server.getNodeInfo().getNodeGroup());
        return metaData;
    }

    private static String convertMap2Pair(Map<String, String> map) {

        if (CommonUtils.isEmpty(map)) {
            return StringUtils.EMPTY;
        }

        StringBuilder sb = new StringBuilder(128);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(getKeyPairs(entry.getKey(), entry.getValue()));
        }

        return sb.toString();
    }

    public static String getKeyPairs(String key, Object value) {
        if (value != null) {
            return "&" + key + "=" + value.toString();
        } else {
            return "";
        }
    }


}
