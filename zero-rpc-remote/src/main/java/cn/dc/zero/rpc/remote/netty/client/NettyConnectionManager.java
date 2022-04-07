package cn.dc.zero.rpc.remote.netty.client;

import cn.dc.zero.rpc.core.client.ClientRemoteConfig;
import cn.dc.zero.rpc.core.client.ProviderInfo;
import cn.dc.zero.rpc.core.common.RpcConstants;
import cn.dc.zero.rpc.core.remote.Url;
import cn.dc.zero.rpc.core.serializer.RpcSerializerFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Author:     DC
 * @Description:  netty连接池管理
 * @Date:    2022/1/16 20:42
 * @Version:    1.0
 */
public class NettyConnectionManager {

    //可用长连接池
    private static ConcurrentHashMap<Url, Channel> reuse_connection_map = new ConcurrentHashMap<>();

    private static EventLoopGroup eventLoopGroup = new NioEventLoopGroup(2);




    public  static Channel getConnection(Url url, ClientRemoteConfig clientRemoteConfig){
        Channel oldChannel = reuse_connection_map.get(url);
        if(oldChannel != null && oldChannel.isActive()){
            return reuse_connection_map.get(url);
        }
        if(oldChannel != null){
            oldChannel.close();
        }
        Channel channel = connect(url,clientRemoteConfig.getProviderInfo());
        reuse_connection_map.put(url,channel);
        return channel;
    }

    private  static Channel connect(Url url, ProviderInfo providerInfo){
        Bootstrap b = new Bootstrap();
        b.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new NettyClientInitializer(RpcSerializerFactory.getSerializer(providerInfo.getSerializable()),new NettyClientHandler()));
        ChannelFuture channelFuture = b.connect(getConnectAddress(url));
        boolean ret = channelFuture.awaitUninterruptibly(10000, TimeUnit.MILLISECONDS);
        if(ret && channelFuture.isSuccess()){
            Channel oldConnect = reuse_connection_map.get(url);
            if(oldConnect != null){
                oldConnect.close();
            }
            reuse_connection_map.put(url,channelFuture.channel());
            return channelFuture.channel();
        }
        //抛出异常
        return null;
    }

    private static InetSocketAddress getConnectAddress(Url url){
        return new InetSocketAddress(url.getIp(), url.getPort());
    }




}
