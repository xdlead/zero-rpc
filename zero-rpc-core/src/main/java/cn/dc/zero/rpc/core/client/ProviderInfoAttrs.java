package cn.dc.zero.rpc.core.client;


import cn.dc.zero.rpc.core.common.RpcConstants;

/**
 * @author ：d3137
 * @date ：Created in 2021/11/15 15:11
 * @description：
 * @version:
 */
public class ProviderInfoAttrs {

    /*=====字段配置=====*/
    /**
     * 配置key:serialization
     */
    public static final String ATTR_SERIALIZATION         = RpcConstants.CONFIG_KEY_SERIALIZATION;
    /**
     * 配置key:rpcVersion
     */
    public static final String ATTR_RPC_VERSION           = RpcConstants.CONFIG_KEY_RPC_VERSION;

    /*=====静态态配置=====*/
    /**
     * 配置key:权重
     */
    public static final String ATTR_WEIGHT                = RpcConstants.CONFIG_KEY_WEIGHT;
    /**
     * 静态配置key:appName
     */
    public static final String ATTR_APP_NAME              = RpcConstants.CONFIG_KEY_APP_NAME;
    /**
     * 静态配置key:uniqueId
     */
    public static final String ATTR_UNIQUEID              = RpcConstants.CONFIG_KEY_UNIQUEID;
}
