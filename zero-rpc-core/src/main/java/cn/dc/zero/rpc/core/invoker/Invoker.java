package cn.dc.zero.rpc.core.invoker;


import cn.dc.zero.rpc.core.remote.RpcRequest;
import cn.dc.zero.rpc.core.remote.RpcResponse;

public interface Invoker {

    RpcResponse invoke(RpcRequest rpcRequest);

}
