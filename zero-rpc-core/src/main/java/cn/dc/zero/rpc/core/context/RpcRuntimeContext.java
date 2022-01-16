package cn.dc.zero.rpc.core.context;

import cn.dc.zero.rpc.core.cache.ReflectCache;
import cn.dc.zero.rpc.core.registry.RegistryFactory;
import cn.dc.zero.rpc.core.server.ServerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author ：d3137
 * @date ：Created in 2021/11/25 16:55
 * @description：
 * @version:
 */
public class RpcRuntimeContext {


    private final static Logger LOGGER                    = LoggerFactory
            .getLogger(RpcRuntimeContext.class);

    /**
     * 上下文信息，例如instancekey，本机ip等信息
     */
    private final static ConcurrentMap CONTEXT                   = new ConcurrentHashMap();

    /**
     * 当前进程Id
     */
    public static final String                                PID                       = ManagementFactory
            .getRuntimeMXBean()
            .getName().split("@")[0];

    /**
     * 当前应用启动时间（用这个类加载时间为准）
     */
    public static final long                                  START_TIME                = System.currentTimeMillis();




    static {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Welcome! Loading IOT RPC Framework : {}, PID is:{}", "0.0.1", PID);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                if (LOGGER.isWarnEnabled()) {
                    LOGGER.warn("IOT-RPC Framework catch JVM shutdown event, Run shutdown hook now.");
                }
                destroy(false);
            }
        }, "IOT-RPC-ShutdownHook"));
    }

    /**
     * 初始化一些上下文
     */
    private static void initContext() {
        //暂时不需要
    }

    /**
     * 主动销毁全部SOFA RPC运行相关环境
     */
    public static void destroy() {
        destroy(true);
    }

    /**
     * 销毁方法
     *
     * @param active 是否主动销毁
     */
    private static void destroy(boolean active) {

        RegistryFactory.destroy();
        // 关闭端口
        ServerFactory.destroy();

        ReflectCache.clearAll();
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn("IOT-Rpc has been release all resources {}...",
                    active ? "actively " : "");
        }
    }

}
