package cn.dc.zero.rpc.core.balancer;


import cn.dc.zero.rpc.core.client.LoadBalancer;
import cn.dc.zero.rpc.core.config.ConsumerConfig;
import cn.dc.zero.rpc.core.ext.ExtensionClass;
import cn.dc.zero.rpc.core.ext.ExtensionLoaderFactory;

/**
 * @Author:     DC
 * @Description:  负载均衡工厂
 * @Date:    2021/12/30 22:39
 * @Version:    1.0
 */
public class LoadBalancerFactory {

    public static LoadBalancer getLoadBalancer(ConsumerConfig consumerConfig , String loadBalancer) {
        try {
            ExtensionClass<LoadBalancer> ext = ExtensionLoaderFactory
                    .getExtensionLoader(LoadBalancer.class).getExtensionClass(loadBalancer);
            if (ext == null) {

            }
            return ext.getExtInstance(new Class[] { ConsumerConfig.class }, new Object[] { consumerConfig });
        } catch (Throwable e) {
           e.printStackTrace();
           return null;
        }
    }
}
