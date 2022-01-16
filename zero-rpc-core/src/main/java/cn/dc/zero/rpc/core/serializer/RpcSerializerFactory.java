package cn.dc.zero.rpc.core.serializer;



import cn.dc.zero.rpc.core.exception.RpcRuntimeException;
import cn.dc.zero.rpc.core.ext.ExtensionClass;
import cn.dc.zero.rpc.core.ext.ExtensionLoaderFactory;
import cn.dc.zero.rpc.core.log.LogCodes;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ：d3137
 * @date ：Created in 2021/11/26 10:04
 * @description：序列化工厂
 * @version:
 */
public class RpcSerializerFactory {

    private static final ConcurrentHashMap<String,RpcSerializer> All_SERIALIZER = new ConcurrentHashMap<>();

    public static synchronized RpcSerializer getSerializer(String serializer) {

        RpcSerializer rpcSerializer ;
        try {
            rpcSerializer = All_SERIALIZER.get(serializer);
            if (rpcSerializer == null) {
                ExtensionClass<RpcSerializer> ext = ExtensionLoaderFactory.getExtensionLoader(RpcSerializer.class)
                        .getExtensionClass(serializer);
                if (ext == null) {
                    throw new RpcRuntimeException(LogCodes.getLog(LogCodes.ERROR_LOAD_SERIALIZER,serializer,"not found serializer "));
                }
                rpcSerializer = ext.getExtInstance(new Class[] {  }, new Object[] {  });
                All_SERIALIZER.put(serializer, rpcSerializer);
            }
            return rpcSerializer;
        } catch (RpcRuntimeException e){
            throw e;
        }catch (Throwable e) {
            throw new RpcRuntimeException(LogCodes.getLog(LogCodes.ERROR_LOAD_SERIALIZER,serializer,"the process of initial serializer is error"));
        }
    }
}
