package cn.dc.zero.rpc.core.client;


import cn.dc.zero.rpc.core.config.ConsumerConfig;
import cn.dc.zero.rpc.core.filter.FilterChain;
import cn.dc.zero.rpc.core.invoker.Invoker;
import cn.dc.zero.rpc.core.remote.RpcRequest;
import cn.dc.zero.rpc.core.remote.RpcResponse;

public class ClientProxyInvoker implements Invoker {

    protected final ConsumerConfig consumerConfig;



    /**
     * 过滤器执行链
     */
    private final FilterChain filterChain;


    public ClientProxyInvoker(ConsumerConfig consumerConfig, FilterChain filterChain) {
        this.consumerConfig = consumerConfig;
        this.filterChain = filterChain;
    }

    @Override
    public RpcResponse invoke(RpcRequest rpcRequest) {
        rpcRequest.setInvokeType(consumerConfig.getInvokeType());
        rpcRequest.setUniqueId(consumerConfig.getUniqueId());
        return filterChain.invoke(rpcRequest);
    }
}
