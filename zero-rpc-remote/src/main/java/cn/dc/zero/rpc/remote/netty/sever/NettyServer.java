package cn.dc.zero.rpc.remote.netty.sever;


import cn.dc.zero.rpc.core.cache.ReflectCache;
import cn.dc.zero.rpc.core.config.ProviderConfig;
import cn.dc.zero.rpc.core.config.ServerConfig;
import cn.dc.zero.rpc.core.exception.RpcRuntimeException;
import cn.dc.zero.rpc.core.ext.Extension;
import cn.dc.zero.rpc.core.invoker.Invoker;
import cn.dc.zero.rpc.core.server.Server;
import cn.dc.zero.rpc.core.util.ClassTypeUtils;
import cn.dc.zero.rpc.remote.netty.code.RpcNettyDecoder;
import cn.dc.zero.rpc.remote.netty.code.RpcNettyEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author:     DC
 * @Description:  netty 服务端
 * @Date:    2022/1/16 20:30
 * @Version:    1.0
 */
@Extension("netty")
public class NettyServer implements Server {

    private final static Logger LOGGER   = LoggerFactory.getLogger(NettyServer.class);

    private ServerConfig serverConfig;

    private ThreadPoolExecutor serverPool = new ThreadPoolExecutor(4,200,2000,TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>(10000));

    private ThreadPoolExecutor handlePool;

    protected Map<String, Invoker> invokerMap = new ConcurrentHashMap<String, Invoker>();

    protected  NettyServerManage nettyServerManage;

    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();
    private ChannelFuture future = null;

    @Override
    public void init(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
        this.nettyServerManage = new NettyServerManage();
    }

    @Override
    public void start() {
        serverPool.execute(new Runnable() {
            @Override
            public void run() {
                bossGroup = new NioEventLoopGroup();
                workerGroup = new NioEventLoopGroup();
                if(serverConfig.getRpcSerializer() == null){
                    throw new RpcRuntimeException("serverConfig check error,rpcProtocol is empty");
                }
                ServerBootstrap bootstrap = new ServerBootstrap();
                try {
                    bootstrap.group(bossGroup, workerGroup)
                            .channel(NioServerSocketChannel.class)
                            .childHandler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                public void initChannel(SocketChannel channel) throws Exception {
                                    channel.pipeline()
                                            //需要参数可配置化
                                            .addLast(new IdleStateHandler(0, 0, 10000, TimeUnit.SECONDS))     // beat 3N, close if idle
                                            .addLast(new RpcNettyDecoder(serverConfig.getRpcSerializer()))
                                            .addLast(new RpcNettyEncoder(serverConfig.getRpcSerializer()))
                                            .addLast(new NettyServerHandler(nettyServerManage));
                                }
                            })
                            .childOption(ChannelOption.TCP_NODELAY, true)
                            .childOption(ChannelOption.SO_KEEPALIVE, true);
                    String host =serverConfig.getHost();
                    int port = serverConfig.getPort();
                    future = bootstrap.bind(host, port).sync();
                    LOGGER.info("netty server start success,host:{},port:{}",serverConfig.getHost(),serverConfig.getPort());
                    future.channel().closeFuture().sync();
                } catch (Exception e) {
                    LOGGER.error("netty server start error",e);
                } finally {
                    bossGroup.shutdownGracefully();
                    workerGroup.shutdownGracefully();
                    future.channel().close();
                }

            }
        });

    }

    @Override
    public boolean isStarted() {
        return false;
    }

    @Override
    public boolean hasNoEntry() {
        return false;
    }

    @Override
    public void stop() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        future.channel().close();
    }

    @Override
    public void registerProcessor(ProviderConfig providerConfig, Invoker instance) {
        String serviceName = providerConfig.getUniqueId();
        Class itfClass = providerConfig.getProxyClass();
        HashMap<String, Method> methodsLimit = new HashMap<String, Method>(16);
        for (Method method : itfClass.getMethods()) {
            String methodName = method.getName();
            if (methodsLimit.containsKey(methodName)) {
                // 重名的方法
            }
            methodsLimit.put(methodName, method);
        }

        for (Map.Entry<String, Method> entry : methodsLimit.entrySet()) {
            // 缓存接口的方法
            ReflectCache.putMethodCache(serviceName, entry.getValue());
            ReflectCache.putMethodSigsCache(serviceName, entry.getKey(),
                    ClassTypeUtils.getTypeStrs(entry.getValue().getParameterTypes(), true));
        }
        nettyServerManage.getInvokerMap().put(serviceName,instance);
        LOGGER.info("service register processor,service name :{}",serviceName);

    }

    @Override
    public void unRegisterProcessor(ProviderConfig providerConfig, boolean closeIfNoEntry) {

    }
}
