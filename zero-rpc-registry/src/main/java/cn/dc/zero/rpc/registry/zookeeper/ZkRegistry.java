package cn.dc.zero.rpc.registry.zookeeper;

import cn.dc.zero.rpc.core.client.ProviderInfo;
import cn.dc.zero.rpc.core.config.AbstractServiceConfig;
import cn.dc.zero.rpc.core.config.ConsumerConfig;
import cn.dc.zero.rpc.core.config.ProviderConfig;
import cn.dc.zero.rpc.core.config.RegistryConfig;
import cn.dc.zero.rpc.core.exception.RpcRuntimeException;
import cn.dc.zero.rpc.core.ext.Extension;
import cn.dc.zero.rpc.core.listener.ProviderInfoListener;
import cn.dc.zero.rpc.core.registry.Registry;
import cn.dc.zero.rpc.core.registry.RegistryUtils;
import cn.dc.zero.rpc.core.util.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Extension("zookeeper")
public class ZkRegistry extends Registry {

    private final static Logger LOGGER   = LoggerFactory.getLogger(ZkRegistry.class);

    private CuratorFramework zkClient;

    private volatile ConcurrentHashMap<ProviderConfig, List<String>> providers = new ConcurrentHashMap<>();

    private volatile ConcurrentHashMap<ConsumerConfig,String> consumers  = new ConcurrentHashMap<>();

    private String rootPath = "/";

    private boolean ephemeralNode =true;

    private ZookeeperProviderObserver providerObserver;

    private static final ConcurrentMap<ConsumerConfig, PathChildrenCache> INTERFACE_PROVIDER_CACHE = new ConcurrentHashMap<ConsumerConfig, PathChildrenCache>();


    /**
     * 服务被下线
     */
    private final static byte[]                         PROVIDER_OFFLINE        = new byte[] { 0 };
    /**
     * 正常在线服务
     */
    private final static byte[]                         PROVIDER_ONLINE         = new byte[] { 1 };

    protected ZkRegistry(RegistryConfig registryConfig) {
        super(registryConfig);
    }


    @Override
    public synchronized void init() {
        String addressInput = registryConfig.getAddress();
        if (StringUtils.isEmpty(addressInput)) {
            throw new RpcRuntimeException("register address is empty");
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(
                    "Init ZookeeperRegistry with address {}, root path is {}, ephemeralNode:{}",
                    addressInput, rootPath, ephemeralNode);
        }
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(addressInput)
                .sessionTimeoutMs(registryConfig.getConnectTimeout() * 3)
                .connectionTimeoutMs(registryConfig.getConnectTimeout())
                .canBeReadOnly(false)
                .retryPolicy(retryPolicy)
                .defaultData(null);
        zkClient = builder.build();
        zkClient.getConnectionStateListenable().addListener(new ConnectionStateListener() {


            @Override
            public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("reconnect to zookeeper,recover provider and consumer data");
                }
                if (connectionState == ConnectionState.RECONNECTED) {
                    reconnect();
                }
            }
        } );
    }

    @Override
    public boolean start() {
        if (zkClient == null) {
            return false;
        }
        if (zkClient.getState() == CuratorFrameworkState.STARTED) {
            return true;
        }
        try {
            zkClient.start();
        } catch (Exception e) {
            throw new RpcRuntimeException("Zookeeper registry start error",e);
        }
        return zkClient.getState() == CuratorFrameworkState.STARTED;
    }


    @Override
    public void destroy() {
        closePathChildrenCache(INTERFACE_PROVIDER_CACHE);
        destroyRegistryPath();
        if (zkClient != null && zkClient.getState() == CuratorFrameworkState.STARTED) {
            zkClient.close();
        }
        providers.clear();
        consumers.clear();
    }

    public void destroyRegistryPath(){
        for (Map.Entry<ProviderConfig, List<String>> entry : providers.entrySet()) {
            try {
                for (String path:entry.getValue()){
                    try {
                        System.out.println("destroy path="+path);
                        getAndCheckZkClient().delete().forPath(path);
                    }catch (Exception e){
                        throw new RpcRuntimeException("unRegistry error,path:"+path,e);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("close path cache error", e);
            }
        }
    }

    @Override
    public void registry(ProviderConfig providerConfig) {
        String registryPath = buildProviderPath(providerConfig);
        List<String> urls = RegistryUtils.convertProviderToUrls(providerConfig);
        String providerPath = null;
        for(String content:urls){
            try {
                content = URLEncoder.encode(content, "UTF-8");
                providerPath = registryPath + StringUtils.CONTEXT_SEP + content;
                getAndCheckZkClient().create().creatingParentContainersIfNeeded()
                        .withMode(ephemeralNode ? CreateMode.EPHEMERAL : CreateMode.PERSISTENT) // 是否永久节点
                        .forPath(providerPath,  PROVIDER_ONLINE);
                putProvider(providerConfig,providerPath);// 是否默认上下线
            } catch (KeeperException.NodeExistsException nodeExistsException) {
                //增加抛出异常
                if (LOGGER.isWarnEnabled()) {
                    LOGGER.warn("registry path has exists in zookeeper,path = "+providerPath);
                }
            }catch (Exception e){
                throw new RpcRuntimeException("zookeeper registry error,path = "+providerPath,e);
            }
        }

    }

    private void putProvider(ProviderConfig providerConfig ,String url){
        List<String> urls ;
        if (providers.containsKey(providerConfig)) {
            urls = providers.get(providerConfig);
        } else {
            urls = new ArrayList<>();
            urls.add(url);
            providers.put(providerConfig, urls);
        }
    }



    @Override
    public void unRegistry(ProviderConfig providerConfig) {
        providers.remove(providerConfig);
        INTERFACE_PROVIDER_CACHE.remove(providerConfig);
        String providerPath = buildProviderPath(providerConfig);
        try {
            getAndCheckZkClient().delete().forPath(providerPath);
        }catch (Exception e){
            throw new RpcRuntimeException("unRegistry error",e);
        }
    }

    private void closePathChildrenCache(Map<ConsumerConfig, PathChildrenCache> map) {
        for (Map.Entry<ConsumerConfig, PathChildrenCache> entry : map.entrySet()) {
            try {
                entry.getValue().close();
            } catch (Exception e) {
                LOGGER.error("close path cache error", e);
            }
        }
    }

    @Override
    public List<ProviderInfo> subscribe(ConsumerConfig consumerConfig) {
        final String providerPath = buildProviderPath(consumerConfig);
        PathChildrenCache pathChildrenCache = INTERFACE_PROVIDER_CACHE.get(consumerConfig);
        if (providerObserver == null) {
            providerObserver = new ZookeeperProviderObserver();
        }
        try {
            if(pathChildrenCache == null){
                pathChildrenCache = new PathChildrenCache(zkClient, providerPath, true);
                final PathChildrenCache finalPathChildrenCache = pathChildrenCache;
                pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
                    @Override
                    public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {
                        switch (event.getType()) {
                            case CHILD_ADDED: //加了一个provider
                                providerObserver.addProvider(consumerConfig, providerPath, event.getData());
                                break;
                            case CHILD_REMOVED: //删了一个provider
                                providerObserver.removeProvider(consumerConfig, providerPath, event.getData());
                                break;
                            case CHILD_UPDATED: // 更新一个Provider
                                providerObserver.updateProvider(consumerConfig, providerPath, event.getData());
                                break;
                            default:
                                break;
                        }
                    }
                });
                pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
                INTERFACE_PROVIDER_CACHE.put(consumerConfig,pathChildrenCache);
            }
            List<ProviderInfoListener> listeners = consumerConfig.getProviderInfoListeners();
            listeners.forEach(listener ->{
                providerObserver.addProviderListener(consumerConfig,listener);
            });
            List<ProviderInfo> providerInfos = ZookeeperRegistryHelper.convertUrlsToProviders(
                    providerPath, pathChildrenCache.getCurrentData());
            List<ProviderInfo> matchProviders = ZookeeperRegistryHelper.matchProviderInfos(consumerConfig, providerInfos);
            return matchProviders;
        } catch (Exception e) {
            throw new RpcRuntimeException("subscribe provider error,providerPath = "+providerPath);
        }
    }

    @Override
    public void unSubscribe(ConsumerConfig consumerConfig) {
        PathChildrenCache childrenCache = INTERFACE_PROVIDER_CACHE.remove(consumerConfig);
        consumers.remove(consumerConfig);
        providerObserver.removeProviderListener(consumerConfig);
        if (childrenCache != null) {
            try {
                childrenCache.close();
            } catch (Exception e) {
                throw new RpcRuntimeException("取消订阅失败");
            }
        }
    }

    private void reconnect(){
        providers.keySet().forEach(p -> {
            registry(p);
        });
        consumers.keySet().forEach(c -> {
            subscribe(c);
        });
    }

    private String buildProviderPath(AbstractServiceConfig config) {
        String providerPath =  rootPath + "iot-rpc/" + config.getInterfaceId() + "/providers";
        return providerPath;

    }


    private CuratorFramework getAndCheckZkClient() {
        if (zkClient == null || zkClient.getState() != CuratorFrameworkState.STARTED) {
            throw  new RpcRuntimeException("zookeeper client is not available");
        }
        return zkClient;
    }
}
