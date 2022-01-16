package cn.dc.zero.rpc.core.server;


import cn.dc.zero.rpc.core.config.ProviderConfig;
import cn.dc.zero.rpc.core.filter.FilterChain;
import cn.dc.zero.rpc.core.invoker.Invoker;
import cn.dc.zero.rpc.core.invoker.ProviderInvoker;
import cn.dc.zero.rpc.core.remote.RpcRequest;
import cn.dc.zero.rpc.core.remote.RpcResponse;

/**
 * @author ：d3137
 * @date ：Created in 2021/10/26 17:27
 * @description：
 * @version:
 */
public class ProviderProxyInvoker implements Invoker {
    /**
     * 对应的客户端信息
     */
    private final ProviderConfig providerConfig;

    /**
     * 过滤器执行链
     */
    private final FilterChain filterChain;

    /**
     * 构造调用链
     * @param providerConfig
     */
    public ProviderProxyInvoker(ProviderConfig providerConfig) {
        this.providerConfig = providerConfig;
        // 最底层是调用过滤器
        this.filterChain = FilterChain.buildProviderChain(providerConfig,
                new ProviderInvoker(providerConfig));
    }

    @Override
    public RpcResponse invoke(RpcRequest rpcRequest) {
        return filterChain.invoke(rpcRequest);
    }
}
