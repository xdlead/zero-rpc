package cn.dc.zero.rpc.core.common;

import java.nio.charset.Charset;

import static cn.dc.zero.rpc.core.common.RpcConfigs.getStringValue;

/**
 * @author ：d3137
 * @date ：Created in 2021/11/1 11:36
 * @description：
 * @version:
 */
public class RpcConstants {

    public static final String RPC_IDENTIFIER = "ZERO-RPC";

    public static final String RPC_VERSION = "ZERO-RPC";

    /**
     * 调用方式：同步调用
     */
    public static final String  INVOKER_TYPE_SYNC                  = "sync";

    /**
     * 调用方式：单向
     */
    public static final String  INVOKER_TYPE_ONEWAY                = "oneway";
    /**
     * 调用方式：回调
     */
    public static final String  INVOKER_TYPE_CALLBACK              = "callback";
    /**
     * 调用方式：future
     */
    public static final String  INVOKER_TYPE_FUTURE                = "future";

    public static final int DEFAULT_IO_THREAD = Math.min(Runtime.getRuntime().availableProcessors()+1, 32);


    public static final String  CONFIG_KEY_RPC_VERSION             = "rpcVer";

    /**
     * 配置key:serialization
     */
    public static final String  CONFIG_KEY_SERIALIZATION           = "serialization";


    /**
     * 配置key:weight
     */
    public static final String  CONFIG_KEY_WEIGHT                  = "weight";

    public static final String  CONFIG_KEY_SERVER_TYPE                 = "serverType";

    /**
     * 配置key:alias
     */
    public static final String  CONFIG_KEY_UNIQUEID                = "uniqueId";

    public static final String  CONFIG_KEY_INTERFACE                = "interface";

    public static final String  CONFIG_KEY_APP_NAME                = "appName";

    public static final String  REGISTRY_PROTOCOL_ZK               = "zookeeper";



    /**
     * 配置key:schedule(其他情况暂时不使用)
     */
    public static final String  CONFIG_KEY_NODE_ID                = "nodeId";

    public static final String  CONFIG_KEY_NODE_IDENTIFY                = "nodeIdentify";

    public static final String  CONFIG_KEY_NODE_GROUP               = "nodeGroup";


    /**
     * 默认字符集 utf-8
     */
    public static final Charset DEFAULT_CHARSET                    = Charset
            .forName(getStringValue(RpcOptions.DEFAULT_CHARSET));

    /**
     * 客户端IO线程池
     */
    public static final String TRANSPORT_CLIENT_IO_THREADS              = "transport.client.io.threads";
}
