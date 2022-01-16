package cn.dc.zero.rpc.core.filter;


import cn.dc.zero.rpc.core.config.ConsumerConfig;
import cn.dc.zero.rpc.core.ext.Extensible;
import cn.dc.zero.rpc.core.remote.RpcRequest;
import cn.dc.zero.rpc.core.remote.RpcResponse;

/**
 * @author ：d3137
 * @date ：Created in 2021/10/26 17:39
 * @description：
 * @version:
 */
@Extensible
public abstract class Filter {
    /**
     * Is this filter need load in this invoker
     *
     * @param invoker Filter invoker contains ProviderConfig or ConsumerConfig.
     * @return is need load
     */
    public boolean needToLoad(FilterInvoker invoker) {
        return true;
    }

    /**
     * Do filtering
     * <p>
     * <pre><code>
     *  doBeforeInvoke(); // the code before invoke, even new dummy response for return (skip all next invoke).
     *  SofaResponse response = invoker.invoke(request); // do next invoke(call next filter, call remote, call implements).
     *  doAfterInvoke(); // the code after invoke
     * </code></pre>
     *
     * @param invoker Invoker
     * @param request Request
     * @return SofaResponse Response
     */
    public abstract RpcResponse invoke(FilterInvoker invoker, RpcRequest request) ;


    public void onAsyncResponse(ConsumerConfig config, RpcRequest request, RpcResponse response, Throwable exception)
             {
    }
}
