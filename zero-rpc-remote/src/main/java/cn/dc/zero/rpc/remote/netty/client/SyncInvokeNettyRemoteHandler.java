package cn.dc.zero.rpc.remote.netty.client;

import cn.dc.zero.rpc.core.remote.RemoteHandler;
import cn.dc.zero.rpc.core.remote.ResponseFuture;

import java.util.concurrent.Executor;

/**
 * @Author:     DC
 * @Description:  同步处理器
 * @Date:    2022/1/16 20:31
 * @Version:    1.0
 */
public class SyncInvokeNettyRemoteHandler implements RemoteHandler {

    public ResponseFuture responseFuture;

    public SyncInvokeNettyRemoteHandler(ResponseFuture responseFuture) {
        this.responseFuture = responseFuture;
    }

    @Override
    public void onResponse(Object result) {
        responseFuture.setSuccess(result);
    }

    @Override
    public void onException(Throwable e) {

    }

    @Override
    public Executor getExecutor() {
        return null;
    }
}
