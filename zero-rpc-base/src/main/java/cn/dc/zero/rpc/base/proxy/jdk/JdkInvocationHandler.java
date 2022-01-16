package cn.dc.zero.rpc.base.proxy.jdk;


import cn.dc.zero.rpc.core.builder.MessageBuilder;
import cn.dc.zero.rpc.core.invoker.Invoker;
import cn.dc.zero.rpc.core.remote.RpcRequest;
import cn.dc.zero.rpc.core.remote.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author:     DC
 * @Description:  
 * @Date:    2022/1/16 22:08
 * @Version:    1.0
 */
public class JdkInvocationHandler implements InvocationHandler {

    private Class  proxyClass;

    private Invoker proxyInvoker;

    public JdkInvocationHandler(Class proxyClass, Invoker proxyInvoker) {
        this.proxyClass = proxyClass;
        this.proxyInvoker = proxyInvoker;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        Class[] paramTypes = method.getParameterTypes();
        RpcRequest rpcRequest = MessageBuilder.buildRequest(proxyClass,method,paramTypes,args);
        RpcResponse rpcResponse = proxyInvoker.invoke(rpcRequest);
        Object result =  rpcResponse.getResult();
        return result;
    }

    public Invoker getProxyInvoker() {
        return proxyInvoker;
    }
}
