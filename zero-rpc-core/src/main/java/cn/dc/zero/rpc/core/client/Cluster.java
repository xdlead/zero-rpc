package cn.dc.zero.rpc.core.client;

import cn.dc.zero.rpc.core.base.Lifecycle;
import cn.dc.zero.rpc.core.config.ConsumerConfig;
import cn.dc.zero.rpc.core.invoker.Invoker;
import cn.dc.zero.rpc.core.remote.RpcRequest;
import cn.dc.zero.rpc.core.remote.RpcResponse;

/**
 * @author ：d3137
 * @date ：Created in 2021/10/28 11:31
 * @description：
 * @version:
 */
public abstract class Cluster implements Invoker, Lifecycle {

    /**
     * 配置
     */
    protected final ConsumerConfig consumerConfig;

    public Cluster(ConsumerConfig consumerConfig) {
        this.consumerConfig = consumerConfig;
    }

    /**
     * 调用远程地址发送消息
     *
     * @param providerInfo 服务提供者信息
     * @param request      请求
     * @return 状态
     */
    public abstract RpcResponse sendMsg(ProviderInfo providerInfo, RpcRequest request) ;
}
