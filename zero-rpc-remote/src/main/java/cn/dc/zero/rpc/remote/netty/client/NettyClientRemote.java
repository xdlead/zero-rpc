package cn.dc.zero.rpc.remote.netty.client;

import cn.dc.zero.rpc.core.client.ClientRemoteConfig;
import cn.dc.zero.rpc.core.client.ProviderInfo;
import cn.dc.zero.rpc.core.common.RpcConstants;
import cn.dc.zero.rpc.core.ext.Extension;
import cn.dc.zero.rpc.core.remote.*;
import cn.dc.zero.rpc.remote.netty.message.NettyResponseFuture;
import io.netty.channel.Channel;

import java.util.concurrent.TimeUnit;

/**
 * @Author:     DC
 * @Description:  netty 客户端
 * @Date:    2022/1/16 20:30
 * @Version:    1.0
 */
@Extension("netty")
public class NettyClientRemote extends Remote {


    protected Url url;

    protected   NettyConnectionManager connectionManager;

    protected  NettyClientHandler nettyClientHandler = new NettyClientHandler();


    protected NettyClientRemote(ClientRemoteConfig clientRemoteConfig) {
        super(clientRemoteConfig);
        url = convertProviderToUrl(clientRemoteConfig, clientRemoteConfig.getProviderInfo());
    }

    protected Url convertProviderToUrl(ClientRemoteConfig transportConfig, ProviderInfo providerInfo) {
        // Url的第一个参数，如果不用事件的话，其实无所谓
        Url prourl = new Url(providerInfo.toString(), providerInfo.getHost(), providerInfo.getPort());
        return prourl;
    }

    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void connect() {
        connectionManager.getConnection(url,clientRemoteConfig);
    }

    @Override
    public ResponseFuture asyncSend(RpcRequest request, int timeout) {
        NettyResponseFuture nettyResponseFuture = new NettyResponseFuture(request,timeout);
        if(request.getInvokeType() == RpcConstants.INVOKER_TYPE_FUTURE){
            FutureInvokeNettyRemoteHandler futureInvokeNettyRemoteHandler = new FutureInvokeNettyRemoteHandler(nettyResponseFuture);
            nettyClientHandler.putHandler(request.getRequestId(),futureInvokeNettyRemoteHandler);
        }else{
            CallBackInvokeNettyRemoteHandler callBackInvokeNettyRemoteHandler = new CallBackInvokeNettyRemoteHandler(request);
            nettyClientHandler.putHandler(request.getRequestId(),callBackInvokeNettyRemoteHandler);
        }

        Channel channel = connectionManager.getConnection(url,clientRemoteConfig);
        channel.writeAndFlush(request);
        return nettyResponseFuture;

    }

    @Override
    public RpcResponse syncSend(RpcRequest request, int timeout) {
        NettyResponseFuture nettyResponseFuture = new NettyResponseFuture(request,timeout);

        SyncInvokeNettyRemoteHandler syncInvokeNettyRemoteHandler = new SyncInvokeNettyRemoteHandler(nettyResponseFuture);
        nettyClientHandler.putHandler(request.getRequestId(),syncInvokeNettyRemoteHandler);
        Channel channel = connectionManager.getConnection(url,clientRemoteConfig);
        nettyResponseFuture.setSendTime(System.currentTimeMillis());
        channel.writeAndFlush(request);

        try {
            return nettyResponseFuture.getRpcResponse(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public void oneWaySend(RpcRequest request, int timeout) {
        Channel channel = connectionManager.getConnection(url,clientRemoteConfig);
        channel.writeAndFlush(request);
    }


}
