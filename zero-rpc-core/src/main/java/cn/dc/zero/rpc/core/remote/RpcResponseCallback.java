package cn.dc.zero.rpc.core.remote;

/**
 * @author ：d3137
 * @date ：Created in 2021/10/28 15:12
 * @description：
 * @version:
 */
public interface RpcResponseCallback <T>{

    void onResponse(Object appResponse, String methodName, RpcRequest request);
}
