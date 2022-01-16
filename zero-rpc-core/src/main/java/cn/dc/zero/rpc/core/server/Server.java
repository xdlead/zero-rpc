package cn.dc.zero.rpc.core.server;


import cn.dc.zero.rpc.core.config.ProviderConfig;
import cn.dc.zero.rpc.core.config.ServerConfig;
import cn.dc.zero.rpc.core.ext.Extensible;
import cn.dc.zero.rpc.core.invoker.Invoker;

@Extensible(singleton = false)
public interface Server {
    /**
    * @description: 服务端初始化
    * @param: [serverConfig]
    * @return: void
    * @author: DC
    */
    void init(ServerConfig serverConfig);

    /**
    * @description: 服务端启动
    * @param: []
    * @return: void
    * @author: DC
    */
    void start();

    /**
    * @description: 是否启动
    * @param: []
    * @return: boolean
    * @author: DC
    */
    boolean isStarted();

    /**
    * @description: 
    * @param: []
    * @return: boolean
    * @author: DC
    */
    boolean hasNoEntry();

    /**
    * @description: 停止服务端
    * @param: []
    * @return: void
    * @author: DC
    */
    void stop();

    /**
    * @description: 注册服务代理
    * @param: [providerConfig, instance]
    * @return: void
    * @author: DC
    */
    void registerProcessor(ProviderConfig providerConfig, Invoker instance);

    /**
    * @description: 注销服务代理
    * @param: [providerConfig, closeIfNoEntry]
    * @return: void
    * @author: DC
    */
    void unRegisterProcessor(ProviderConfig providerConfig, boolean closeIfNoEntry);

}
