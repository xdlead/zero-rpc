package cn.dc.zero.rpc.core.context;


import cn.dc.zero.rpc.core.remote.ResponseFuture;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
     * 自定义属性
     */
    protected ConcurrentMap<String, Object> map             = new ConcurrentHashMap<String, Object>();

    /**
     * 设置一个调用上下文数据
     *
     * @param key   Key
     * @param value Value
     */
    public void put(String key, Object value) {
        if (key != null && value != null) {
            map.put(key, value);
        }
    }

    /**
     * 获取一个调用上下文数据
     *
     * @param key Key
     * @return 值
     */
    public Object get(String key) {
        if (key != null) {
            return map.get(key);
        }
        return null;
    }

    /**
     * 删除一个调用上下文数据
     *
     * @param key Key
     * @return 删除前的值
     */
    public Object remove(String key) {
        if (key != null) {
            return map.remove(key);
        }
        return null;
    }
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
