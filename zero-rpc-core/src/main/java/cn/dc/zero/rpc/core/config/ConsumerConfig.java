package cn.dc.zero.rpc.core.config;



import cn.dc.zero.rpc.core.balancer.LoadBalancerFactory;
import cn.dc.zero.rpc.core.client.ClientProxyInvoker;
import cn.dc.zero.rpc.core.client.LoadBalancer;
import cn.dc.zero.rpc.core.client.ProviderInfo;
import cn.dc.zero.rpc.core.common.ConcurrentHashSet;
import cn.dc.zero.rpc.core.common.RpcConstants;
import cn.dc.zero.rpc.core.exception.RpcRuntimeException;
import cn.dc.zero.rpc.core.filter.FilterChain;
import cn.dc.zero.rpc.core.invoker.ConsumerInvoker;
import cn.dc.zero.rpc.core.invoker.Invoker;
import cn.dc.zero.rpc.core.listener.DefaultProviderInfoListener;
import cn.dc.zero.rpc.core.listener.ProviderInfoListener;
import cn.dc.zero.rpc.core.log.LogCodes;
import cn.dc.zero.rpc.core.proxy.ProxyFactory;
import cn.dc.zero.rpc.core.registry.Registry;
import cn.dc.zero.rpc.core.registry.RegistryFactory;
import cn.dc.zero.rpc.core.util.ClassUtils;
import cn.dc.zero.rpc.core.util.CommonUtils;
import cn.dc.zero.rpc.core.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
/**
 * @Author:     DC
 * @Description:  消费者配置
 * @Date:    2021/12/30 22:55
 * @Version:    1.0
 */
public class ConsumerConfig<T> extends  AbstractServiceConfig<ConsumerConfig<T>> {

    protected transient volatile T  proxyIns;

    /**
     * 代理的Invoker对象
     */
    protected transient volatile Invoker proxyInvoker;

    /**
     * @description 服务提供者列表
     */
    protected transient volatile List<ProviderInfo> providerInfoList;

    private ProviderInfoListener providerInfoListener;

    //调用类型
    private String invokeType = RpcConstants.INVOKER_TYPE_SYNC;

    //代理类生成方式，目前支持 jdk
    private String proxy = "jdk";

    protected transient volatile Class  proxyClass;

    protected FilterChain filterChain;

    private LoadBalancer loadBalancer;

    private String balancer = "random";

    protected String  protocol= "netty";



    /**
    * @description: 消费者配置开启
    * @param: []
    * @return: T
    * @author: DC
    */
    public T refer() {
        try {
            checkConfig();
            consumerInit();
            this.providerInfoListener = new DefaultProviderInfoListener(this);
            proxyInvoker = new ClientProxyInvoker(this,filterChain);
            proxyIns = (T) ProxyFactory.buildProxy(proxy,getProxyClass(),proxyInvoker);
            consumerSubscribe();
            return proxyIns;
        }catch (RpcRuntimeException e){
            throw  e;
        }catch (Exception e){
            throw new RpcRuntimeException(LogCodes.getLog(LogCodes.ERROR_BUILD_CONSUMER_CONFIG,interfaceId,uniqueId));
        }

    }

    public void checkConfig(){
        if(uniqueId == null || StringUtils.isBlank(uniqueId)){
            throw new RpcRuntimeException(LogCodes.getLog(LogCodes.ERROR_CONSUMER_CONFIG,"consumer.uniqueId","null","uniqueId must be not null"));
        }
        if((uniqueId == null || StringUtils.isBlank(uniqueId)) && proxyClass == null){
            throw new RpcRuntimeException(LogCodes.getLog(LogCodes.ERROR_CONSUMER_CONFIG,"consumer.uniqueId and consumer.proxyClass","null","uniqueId and proxyClass must have one "));
        }
    }

    /**
     * @description: 消费者配置关闭
     * @param: []
     * @return: []
     * @author: DC
     */
    public void unRefer() {
        if (proxyIns == null) {
            return;
        }
    }
    /**
    * @description: 消费者初始化
    * @param: []
    * @return: void
    * @author: DC
    */
    public void consumerInit(){
        //构造客户端调用链
        this.filterChain = FilterChain.buildConsumerChain(this,new ConsumerInvoker<T>(this));

        //注册中心初始化
        registryConfigs.forEach(config ->{
            Registry registry = RegistryFactory.getRegistry(config);
            registry.init();
            if(!registry.start()){
                throw new RpcRuntimeException(LogCodes.getLog(LogCodes.ERROR_BUILD_CONSUMER_REGISTRY,interfaceId,
                        config.getAddress(),config.getProtocol()));
            }
            registry.start();
        });

    }

    /**
    * @description: 消费者订阅
    * @param: []
    * @return: void
    * @author: DC
    */
    public void consumerSubscribe(){
        //订阅服务信息
        registryConfigs.forEach(config ->{
            Registry registry = RegistryFactory.getRegistry(config);
            providerInfoList = registry.subscribe(this);
        });

    }

    public LoadBalancer getLoadBalancer() {
        if(loadBalancer == null){
            loadBalancer = LoadBalancerFactory.getLoadBalancer(this,balancer);
        }
        return loadBalancer;
    }

    public void setLoadBalancer(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    public List<ProviderInfo> getProviderInfoList() {
        return providerInfoList;
    }

    public void setProviderInfoList(List<ProviderInfo> providerInfoList) {
        this.providerInfoList = providerInfoList;
    }

    public String getProtocol() {
        return protocol;
    }

    public ConsumerConfig<T> setProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public void addProvider(ProviderInfo providerInfo) {
        if (providerInfo == null) {
            return;
        }
        ConcurrentHashSet<ProviderInfo> tmp = new ConcurrentHashSet<ProviderInfo>(providerInfoList);
        tmp.add(providerInfo); // 排重
        this.providerInfoList = new ArrayList<ProviderInfo>(tmp);
    }


    public void addProviders(List<ProviderInfo> providerInfos) {
        if (providerInfos == null) {
            return;
        }
        ConcurrentHashSet<ProviderInfo> tmp = new ConcurrentHashSet<ProviderInfo>(providerInfoList);
        tmp.addAll(providerInfos); // 排重
        this.providerInfoList = new ArrayList<ProviderInfo>(tmp);
    }


    public void removeProvider(ProviderInfo providerInfo) {
        if (providerInfo == null) {
            return ;
        }
        ConcurrentHashSet<ProviderInfo> tmp = new ConcurrentHashSet<ProviderInfo>(providerInfoList);
        tmp.remove(providerInfo); // 排重
        this.providerInfoList = new ArrayList<ProviderInfo>(tmp);
    }


    public void removeProviders(List<ProviderInfo> providerInfos) {
        if (CommonUtils.isEmpty(providerInfos)) {
            return;
        }
        ConcurrentHashSet<ProviderInfo> tmp = new ConcurrentHashSet<ProviderInfo>(this.providerInfoList);
        tmp.removeAll(providerInfos); // 排重
        this.providerInfoList = new ArrayList<ProviderInfo>(tmp);
    }


    public void updateProvider(ProviderInfo providerInfo) {
        if (providerInfo == null) {
            return ;
        }
        ConcurrentHashSet<ProviderInfo> tmp = new ConcurrentHashSet<ProviderInfo>(providerInfoList);
        tmp.add(providerInfo); // 排重
        this.providerInfoList = new ArrayList<ProviderInfo>(tmp);
    }


    public void updateProviders(List<ProviderInfo> providerInfos) {
        if (CommonUtils.isEmpty(providerInfos)) {
            return;
        }
        ConcurrentHashSet<ProviderInfo> tmp = new ConcurrentHashSet<ProviderInfo>(this.providerInfoList);
        tmp.addAll(providerInfos); // 排重
        this.providerInfoList = new ArrayList<ProviderInfo>(tmp);
    }


    public void updateAllProviders(List<ProviderInfo> providerInfos) {

    }



    @Override
    protected Class<?> getProxyClass() {
        if (proxyClass != null) {
            return proxyClass;
        }
        try {
            if (StringUtils.isNotBlank(interfaceId)) {
                this.proxyClass = ClassUtils.forName(interfaceId);
            } else {
                //打印日志
                throw new Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return proxyClass;
    }

    public String getProxy() {
        return proxy;
    }

    public ConsumerConfig<T> setProxy(String proxy) {
        this.proxy = proxy;
        return this;
    }

    public String getBalancer() {
        return balancer;
    }

    public ConsumerConfig<T> setBalancer(String balancer) {
        this.balancer = balancer;
        return this;
    }

    public String getInvokeType() {
        return invokeType;
    }

    public ConsumerConfig<T> setInvokeType(String invokeType) {
        this.invokeType = invokeType;
        return this;
    }

    public ProviderInfoListener getProviderInfoListener() {
        return providerInfoListener;
    }

    public ConsumerConfig<T> setProviderInfoListener(ProviderInfoListener providerInfoListener) {
        this.providerInfoListener = providerInfoListener;
        return this;
    }
}
