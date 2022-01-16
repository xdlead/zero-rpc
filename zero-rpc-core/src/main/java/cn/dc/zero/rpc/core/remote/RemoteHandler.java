package cn.dc.zero.rpc.core.remote;

import java.util.concurrent.Executor;

/**
 * @author ：d3137
 * @date ：Created in 2021/11/10 14:32
 * @description：客户端通信处理基类
 * @version:
 */
public interface RemoteHandler {

    void onResponse(final Object result);

    /**
     * On exception caught.
     *
     * @param e exception
     */
    void onException(final Throwable e);

    /**
     * User defined executor.
     *
     * @return executor
     */
    Executor getExecutor();
}
