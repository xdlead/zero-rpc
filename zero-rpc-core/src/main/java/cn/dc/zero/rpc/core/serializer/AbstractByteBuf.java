package cn.dc.zero.rpc.core.serializer;

/**
 * @Author: DC
 * @Description:
 * @Date: 2022/1/23 16:48
 * @Version: 1.0
 */
public abstract class AbstractByteBuf {
    /**
     * Get byte[] data
     *
     * @return byte[]
     */
    public abstract byte[] array();

    /**
     * Get length of readable bytes
     *
     * @return length
     */
    public abstract int readableBytes();

    /**
     * release byte buffer
     *
     * @return result
     */
    public abstract boolean release();
}
