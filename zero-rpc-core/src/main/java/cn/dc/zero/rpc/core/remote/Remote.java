package cn.dc.zero.rpc.core.remote;


import cn.dc.zero.rpc.core.base.Lifecycle;
import cn.dc.zero.rpc.core.client.ClientRemoteConfig;
import cn.dc.zero.rpc.core.ext.Extensible;

/**
 * @author ：d3137
 * @date ：Created in 2021/10/28 15:02
 * @description：
 * @version:
 */
@Extensible(singleton = false)
public abstract  class Remote implements Lifecycle {

    protected ClientRemoteConfig clientRemoteConfig;

    protected Remote(ClientRemoteConfig clientRemoteConfig){
        this.clientRemoteConfig = clientRemoteConfig;
    }

    /**
     * 建立长连接
     */
    public abstract void connect();

    /**
     * @description
     * @author d3137
     * @param  * @param request
     * @param timeout
     * @return {@link ResponseFuture}
     */
    public abstract ResponseFuture asyncSend(RpcRequest request, int timeout);

    public abstract RpcResponse syncSend(RpcRequest request, int timeout);

    public abstract void oneWaySend(RpcRequest request, int timeout);

}
