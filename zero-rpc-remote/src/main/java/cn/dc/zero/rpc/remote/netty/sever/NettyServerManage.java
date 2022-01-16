package cn.dc.zero.rpc.remote.netty.sever;


import cn.dc.zero.rpc.core.base.Lifecycle;
import cn.dc.zero.rpc.core.invoker.Invoker;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ：d3137
 * @date ：Created in 2021/11/23 10:11
 * @description：服务端代理
 * @version:
 */
public class NettyServerManage implements Lifecycle {

    protected static  Map<String, Invoker> invokerMap      = new ConcurrentHashMap<String, Invoker>();

    public  Map<String, Invoker> getInvokerMap(){
        return invokerMap;
    }

    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }
}
