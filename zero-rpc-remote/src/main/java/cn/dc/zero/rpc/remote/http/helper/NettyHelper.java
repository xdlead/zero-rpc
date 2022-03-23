package cn.dc.zero.rpc.remote.http.helper;

import cn.dc.zero.rpc.core.common.NamedThreadFactory;
import cn.dc.zero.rpc.core.common.RpcConstants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

import static cn.dc.zero.rpc.core.common.RpcConfigs.getBooleanValue;
import static cn.dc.zero.rpc.core.common.RpcConfigs.getIntValue;
import static cn.dc.zero.rpc.core.common.RpcConstants.TRANSPORT_CLIENT_IO_THREADS;

/**
 * @Author: DC
 * @Description: http 远程客户端的netty 辅助
 * @Date: 2022/1/20 22:34
 * @Version: 1.0
 */
public class NettyHelper {

    private static final Logger LOGGER           = LoggerFactory
            .getLogger(NettyHelper.class);

    /**
     * 服务端Boss线程池（一种协议一个）
     */
    private static ConcurrentMap<String, EventLoopGroup> serverBossGroups = new ConcurrentHashMap<String, EventLoopGroup>();
    /**
     * 服务端IO线程池（一种协议一个）
     */
    private static ConcurrentMap<String, EventLoopGroup>        serverIoGroups   = new ConcurrentHashMap<String, EventLoopGroup>();

    /**
     * 由于线程池是公用的，需要计数器，在最后一个人关闭时才能销毁
     */
    private static ConcurrentMap<EventLoopGroup, AtomicInteger> refCounter       = new ConcurrentHashMap<EventLoopGroup, AtomicInteger>();


    /**
     * 客户端IO线程 全局共用
     */
    private volatile static EventLoopGroup clientIOEventLoopGroup;

    /**
     * 获取客户端IO线程池
     *
     * @return 客户端IO线程池
     */
    public synchronized static EventLoopGroup getClientIOEventLoopGroup() {
        if (clientIOEventLoopGroup != null && clientIOEventLoopGroup.isShutdown()) {
            clientIOEventLoopGroup = null;
        }
        if (clientIOEventLoopGroup == null) {
            int clientIoThreads = getIntValue(TRANSPORT_CLIENT_IO_THREADS);
            int threads = clientIoThreads > 0 ?
                    clientIoThreads : // 用户配置
                    Math.max(4, Runtime.getRuntime().availableProcessors() + 1); // 默认cpu+1,至少4个
            NamedThreadFactory threadName = new NamedThreadFactory("CLI-IO", true);
            boolean useEpoll = true;
            clientIOEventLoopGroup = useEpoll ? new EpollEventLoopGroup(threads, threadName)
                    : new NioEventLoopGroup(threads, threadName);
            refCounter.putIfAbsent(clientIOEventLoopGroup, new AtomicInteger(0));
        }
        return clientIOEventLoopGroup;
    }

    /**
     * 关闭客户端IO线程池
     */
    public synchronized static void closeClientIOEventGroup() {
        if (clientIOEventLoopGroup != null) {
            AtomicInteger ref = refCounter.get(clientIOEventLoopGroup);
            if (ref.decrementAndGet() <= 0) {
                if (!clientIOEventLoopGroup.isShutdown() && !clientIOEventLoopGroup.isShuttingDown()) {
                    clientIOEventLoopGroup.shutdownGracefully();
                }
                refCounter.remove(clientIOEventLoopGroup);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Closing Client EventLoopGroup, ref : 0");
                }
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Client EventLoopGroup still has ref : {}", ref.get());
                }
            }
        }
        clientIOEventLoopGroup = null;
    }



    private static AdaptiveRecvByteBufAllocator recvByteBufAllocator = AdaptiveRecvByteBufAllocator.DEFAULT;

    private static ByteBufAllocator byteBufAllocator     = ByteBufAllocator.DEFAULT;

    public static ByteBufAllocator getByteBufAllocator() {
        return byteBufAllocator;
    }

    public static ByteBuf getBuffer() {
        return byteBufAllocator.buffer();
    }

    public static ByteBuf getBuffer(int size) {
        return byteBufAllocator.buffer(size);
    }

    public static AdaptiveRecvByteBufAllocator getRecvByteBufAllocator() {
        return recvByteBufAllocator;
    }



    public static String toString(ByteBuf byteBuf) {
        if (byteBuf == null) {
            return null;
        }
        byte[] bs;
        int readIndex = byteBuf.readerIndex();
        if (byteBuf.hasArray()) {
            bs = byteBuf.array();
        } else {
            bs = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(bs);
        }
        // 恢复Index
        byteBuf.readerIndex(readIndex);
        return new String(bs, RpcConstants.DEFAULT_CHARSET);
    }
}
