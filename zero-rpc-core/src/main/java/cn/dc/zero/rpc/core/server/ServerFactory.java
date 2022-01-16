package cn.dc.zero.rpc.core.server;

import cn.dc.zero.rpc.core.config.ServerConfig;
import cn.dc.zero.rpc.core.exception.RpcRuntimeException;
import cn.dc.zero.rpc.core.ext.ExtensionClass;
import cn.dc.zero.rpc.core.ext.ExtensionLoaderFactory;
import cn.dc.zero.rpc.core.log.LogCodes;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerFactory  {

    private final static ConcurrentHashMap<String, Server>  SERVER_MAP = new ConcurrentHashMap<>();

    /**
     * 初始化Server实例
     *
     * @param serverConfig 服务端配置
     * @return Server
     */
    public synchronized static Server getServer(ServerConfig serverConfig) {
        try {
            Server server = SERVER_MAP.get(Integer.toString(serverConfig.getPort()));
            if (server == null) {

                ExtensionClass<Server> ext = ExtensionLoaderFactory.getExtensionLoader(Server.class)
                        .getExtensionClass(serverConfig.getServerType());
                if (ext == null) {
                    //异常捕获
                    throw new RpcRuntimeException(LogCodes.getLog(LogCodes.ERROR_LOAD_SERVER,serverConfig.getServerType(),"not found server "));
                }
                server = ext.getExtInstance();
                server.init(serverConfig);
                SERVER_MAP.put(serverConfig.getPort() + "", server);
            }
            return server;
        } catch (RpcRuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new RpcRuntimeException(LogCodes.getLog(LogCodes.ERROR_LOAD_SERVER,serverConfig.getServerType(),"the process of initial server is error"));
        }
    }



    public static void destroy() {
        Iterator<Map.Entry<String, Server>> entries = SERVER_MAP.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, Server> entry = entries.next();
            Server server = entry.getValue();
            server.stop();
        }

    }
}
