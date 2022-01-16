package cn.dc.zero.rpc.base.proxy.jdk;

import cn.dc.zero.rpc.core.ext.Extension;
import cn.dc.zero.rpc.core.invoker.Invoker;
import cn.dc.zero.rpc.core.proxy.Proxy;
import cn.dc.zero.rpc.core.util.ClassLoaderUtils;

import java.lang.reflect.InvocationHandler;

/**
 * @Author:     DC
 * @Description:  jdk动态代理
 * @Date:    2022/1/16 22:08
 * @Version:    1.0
 */
@Extension("jdk")
public class JdkProxy implements Proxy {
    @Override
    public <T> T getProxy(Class<T> interfaceClass, Invoker proxyInvoker) {
        InvocationHandler handler = new JdkInvocationHandler(interfaceClass, proxyInvoker);
        ClassLoader classLoader = ClassLoaderUtils.getCurrentClassLoader();
        T result = (T) java.lang.reflect.Proxy.newProxyInstance(classLoader,
                new Class[] { interfaceClass }, handler);
        return result;
    }

    @Override
    public Invoker getInvoker(Object proxyObject) {
        return parseInvoker(proxyObject);
    }

    public static Invoker parseInvoker(Object proxyObject) {
        InvocationHandler handler = java.lang.reflect.Proxy.getInvocationHandler(proxyObject);
        if (handler instanceof JdkInvocationHandler) {
            return ((JdkInvocationHandler) handler).getProxyInvoker();
        }
        return null;
    }
}
