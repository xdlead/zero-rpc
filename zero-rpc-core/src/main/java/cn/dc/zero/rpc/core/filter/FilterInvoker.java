package cn.dc.zero.rpc.core.filter;


import cn.dc.zero.rpc.core.config.AbstractServiceConfig;
import cn.dc.zero.rpc.core.invoker.Invoker;
import cn.dc.zero.rpc.core.remote.RpcRequest;
import cn.dc.zero.rpc.core.remote.RpcResponse;

/**
 *
 */
public class FilterInvoker implements Invoker {
    /**
     * 下一层过滤器
     */
    protected Filter                  nextFilter;

    /**
     * 下一层Invoker
     */
    protected FilterInvoker           invoker;

    /**
     * 过滤器所在的接口，可能是provider或者consumer
     */
    protected AbstractServiceConfig config;


    /**
     * 构造函数
     *
     * @param nextFilter 下一层过滤器
     * @param invoker    下一层调用器
     */
    public FilterInvoker(Filter nextFilter, FilterInvoker invoker) {
        this.nextFilter = nextFilter;
        this.invoker = invoker;

    }

    protected FilterInvoker(AbstractServiceConfig config) {
        this.config = config;
    }

    @Override
    public RpcResponse invoke(RpcRequest request)  {
        if (nextFilter == null && invoker == null) {
           //exception catch
            return  null;
        }
        return nextFilter == null ?
                invoker.invoke(request) :
                nextFilter.invoke(invoker, request);
    }





}
