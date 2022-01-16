package cn.dc.zero.rpc.remote.netty.client;



import cn.dc.zero.rpc.core.remote.RemoteHandler;
import cn.dc.zero.rpc.remote.netty.message.NettyResponseFuture;

import java.util.concurrent.Executor;

/**
 * @author ：d3137
 * @date ：Created in 2021/11/10 17:21
 * @description：异步调用处理
 * @version:
 */
public class FutureInvokeNettyRemoteHandler implements RemoteHandler {

    public FutureInvokeNettyRemoteHandler(NettyResponseFuture nettyResponseFuture) {
        this.nettyResponseFuture = nettyResponseFuture;
    }

    protected NettyResponseFuture nettyResponseFuture;

    @Override
    public void onResponse(Object result) {

    }

    @Override
    public void onException(Throwable e) {

    }

    @Override
    public Executor getExecutor() {
        return null;
    }
}
