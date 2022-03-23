package cn.dc.zero.rpc.remote.http.client;

import cn.dc.zero.rpc.core.serializer.AbstractByteBuf;
import io.netty.buffer.ByteBuf;

/**
 * @Author: DC
 * @Description:
 * @Date: 2022/1/23 16:49
 * @Version: 1.0
 */
public class HttpByteBuffer extends AbstractByteBuf {

    private final ByteBuf byteBuf;

    public HttpByteBuffer(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    @Override
    public byte[] array() {
        if (byteBuf.hasArray()) {
            // 堆内 ByteBuf
            return byteBuf.array();
        } else {
            // 堆外 ByteBuf
            byte[] bs = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(bs);
            return bs;
        }
    }

    @Override
    public int readableBytes() {
        return byteBuf.readableBytes();
    }

    @Override
    public boolean release() {
        return byteBuf.refCnt() <= 0 || byteBuf.release();
    }
}
