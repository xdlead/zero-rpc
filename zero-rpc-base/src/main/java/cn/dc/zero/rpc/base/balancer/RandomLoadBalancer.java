package cn.dc.zero.rpc.base.balancer;

import cn.dc.zero.rpc.core.client.LoadBalancer;
import cn.dc.zero.rpc.core.client.ProviderInfo;
import cn.dc.zero.rpc.core.config.ConsumerConfig;
import cn.dc.zero.rpc.core.ext.Extension;
import cn.dc.zero.rpc.core.remote.RpcRequest;

import java.util.List;
import java.util.Random;

/**
 * @Author:     DC
 * @Description:  随机算法
 * @Date:    2022/1/16 21:45
 * @Version:    1.0
 */
@Extension("random")
public class RandomLoadBalancer extends LoadBalancer {

    private final Random random = new Random();


    protected RandomLoadBalancer(ConsumerConfig consumerConfig) {
        super(consumerConfig);
    }

    @Override
    public ProviderInfo select(RpcRequest request, List<ProviderInfo> providerInfos) {
        ProviderInfo providerInfo = null;
        int totalWeight = 0;
        boolean isWeightSame = true;
        for (int i=0;i<providerInfos.size();i++){
            int weight = getWeight(providerInfos.get(i));
            totalWeight +=weight;
            if (isWeightSame && i > 0 && weight != getWeight(providerInfos.get(i - 1))) {
                isWeightSame = false; // 计算所有权重是否一样
            }
        }
        if (totalWeight > 0 && !isWeightSame) {
            // 如果权重不相同且权重大于0则按总权重数随机
            int offset = random.nextInt(totalWeight);
            // 并确定随机值落在哪个片断上
            for (int i = 0; i < providerInfos.size(); i++) {
                offset -= getWeight(providerInfos.get(i));
                if (offset < 0) {
                    providerInfo = providerInfos.get(i);
                    break;
                }
            }
        } else {
            // 如果权重相同或权重为0则均等随机
            providerInfo = providerInfos.get(random.nextInt(providerInfos.size()));
        }

        return providerInfo;
    }
}
