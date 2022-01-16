package cn.dc.zero.rpc.remote.netty.code;


import cn.dc.zero.rpc.core.serializer.RpcSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
/**
 * @Author:     DC
 * @Description:  通信解码器
 * @Date:    2022/1/16 20:36
 * @Version:    1.0
 */
public class RpcNettyDecoder extends ByteToMessageDecoder {

    private RpcSerializer rpcProtocol;

    public RpcNettyDecoder(RpcSerializer rpcProtocol){
        this.rpcProtocol = rpcProtocol;
    }
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < 4) {
            return;
        }
        byteBuf.markReaderIndex();
        int dataLength = byteBuf.readInt();
        if (dataLength < 0) {
            ctx.close();
        }
        if (byteBuf.readableBytes() < dataLength) {
            byteBuf.resetReaderIndex();
            return;	// fix 1024k buffer splice limix
        }
        byte[] data = new byte[dataLength];
        byteBuf.readBytes(data);

        Object obj = rpcProtocol.deserialize(data);
        list.add(obj);
    }
}
