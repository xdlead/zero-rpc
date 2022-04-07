package cn.dc.zero.rpc.base.balancer;

import cn.dc.zero.rpc.core.client.LoadBalancer;
import cn.dc.zero.rpc.core.client.ProviderInfo;
import cn.dc.zero.rpc.core.config.ConsumerConfig;
import cn.dc.zero.rpc.core.context.RpcInvokeContext;
import cn.dc.zero.rpc.core.remote.RpcRequest;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author: DC
 * @Description: 固定ip选择
 * @Date: 2022/3/27 16:47
 * @Version: 1.0
 */
public class StaticLoadBalancer extends LoadBalancer {

    protected StaticLoadBalancer(ConsumerConfig consumerConfig) {
        super(consumerConfig);
    }

    @Override
    public ProviderInfo select(RpcRequest request, List<ProviderInfo> providerInfos) {
        RpcInvokeContext context = RpcInvokeContext.getContext();
        String filter = (String) context.get("nodeId");
        for(ProviderInfo p:providerInfos){
            ConcurrentMap<String, Object> dynamicAttrs = p.getDynamicAttrs();
            String nodeId = (String) dynamicAttrs.get("nodeId");
            if(filter.equals(nodeId)){
                return p;
            }
        }
        return null;
    }
}
