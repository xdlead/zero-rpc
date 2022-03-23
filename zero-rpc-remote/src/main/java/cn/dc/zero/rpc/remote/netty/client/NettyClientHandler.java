package cn.dc.zero.rpc.remote.netty.client;


import cn.dc.zero.rpc.core.remote.RemoteHandler;
import cn.dc.zero.rpc.core.remote.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ：d3137
 * @date ：Created in 2021/11/10 16:56
 * @description：
 * @version:
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private static final ConcurrentHashMap<Integer, RemoteHandler> handlerMap = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) throws Exception {
        RemoteHandler remoteHandler = removeRequest(rpcResponse.getRequestId());
        if(remoteHandler == null){
            //打印日志
            return;
        }

        remoteHandler.onResponse(rpcResponse);
    }

    public RemoteHandler removeRequest(int requestId) {
        return handlerMap.remove(requestId);
    }

    public void putHandler(int requestId,RemoteHandler remoteHandler){
        handlerMap.put(requestId,remoteHandler);
    }
}
