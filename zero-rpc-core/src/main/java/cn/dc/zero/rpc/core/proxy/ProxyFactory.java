package cn.dc.zero.rpc.core.proxy;


import cn.dc.zero.rpc.core.exception.RpcRuntimeException;
import cn.dc.zero.rpc.core.ext.ExtensionClass;
import cn.dc.zero.rpc.core.ext.ExtensionLoaderFactory;
import cn.dc.zero.rpc.core.invoker.Invoker;
import cn.dc.zero.rpc.core.log.LogCodes;

/**
 * @author ：d3137
 * @date ：Created in 2021/10/27 19:36
 * @description：代理工厂
 * @version:
 */
public final class ProxyFactory {

    /**
     * 构建代理类实例
     *
     * @param proxyType    代理类型
     * @param clazz        原始类
     * @param proxyInvoker 代码执行的Invoker
     * @param <T>          类型
     * @return 代理类实例
     * @throws Exception
     */
    public static <T> T buildProxy(String proxyType, Class<T> clazz, Invoker proxyInvoker) {
        try {
            ExtensionClass<Proxy> ext = ExtensionLoaderFactory.getExtensionLoader(Proxy.class)
                    .getExtensionClass(proxyType);
            if (ext == null) {
                //exception catch
                throw new RpcRuntimeException(LogCodes.getLog(LogCodes.ERROR_LOAD_PROXY,proxyType,"extensionClass must be exits"));
            }
            Proxy proxy = ext.getExtInstance();
            return proxy.getProxy(clazz, proxyInvoker);
        } catch (RpcRuntimeException e){
            throw  e;
        }catch (Throwable e) {
            //exception catch
            throw new RpcRuntimeException(LogCodes.getLog(LogCodes.ERROR_LOAD_PROXY,proxyType,"error create proxy"));
        }
    }


}
