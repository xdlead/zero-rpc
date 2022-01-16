package cn.dc.zero.rpc.core.invoker;


import cn.dc.zero.rpc.core.builder.MessageBuilder;
import cn.dc.zero.rpc.core.config.ProviderConfig;
import cn.dc.zero.rpc.core.filter.FilterInvoker;
import cn.dc.zero.rpc.core.remote.RpcRequest;
import cn.dc.zero.rpc.core.remote.RpcResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author:     DC
 * @Description: 服务端调用处理
 * @Date:    2021/12/30 22:43
 * @Version:    1.0
 */
public class ProviderInvoker<T> extends FilterInvoker {

    private ProviderConfig providerConfig;
    
    public ProviderInvoker(ProviderConfig<T> providerConfig) {
        super(providerConfig);
        this.providerConfig = providerConfig;
    }


    @Override
    public RpcResponse invoke(RpcRequest request)  {

        RpcResponse rpcResponse = MessageBuilder.transform(request);
        Method method = request.getMethod();
        Object result = null;
        try {
            result = method.invoke(providerConfig.getRef(), request.getParameters());
        } catch (IllegalAccessException illegalAccessException) {
            illegalAccessException.printStackTrace();
            rpcResponse.setErrorMsg(illegalAccessException.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            rpcResponse.setErrorMsg(e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            rpcResponse.setErrorMsg(e.getMessage());
        }
        rpcResponse.setResult(result);
        return rpcResponse;
    }
}
