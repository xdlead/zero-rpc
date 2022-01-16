package cn.dc.zero.rpc.core.builder;



import cn.dc.zero.rpc.core.remote.RpcRequest;
import cn.dc.zero.rpc.core.remote.RpcResponse;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author:     DC
 * @Description:  消息构造辅助
 * @Date:    2021/12/30 22:39
 * @Version:    1.0
 */
public class MessageBuilder {
    private static AtomicInteger requestId = new AtomicInteger(0);

    public static RpcResponse transform(RpcRequest request){
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(request.getRequestId());
        return rpcResponse;
    }

    public static RpcRequest buildRequest(Class<?> clazz, Method method, Class[] argTypes, Object[] args) {
        RpcRequest request = new RpcRequest();
        request.setInterfaceId(clazz.getName());
        request.setMethodName(method.getName());
        request.setMethod(method);
        request.setParameters(args == null ? new Object[0]: args);
        request.setParameterTypes(argTypes);
        request.setRequestId(requestId.incrementAndGet());
        return request;
    }
}
