package cn.dc.zero.rpc.core.proxy;


import cn.dc.zero.rpc.core.ext.Extensible;
import cn.dc.zero.rpc.core.invoker.Invoker;

/**
 * @author ：d3137
 * @date ：Created in 2021/10/27 19:35
 * @description：代理基础类
 * @version:
 */
@Extensible(singleton = false)
public interface Proxy {

    <T> T getProxy(Class<T> interfaceClass, Invoker proxyInvoker);

    /**
     * 从代理对象里解析Invoker
     *
     * @param proxyObject 代理对象
     * @return Invoker
     */
    Invoker getInvoker(Object proxyObject);

}
