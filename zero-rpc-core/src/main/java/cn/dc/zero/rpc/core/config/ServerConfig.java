package cn.dc.zero.rpc.core.config;


import cn.dc.zero.rpc.core.node.NodeInfo;
import cn.dc.zero.rpc.core.serializer.RpcSerializer;
import cn.dc.zero.rpc.core.serializer.RpcSerializerFactory;
import cn.dc.zero.rpc.core.util.NetUtils;

/**
 * 服务配置基础类
 */
public class ServerConfig {

    /**
     * 内部ip或者域名
     */
    protected String  host;

    /**
     * 监听端口
     */
    protected Integer port;

    /**
     * 序列化方式
     */
    private String serializer;

    /**
     * 服务配置类型
     */
    private String serverType;

    /**
     * 序列化实例
     */
    private RpcSerializer rpcSerializer;

    private NodeInfo nodeInfo;

    public String getHost() {
        if (host == null) {
            host = NetUtils.getLocalHost();
        }
        return host;
    }

    public ServerConfig setHost(String host) {
        this.host = host;
        return this;
    }

    public Integer getPort() {
        if (port == null) {
            port = 9527;
        }
        return port;
    }

    public ServerConfig setPort(Integer port) {
        this.port = port;
        return this;
    }

    public String getSerializer() {
        return serializer;
    }

    public ServerConfig setSerializer(String serializer) {
        this.serializer = serializer;
        return this;
    }

    public String getServerType() {
        return serverType;
    }

    public ServerConfig setServerType(String serverType) {
        this.serverType = serverType;
        return  this;
    }

    public RpcSerializer getRpcSerializer() {
        if(rpcSerializer == null){
            rpcSerializer = RpcSerializerFactory.getSerializer(serializer);
        }
        return rpcSerializer;
    }

    public ServerConfig setRpcSerializer(RpcSerializer rpcSerializer) {
        this.rpcSerializer = rpcSerializer;
        return this;
    }

    public NodeInfo getNodeInfo() {
        return nodeInfo;
    }

    public void setNodeInfo(NodeInfo nodeInfo) {
        this.nodeInfo = nodeInfo;
    }
}
