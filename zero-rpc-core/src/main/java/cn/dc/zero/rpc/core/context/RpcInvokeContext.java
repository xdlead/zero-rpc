package cn.dc.zero.rpc.core.context;


import cn.dc.zero.rpc.core.remote.ResponseFuture;

/**
 * @author ：d3137
 * @date ：Created in 2021/11/10 10:50
 * @description：
 * @version:
 */
public class RpcInvokeContext {

    /**
     * 线程上下文变量
     */
    protected static final ThreadLocal<RpcInvokeContext> LOCAL = new ThreadLocal<RpcInvokeContext>();


    /**
     * The Future.
     */
    protected ResponseFuture<?> future;

    /**
     * 得到上下文，没有则初始化
     *
     * @return 调用上下文
     */
    public static RpcInvokeContext getContext() {
        RpcInvokeContext context = LOCAL.get();
        if (context == null) {
            context = new RpcInvokeContext();
            LOCAL.set(context);
        }
        return context;
    }

    /**
     * 查看上下文
     *
     * @return 调用上下文，可能为空
     */
    public static RpcInvokeContext peekContext() {
        return LOCAL.get();
    }


    /**
     * 得到单次请求返回的异步Future对象
     *
     * @param <T> 返回值类型
     * @return 异步Future对象
     */
    @SuppressWarnings("unchecked")
    public <T> ResponseFuture<T> getFuture() {
        return (ResponseFuture<T>) future;
    }

    /**
     * 设置单次请求返回的异步Future对象
     *
     * @param future Future对象
     * @return RpcInvokeContext
     */
    public RpcInvokeContext setFuture(ResponseFuture<?> future) {
        this.future = future;
        return this;
    }
}
