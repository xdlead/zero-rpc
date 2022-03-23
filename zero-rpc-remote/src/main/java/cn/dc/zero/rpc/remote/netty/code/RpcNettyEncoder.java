package cn.dc.zero.rpc.remote.netty.code;


import cn.dc.zero.rpc.core.serializer.AbstractByteBuf;
import cn.dc.zero.rpc.core.serializer.RpcSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
/**
 * @Author:     DC
 * @Description:  通信编码器
 * @Date:    2022/1/16 20:37
 * @Version:    1.0
 */
public class RpcNettyEncoder extends MessageToByteEncoder<Object> {

    private RpcSerializer rpcProtocol;

    public RpcNettyEncoder(RpcSerializer rpcProtocol){
        this.rpcProtocol = rpcProtocol;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        byte[] data = rpcProtocol.serialize(o);
        byteBuf.writeInt(data.length);
        byteBuf.writeBytes(data);
    }
}
