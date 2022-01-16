package cn.dc.zero.rpc.registry.zookeeper;

import cn.dc.zero.rpc.core.client.ProviderHelper;
import cn.dc.zero.rpc.core.client.ProviderInfo;
import cn.dc.zero.rpc.core.client.ProviderInfoAttrs;
import cn.dc.zero.rpc.core.config.ConsumerConfig;
import cn.dc.zero.rpc.core.util.CommonUtils;
import cn.dc.zero.rpc.core.util.StringUtils;
import org.apache.curator.framework.recipes.cache.ChildData;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ：d3137
 * @date ：Created in 2021/11/12 17:42
 * @description：
 * @version:
 */
public class ZookeeperRegistryHelper {

    static List<ProviderInfo> convertUrlsToProviders(String providerPath,
                                                     List<ChildData> currentData) throws UnsupportedEncodingException {
        List<ProviderInfo> providerInfos = new ArrayList<ProviderInfo>();
        if (CommonUtils.isEmpty(currentData)) {
            return providerInfos;
        }

        for (ChildData childData : currentData) {
            providerInfos.add(convertUrlToProvider(providerPath, childData));
        }
        return providerInfos;
    }

    static ProviderInfo convertUrlToProvider(String providerPath,
                                             ChildData childData) throws UnsupportedEncodingException {
        String url = childData.getPath().substring(providerPath.length() + 1); // 去掉头部
        url = URLDecoder.decode(url, "UTF-8");
        ProviderInfo providerInfo = ProviderHelper.toProviderInfo(url);
        return providerInfo;
    }

    public static List<ProviderInfo> matchProviderInfos(ConsumerConfig consumerConfig, List<ProviderInfo> providerInfos) {
        String protocol = consumerConfig.getProtocol();
        List<ProviderInfo> result = new ArrayList<ProviderInfo>();
        for (ProviderInfo providerInfo : providerInfos) {
            if (providerInfo.getProtocol().equalsIgnoreCase(protocol)
                    && StringUtils.equals(consumerConfig.getUniqueId(),
                    providerInfo.getAttr(ProviderInfoAttrs.ATTR_UNIQUEID))) {
                providerInfo.setConsumerConfig(consumerConfig);
                result.add(providerInfo);
            }
        }
        return result;
    }

    public static ProviderInfo matchProviderInfo(ConsumerConfig consumerConfig, ProviderInfo providerInfo) {

        if (StringUtils.equals(consumerConfig.getUniqueId(),
                providerInfo.getAttr(ProviderInfoAttrs.ATTR_UNIQUEID))) {
            providerInfo.setConsumerConfig(consumerConfig);
            return  providerInfo;
        }
        return null;
    }
}
