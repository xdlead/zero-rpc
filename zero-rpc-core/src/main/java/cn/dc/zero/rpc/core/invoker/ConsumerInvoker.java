package cn.dc.zero.rpc.core.invoker;


import cn.dc.zero.rpc.core.client.ProviderInfo;
import cn.dc.zero.rpc.core.common.RpcConfigs;
import cn.dc.zero.rpc.core.common.RpcConstants;
import cn.dc.zero.rpc.core.common.RpcOptions;
import cn.dc.zero.rpc.core.config.ConsumerConfig;
import cn.dc.zero.rpc.core.context.RpcInvokeContext;
import cn.dc.zero.rpc.core.exception.RpcRuntimeException;
import cn.dc.zero.rpc.core.filter.FilterInvoker;
import cn.dc.zero.rpc.core.log.LogCodes;
import cn.dc.zero.rpc.core.remote.*;

/**
 * @author ：d3137
 * @date ：Created in 2021/11/1 11:04
 * @description：
 * @version:
 */
public class ConsumerInvoker<T> extends FilterInvoker {

    private ConsumerConfig consumerConfig;

    public ConsumerInvoker(ConsumerConfig consumerConfig) {
        super(consumerConfig);
        this.consumerConfig = consumerConfig;
    }

    @Override
    public RpcResponse invoke(RpcRequest request)  {
        ProviderInfo providerInfo = consumerConfig.getLoadBalancer().select(request,consumerConfig.getProviderInfoList());
        if(providerInfo == null){
            //抛出异常
           throw new RpcRuntimeException(LogCodes.getLog(LogCodes.PROVIDER_NOT_AVAILABLE,request.getInterfaceId(),request.getUniqueId(),request.getMethodName()));
        }
        Remote remote = RemoteManager.getRemote(providerInfo);
        if(remote == null) {
            //抛出异常
            throw new RpcRuntimeException(LogCodes.getLog(LogCodes.REMOTE_NOT_AVAILABLE,providerInfo.getOriginUrl()));
        }
        //TODO 可以对单个服务进行超时配置
        if(request.getInvokeType().equals(RpcConstants.INVOKER_TYPE_FUTURE)){
            ResponseFuture responseFuture = remote.asyncSend(request, RpcConfigs.getIntValue(RpcOptions.CONSUMER_INVOKE_TIMEOUT));
            RpcInvokeContext.getContext().setFuture(responseFuture);

            return new RpcResponse();
        }else if(request.getInvokeType().equals(RpcConstants.INVOKER_TYPE_SYNC)){
            return remote.syncSend(request, RpcConfigs.getIntValue(RpcOptions.CONSUMER_INVOKE_TIMEOUT));
        }else if(request.getInvokeType().equals(RpcConstants.INVOKER_TYPE_CALLBACK)){
            //回调函数执行过程
            return remote.syncSend(request, RpcConfigs.getIntValue(RpcOptions.CONSUMER_INVOKE_TIMEOUT));
        }else if(request.getInvokeType().equals(RpcConstants.INVOKER_TYPE_ONEWAY)){
            remote.oneWaySend(request, RpcConfigs.getIntValue(RpcOptions.CONSUMER_INVOKE_TIMEOUT));
            //返回空对象，单次调用不需要返回值
            return new RpcResponse();
        }
        RpcResponse rpcResponse = new RpcResponse();
        //

        return rpcResponse;
    }
}
