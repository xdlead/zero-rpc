package cn.dc.zero.rpc.remote.http.client;

import cn.dc.zero.rpc.core.remote.RemoteHandler;

import java.util.concurrent.Executor;

/**
 * @Author: DC
 * @Description:
 * @Date: 2022/1/23 21:35
 * @Version: 1.0
 */
public class SyncInvokeHttpRemoteHandler implements RemoteHandler {
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
