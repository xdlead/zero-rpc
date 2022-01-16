package cn.dc.zero.rpc.core.client;

import cn.dc.zero.rpc.core.util.CommonUtils;
import cn.dc.zero.rpc.core.util.StringUtils;

/**
 * @Author:     DC
 * @Description:  服务提供者构造辅助类
 * @Date:    2021/12/30 22:45
 * @Version:    1.0
 */
public class ProviderHelper {
    public static ProviderInfo toProviderInfo(String url) {
        ProviderInfo providerInfo = new ProviderInfo();
        providerInfo.setOriginUrl(url);
        try {
            int protocolIndex = url.indexOf("://");
            String remainUrl;
            if (protocolIndex > -1) {
                String protocol = url.substring(0, protocolIndex).toLowerCase();
                providerInfo.setProtocol(protocol);
                remainUrl = url.substring(protocolIndex + 3);
            } else { // 默认
                remainUrl = url;
            }

            int addressIndex = remainUrl.indexOf(StringUtils.CONTEXT_SEP);
            String address;
            if (addressIndex > -1) {
                address = remainUrl.substring(0, addressIndex);
                remainUrl = remainUrl.substring(addressIndex);
            } else {
                int itfIndex = remainUrl.indexOf('?');
                if (itfIndex > -1) {
                    address = remainUrl.substring(0, itfIndex);
                    remainUrl = remainUrl.substring(itfIndex);
                } else {
                    address = remainUrl;
                    remainUrl = "";
                }
            }
            String[] ipAndPort = address.split(":", -1); // TODO 不支持ipv6
            providerInfo.setHost(ipAndPort[0]);
            if (ipAndPort.length > 1) {
                providerInfo.setPort(CommonUtils.parseInt(ipAndPort[1], providerInfo.getPort()));
            }

            // 后面可以解析remainUrl得到interface等 /xxx?a=1&b=2
            if (remainUrl.length() > 0) {
                int itfIndex = remainUrl.indexOf('?');
                if (itfIndex > -1) {
                    String itf = remainUrl.substring(0, itfIndex);
                    providerInfo.setPath(itf);
                    // 剩下是params,例如a=1&b=2
                    remainUrl = remainUrl.substring(itfIndex + 1);
                    String[] params = remainUrl.split("&", -1);
                    for (String parm : params) {
                        String[] kvpair = parm.split("=", -1);
                        if (ProviderInfoAttrs.ATTR_WEIGHT.equals(kvpair[0]) && StringUtils.isNotEmpty(kvpair[1])) {
                            int weight = CommonUtils.parseInt(kvpair[1], providerInfo.getWeight());
                            providerInfo.setWeight(weight);
                        } else if (ProviderInfoAttrs.ATTR_RPC_VERSION.equals(kvpair[0]) &&
                                StringUtils.isNotEmpty(kvpair[1])) {
                            providerInfo.setVersion(kvpair[1]);
                        } else if (ProviderInfoAttrs.ATTR_SERIALIZATION.equals(kvpair[0]) &&
                                StringUtils.isNotEmpty(kvpair[1])) {
                            providerInfo.setSerializable(kvpair[1]);
                        } else {
                            providerInfo.getStaticAttrs().put(kvpair[0], kvpair[1]);
                        }

                    }
                } else {
                    providerInfo.setPath(remainUrl);
                }
            } else {
                providerInfo.setPath(StringUtils.EMPTY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return providerInfo;
    }
}
