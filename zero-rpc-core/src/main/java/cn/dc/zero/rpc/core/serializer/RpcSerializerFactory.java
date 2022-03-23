package cn.dc.zero.rpc.core.serializer;



import cn.dc.zero.rpc.core.common.struct.TwoWayMap;
import cn.dc.zero.rpc.core.exception.RpcRuntimeException;
import cn.dc.zero.rpc.core.ext.ExtensionClass;
import cn.dc.zero.rpc.core.ext.ExtensionLoader;
import cn.dc.zero.rpc.core.ext.ExtensionLoaderFactory;
import cn.dc.zero.rpc.core.ext.ExtensionLoaderListener;
import cn.dc.zero.rpc.core.log.LogCodes;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author ：d3137
 * @date ：Created in 2021/11/26 10:04
 * @description：序列化工厂
 * @version:
 */
public class RpcSerializerFactory {

    private static final ConcurrentHashMap<String,RpcSerializer> All_SERIALIZER = new ConcurrentHashMap<>();



    /**
     * 除了托管给扩展加载器的工厂模式（保留alias：实例）外<br>
     * 还需要额外保留编码和实例的映射：{编码：序列化器}
     */
    private final static ConcurrentMap<Byte, RpcSerializer> TYPE_SERIALIZER_MAP = new ConcurrentHashMap<Byte, RpcSerializer>();

    /**
     * 除了托管给扩展加载器的工厂模式（保留alias：实例）外，还需要额外保留编码和实例的映射：{别名：编码}
     */
    private final static TwoWayMap<String, Byte> TYPE_CODE_MAP       = new TwoWayMap<String, Byte>();

    /**
     * 扩展加载器
     */
    private final static ExtensionLoader<RpcSerializer> EXTENSION_LOADER    = buildLoader();

    private static ExtensionLoader<RpcSerializer> buildLoader() {
        ExtensionLoader<RpcSerializer> extensionLoader = ExtensionLoaderFactory.getExtensionLoader(RpcSerializer.class);
        extensionLoader.addListener(new ExtensionLoaderListener<RpcSerializer>() {
            @Override
            public void onLoad(ExtensionClass<RpcSerializer> extensionClass) {
                // 除了保留 tag：Serializer外， 需要保留 code：Serializer
                TYPE_SERIALIZER_MAP.put(extensionClass.getCode(), extensionClass.getExtInstance());
                TYPE_CODE_MAP.put(extensionClass.getAlias(), extensionClass.getCode());
            }
        });
        return extensionLoader;

    }

    /**
     * 按序列化名称返回协议对象
     *
     * @param alias 序列化名称
     * @return 序列化器
     */
    public static RpcSerializer getSerializer(String alias) {
        // 工厂模式  托管给ExtensionLoader
        return EXTENSION_LOADER.getExtension(alias);
    }

    /**
     * 按序列化名称返回协议对象
     *
     * @param type 序列号编码
     * @return 序列化器
     */
    public static RpcSerializer getSerializer(byte type) {
        RpcSerializer serializer = TYPE_SERIALIZER_MAP.get(type);
        if (serializer == null) {
            throw new RpcRuntimeException(LogCodes.getLog(LogCodes.ERROR_LOAD_SERIALIZER, type));
        }
        return serializer;
    }

    /**
     * 通过别名获取Code
     *
     * @param serializer 序列化的名字
     * @return 序列化编码
     */
    public static Byte getCodeByAlias(String serializer) {
        return TYPE_CODE_MAP.get(serializer);
    }

    /**
     * 通过Code获取别名
     *
     * @param code 序列化的Code
     * @return 序列化别名
     */
    public static String getAliasByCode(byte code) {
        return TYPE_CODE_MAP.getKey(code);
    }
}
