package cn.dc.zero.rpc.core.filter;



import cn.dc.zero.rpc.core.common.OrderedComparator;
import cn.dc.zero.rpc.core.config.AbstractServiceConfig;
import cn.dc.zero.rpc.core.config.ConsumerConfig;
import cn.dc.zero.rpc.core.config.ProviderConfig;
import cn.dc.zero.rpc.core.exception.RpcRuntimeException;
import cn.dc.zero.rpc.core.ext.ExtensionClass;
import cn.dc.zero.rpc.core.ext.ExtensionLoader;
import cn.dc.zero.rpc.core.ext.ExtensionLoaderFactory;
import cn.dc.zero.rpc.core.ext.ExtensionLoaderListener;
import cn.dc.zero.rpc.core.invoker.Invoker;
import cn.dc.zero.rpc.core.log.LogCodes;
import cn.dc.zero.rpc.core.remote.RpcRequest;
import cn.dc.zero.rpc.core.remote.RpcResponse;
import cn.dc.zero.rpc.core.util.CommonUtils;
import com.alibaba.fastjson.JSON;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author:     DC
 * @Description:
 * @Date:    2022/1/13 22:29
 * @Version:    1.0
 */
public class FilterChain implements Invoker {


    /**
     * 服务端自动激活的 {"alias":ExtensionClass}
     */
    private final static Map<String, ExtensionClass<Filter>> PROVIDER_AUTO_ACTIVES = Collections
            .synchronizedMap(new LinkedHashMap<String, ExtensionClass<Filter>>());

    /**
     * 调用端自动激活的 {"alias":ExtensionClass}
     */
    private final static Map<String, ExtensionClass<Filter>> CONSUMER_AUTO_ACTIVES = Collections
            .synchronizedMap(new LinkedHashMap<String, ExtensionClass<Filter>>());

    /**x
     * 扩展加载器
     */
    private final static ExtensionLoader<Filter> EXTENSION_LOADER      = buildLoader();

    private static ExtensionLoader<Filter> buildLoader() {
        ExtensionLoader<Filter> extensionLoader = ExtensionLoaderFactory.getExtensionLoader(Filter.class);
        return extensionLoader;
    }

    /**
     * 调用链
     */
    private FilterInvoker invokerChain;

    /**
     * 过滤器列表，从底至上排序
     */
    private List<Filter> loadedFilters;

    /**
     * 构造执行链
     *
     * @param filters     包装过滤器列表
     * @param lastInvoker 最终过滤器
     * @param config      接口配置
     */
    protected FilterChain(List<Filter> filters, FilterInvoker lastInvoker, AbstractServiceConfig config) {
        // 调用过程外面包装多层自定义filter
        // 前面的过滤器在最外层
        invokerChain = lastInvoker;
        if (CommonUtils.isNotEmpty(filters)) {
            loadedFilters = new ArrayList<Filter>();
            for (int i = filters.size() - 1; i >= 0; i--) {
                try {
                    Filter filter = filters.get(i);
                    if (filter.needToLoad(invokerChain)) {
                        invokerChain = new FilterInvoker(filter, invokerChain);
                        // cache this for filter when async respond
                        loadedFilters.add(filter);
                    }
                } catch (RpcRuntimeException e) {
                    //exception catch
                    throw e;
                }catch (Exception e) {
                    //exception catch
                    throw new RpcRuntimeException(LogCodes.getLog(LogCodes.ERROR_LOAD_FILTER_CHAIN, JSON.toJSONString(filters),e));
                }
            }
        }
    }

    /**
     * 构造服务端的执行链
     *
     * @param providerConfig provider配置
     * @param lastFilter     最后一个filter
     * @return filter执行链
     */
    public static FilterChain buildProviderChain(ProviderConfig<?> providerConfig, FilterInvoker lastFilter) {
        return new FilterChain(selectActualFilters(providerConfig, PROVIDER_AUTO_ACTIVES), lastFilter, providerConfig);
    }

    /**
     * 构造调用端的执行链
     *
     * @param consumerConfig consumer配置
     * @param lastFilter     最后一个filter
     * @return filter执行链
     */
    public static FilterChain buildConsumerChain(ConsumerConfig<?> consumerConfig, FilterInvoker lastFilter) {
        return new FilterChain(selectActualFilters(consumerConfig, CONSUMER_AUTO_ACTIVES), lastFilter, consumerConfig);
    }

    /**
     * 获取真正的过滤器列表
     *
     * @param config            provider配置或者consumer配置
     * @param autoActiveFilters 系统自动激活的过滤器映射
     * @return 真正的过滤器列表
     */
    private static List<Filter> selectActualFilters(AbstractServiceConfig config,
                                                    Map<String, ExtensionClass<Filter>> autoActiveFilters) {
        //优先加载用户构建的过滤器
        List<Filter> customFilters = config.getFilterRef() == null ?
                new ArrayList<Filter>() : new CopyOnWriteArrayList<Filter>(config.getFilterRef());

        //加载别名引入的过滤器
        List<ExtensionClass<Filter>> extensionFilters = new ArrayList<ExtensionClass<Filter>>();
        List<String> filterAlias = config.getFilterAlias();
        for(String s:filterAlias){
            ExtensionClass<Filter> extFilter = EXTENSION_LOADER.getExtensionClass(s);
            if(extFilter != null){
                extensionFilters.add(extFilter);
            }
        }
        // 按order从小到大排序
        if (extensionFilters.size() > 1) {
            Collections.sort(extensionFilters, new OrderedComparator<ExtensionClass<Filter>>());
        }
        List<Filter> actualFilters = new ArrayList<Filter>();
        for (ExtensionClass<Filter> extensionFilter : extensionFilters) {
            actualFilters.add(extensionFilter.getExtInstance());
        }
        // 加入自定义的过滤器
        actualFilters.addAll(customFilters);
        return actualFilters;
    }



    @Override
    public RpcResponse invoke(RpcRequest rpcRequest) {
        return invokerChain.invoke(rpcRequest);
    }



    /**
     * 得到执行链
     *
     * @return chain
     */
    protected Invoker getChain() {
        return invokerChain;
    }
}
