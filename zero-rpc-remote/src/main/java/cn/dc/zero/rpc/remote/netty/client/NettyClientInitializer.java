package cn.dc.zero.rpc.remote.netty.client;


import cn.dc.zero.rpc.core.serializer.RpcSerializer;
import cn.dc.zero.rpc.remote.netty.code.RpcNettyDecoder;
import cn.dc.zero.rpc.remote.netty.code.RpcNettyEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;


/**
 * @author ：d3137
 * @date ：Created in 2021/11/10 16:54
 * @description：
 * @version:
 */
public class NettyClientInitializer  extends ChannelInitializer<SocketChannel> {

    private RpcSerializer rpcSerializer;

    private NettyClientHandler nettyClientHandler;

    public NettyClientInitializer(RpcSerializer rpcSerializer, NettyClientHandler nettyClientHandler) {
        this.rpcSerializer = rpcSerializer;
        this.nettyClientHandler = nettyClientHandler;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
//        Serializer serializer = ProtostuffSerializer.class.newInstance();
        ChannelPipeline cp = socketChannel.pipeline();
        cp.addLast(new RpcNettyDecoder(rpcSerializer))
                .addLast(nettyClientHandler)
                .addLast(new RpcNettyEncoder(rpcSerializer));
    }
}
