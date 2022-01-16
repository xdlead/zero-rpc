package cn.dc.zero.rpc.remote.netty.client;


import cn.dc.zero.rpc.core.common.RpcCallBack;
import cn.dc.zero.rpc.core.remote.RemoteHandler;
import cn.dc.zero.rpc.core.remote.RpcRequest;
import cn.dc.zero.rpc.core.remote.RpcResponse;

import java.util.concurrent.Executor;

/**
 * @Author:     DC
 * @Description:  回调客户端处理器
 * @Date:    2022/1/16 20:41
 * @Version:    1.0
 */
public class CallBackInvokeNettyRemoteHandler implements RemoteHandler {

    private RpcCallBack rpcCallBack;

    /**
     * 请求
     */
    protected final RpcRequest request;

    public CallBackInvokeNettyRemoteHandler(RpcRequest request) {
        this.request = request;
    }


    @Override
    public void onResponse(Object result) {

        RpcResponse response = (RpcResponse) result;
        rpcCallBack.onAppResponse(response.getResult(),request.getMethodName(),request);
    }

    @Override
    public void onException(Throwable e) {

    }

    @Override
    public Executor getExecutor() {
        return null;
    }
}
