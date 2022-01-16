package cn.dc.zero.rpc.core.common;


import cn.dc.zero.rpc.core.remote.RpcRequest;

/**
 * @author ：d3137
 * @date ：Created in 2021/11/10 14:37
 * @description：RPC回调基类
 * @version:
 */
public interface RpcCallBack<T> {

    /**
     * SOFA RPC will callback this method when server return response success
     *
     * @param appResponse response object
     * @param methodName the invoked method
     * @param request the invoked request object
     */
    void onAppResponse(Object appResponse, String methodName, RpcRequest request);

    /**
     * SOFA RPC will callback this method when server meet exception
     *
     * @param throwable app's exception
     * @param methodName the invoked method
     * @param request the invoked request
     */
    void onAppException(Throwable throwable, String methodName, RpcRequest request);


}
