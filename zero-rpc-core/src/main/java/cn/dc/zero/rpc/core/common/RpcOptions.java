package cn.dc.zero.rpc.core.common;

/**
 * @author ：d3137
 * @date ：Created in 2021/10/25 17:31
 * @description：RPC参数常量池
 * @version:
 */
public class RpcOptions {

    /**
     * 决定本配置文件的加载顺序，越大越往后加载
     */
    public static final String RPC_CFG_ORDER = "rpc.config.order";

    /**
     * 扩展点加载的路径
     */
    public static final String EXTENSION_LOAD_PATH  = "extension.load.path";

    /**
     * 服务提供者权重
     */
    public static final String PROVIDER_WEIGHT = "provider.weight";

    /**
     * 默认consumer调用provider超时时间
     */
    public static final String CONSUMER_INVOKE_TIMEOUT = "consumer.invoke.timeout";

    /**
     * 默认server host
     */
    public static final String SERVER_HOST = "server.host";

    /**
     * 默认server port
     */
    public static final String SERVER_PORT = "server.port";

    /**
     * 默认server序列化方式
     */
    public static final String SERVER_SERIALIZER = "server.serializer";

    /**
     * 默认server类型
     */
    public static final String SERVER_TYPE = "server.type";

    /**
     * 默认注册中心类型
     */
    public static final String REGISTRY_PROTOCOL = "registry.protocol";

    /**
     * 默认与注册中心超时时间
     */
    public static final String REGISTRY_TIMEOUT= "registry.timeout";






}
