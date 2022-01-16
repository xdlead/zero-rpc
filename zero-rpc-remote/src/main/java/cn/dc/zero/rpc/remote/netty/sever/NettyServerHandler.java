package cn.dc.zero.rpc.remote.netty.sever;


import cn.dc.zero.rpc.core.builder.MessageBuilder;
import cn.dc.zero.rpc.core.cache.ReflectCache;
import cn.dc.zero.rpc.core.invoker.Invoker;
import cn.dc.zero.rpc.core.remote.RpcRequest;
import cn.dc.zero.rpc.core.remote.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;
/**
 * @Author:     DC
 * @Description:  netty 处理器
 * @Date:    2022/1/16 20:26
 * @Version:    1.0
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {


    private final  NettyServerManage nettyServerManage;

    public NettyServerHandler(NettyServerManage nettyServerManage){
        this.nettyServerManage = nettyServerManage;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {
        //TODO 改成线程调用，不影响
        Invoker invoker = nettyServerManage.getInvokerMap().get(rpcRequest.getUniqueId());
        RpcResponse rpcResponse;
        if(invoker == null){
            rpcResponse = MessageBuilder.transform(rpcRequest);
            rpcResponse.setErrorMsg("服务不存在");
        }
        buildRequest(rpcRequest);
        rpcResponse = invoker.invoke(rpcRequest);
        channelHandlerContext.channel().writeAndFlush(rpcResponse);
    }

    public void buildRequest(RpcRequest rpcRequest){
        Method method = ReflectCache.getMethodCache(rpcRequest.getUniqueId(),rpcRequest.getMethodName());
        rpcRequest.setMethod(method);
    }


}
