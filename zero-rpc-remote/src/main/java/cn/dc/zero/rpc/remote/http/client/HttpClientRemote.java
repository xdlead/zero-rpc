package cn.dc.zero.rpc.remote.http.client;

import cn.dc.zero.rpc.core.client.ClientRemoteConfig;
import cn.dc.zero.rpc.core.ext.Extension;
import cn.dc.zero.rpc.core.remote.Remote;
import cn.dc.zero.rpc.core.remote.ResponseFuture;
import cn.dc.zero.rpc.core.remote.RpcRequest;
import cn.dc.zero.rpc.core.remote.RpcResponse;
import cn.dc.zero.rpc.remote.http.helper.NettyHelper;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.TimeUnit;

/**
 * @Author: DC
 * @Description:
 * @Date: 2022/1/17 22:25
 * @Version: 1.0
 */
@Extension("http")
public class HttpClientRemote  extends Remote {

    private Channel channel;

    protected HttpClientRemote(ClientRemoteConfig clientRemoteConfig) {
        super(clientRemoteConfig);
    }

    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void connect() {
        if (isAvailable()) {
            return;
        }
//        EventLoopGroup workerGroup = NettyHelper.getClientIOEventLoopGroup();
//        Http2ClientInitializer initializer = new Http2ClientInitializer(transportConfig);
//        try {
//            String host = providerInfo.getHost();
//            int port = providerInfo.getPort();
//            Bootstrap b = new Bootstrap();
//            b.group(workerGroup);
//            b.channel(transportConfig.isUseEpoll() ? EpollSocketChannel.class : NioSocketChannel.class);
//            b.option(ChannelOption.SO_KEEPALIVE, true);
//            b.remoteAddress(host, port);
//            b.handler(initializer);
//
//            // Start the client.
//            Channel channel = b.connect().syncUninterruptibly().channel();
//            this.channel = new NettyChannel(channel);
//
//            // Wait for the HTTP/2 upgrade to occur.
//            Http2SettingsHandler http2SettingsHandler = initializer.settingsHandler();
//            http2SettingsHandler.awaitSettings(transportConfig.getConnectTimeout(), TimeUnit.MILLISECONDS);
//
//            responseChannelHandler = initializer.responseHandler();
//            // RESET streamId
//            streamId.set(START_STREAM_ID);
//        } catch (Exception e) {
//            throw new SofaRpcException(RpcErrorType.CLIENT_NETWORK, e);
//        }
    }

    @Override
    public ResponseFuture asyncSend(RpcRequest request, int timeout) {
        return null;
    }

    @Override
    public RpcResponse syncSend(RpcRequest request, int timeout) {
        return null;
    }

    @Override
    public void oneWaySend(RpcRequest request, int timeout) {

    }

    public boolean isAvailable() {
        return channel != null && channel.isActive();
    }
}
