package cn.dc.zero.rpc.core.common;



import cn.dc.zero.rpc.core.util.ClassLoaderUtils;
import cn.dc.zero.rpc.core.util.CommonUtils;
import cn.dc.zero.rpc.core.util.CompatibleTypeUtils;
import cn.dc.zero.rpc.core.util.FileUtils;
import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author:     DC
 * @Description:  RPC配置文件加载
 * @Date:    2022/1/16 20:42
 * @Version:    1.0
 */
public class RpcConfigs {
    /**
     * 全部配置
     */
    private final static ConcurrentMap<String, Object> CFG          = new ConcurrentHashMap<String, Object>();
    /**
     * 配置变化监听器
     */
    private final static ConcurrentMap<String, List<RpcConfigListener>> CFG_LISTENER = new ConcurrentHashMap<String,
            List<RpcConfigListener>>();

    static {
        init(); // 加载配置文件
    }

    private static void init() {
        try {
            // 加载默认配置文件
            String json = FileUtils.file2String(RpcConfigs.class, "default-zero-rpc-config.json", "UTF-8");
            Map map = JSON.parseObject(json, Map.class);
            CFG.putAll(map);

            // 加载自定义配置文件
            loadCustom("zero-rpc/rpc-config.json");
            loadCustom("META-INF/zero-rpc/rpc-config.json");

            // 加载系统参数
            CFG.putAll(new HashMap(System.getProperties()));
        } catch (Exception e) {
            //异常抛出日志
            e.printStackTrace();
        }
    }

    /**
     * 加载自定义配置文件
     *
     * @param fileName 文件名
     * @throws IOException 加载异常
     */
    private static void loadCustom(String fileName) throws IOException {
        ClassLoader classLoader = ClassLoaderUtils.getClassLoader(RpcConfigs.class);
        Enumeration<URL> urls = classLoader != null ? classLoader.getResources(fileName)
                : ClassLoader.getSystemResources(fileName);
        if (urls != null) { // 可能存在多个文件
            List<CfgFile> allFile = new ArrayList<CfgFile>();
            while (urls.hasMoreElements()) {
                // 读取每一个文件
                URL url = urls.nextElement();
                InputStreamReader input = null;
                BufferedReader reader = null;
                try {
                    input = new InputStreamReader(url.openStream(), "utf-8");
                    reader = new BufferedReader(input);
                    StringBuilder context = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        context.append(line).append("\n");
                    }
                    Map map = JSON.parseObject(context.toString(), Map.class);
                    Integer order = (Integer) map.get(RpcOptions.RPC_CFG_ORDER);
                    allFile.add(new CfgFile(url, order == null ? 0 : order, map));
                } finally {
                    if (reader != null) {
                        reader.close();
                    }
                    if (input != null) {
                        input.close();
                    }
                }
            }
            Collections.sort(allFile, new OrderedComparator<CfgFile>()); // 从小到大排下序
            for (CfgFile file : allFile) {
                CFG.putAll(file.getMap()); // 顺序加载，越大越后加载
            }
        }
    }

    /**
     * Put value.
     *
     * @param key      the key
     * @param newValue the new value
     */
    public static void putValue(String key, Object newValue) {
        Object oldValue = CFG.get(key);
        if (changed(oldValue, newValue)) {
            CFG.put(key, newValue);
            List<RpcConfigListener> rpcConfigListeners = CFG_LISTENER.get(key);
            if (CommonUtils.isNotEmpty(rpcConfigListeners)) {
                for (RpcConfigListener rpcConfigListener : rpcConfigListeners) {
                    rpcConfigListener.onChange(oldValue, newValue);
                }
            }
        }
    }

    /**
     * Remove value
     *
     * @param key Key
     */

    synchronized static void removeValue(String key) {
        Object oldValue = CFG.get(key);
        if (oldValue != null) {
            CFG.remove(key);
            List<RpcConfigListener> rpcConfigListeners = CFG_LISTENER.get(key);
            if (CommonUtils.isNotEmpty(rpcConfigListeners)) {
                for (RpcConfigListener rpcConfigListener : rpcConfigListeners) {
                    rpcConfigListener.onChange(oldValue, null);
                }
            }
        }
    }

    /**
     * Gets boolean value.
     *
     * @param primaryKey the primary key
     * @return the boolean value
     */
    public static boolean getBooleanValue(String primaryKey) {
        Object val = CFG.get(primaryKey);
        if (val == null) {
            return false;
        } else {
            return Boolean.valueOf(val.toString());
        }
    }

    /**
     * Gets boolean value.
     *
     * @param primaryKey   the primary key
     * @param secondaryKey the secondary key
     * @return the boolean value
     */
    public static boolean getBooleanValue(String primaryKey, String secondaryKey) {
        Object val = CFG.get(primaryKey);
        if (val == null) {
            val = CFG.get(secondaryKey);
            if (val == null) {
                //异常抛出日志
                return false;
            }
        }
        return Boolean.valueOf(val.toString());
    }

    /**
     * Gets int value.
     *
     * @param primaryKey the primary key
     * @return the int value
     */
    public static int getIntValue(String primaryKey) {
        Object val = CFG.get(primaryKey);
        if (val == null) {
            //异常抛出日志
            return 0;
        } else {
            return Integer.parseInt(val.toString());
        }
    }

    /**
     * Gets int value.
     *
     * @param primaryKey   the primary key
     * @param secondaryKey the secondary key
     * @return the int value
     */
    public static int getIntValue(String primaryKey, String secondaryKey) {
        Object val = CFG.get(primaryKey);
        if (val == null) {
            val = CFG.get(secondaryKey);
            if (val == null) {
                //异常抛出日志
                return 0;
            }
        }
        return Integer.parseInt(val.toString());
    }

    /**
     * Gets enum value.
     *
     * @param <T>        the type parameter
     * @param primaryKey the primary key
     * @param enumClazz  the enum clazz
     * @return the enum value
     */
    public static <T extends Enum<T>> T getEnumValue(String primaryKey, Class<T> enumClazz) {
        String val = (String) CFG.get(primaryKey);
        if (val == null) {
            //异常抛出日志
            return null;
        } else {
            return Enum.valueOf(enumClazz, val);
        }
    }

    /**
     * Gets string value.
     *
     * @param primaryKey the primary key
     * @return the string value
     */
    public static String getStringValue(String primaryKey) {
        String val = (String) CFG.get(primaryKey);
        if (val == null) {
            //异常抛出日志
            return null;
        } else {
            return val;
        }
    }

    /**
     * Gets string value.
     *
     * @param primaryKey   the primary key
     * @param secondaryKey the secondary key
     * @return the string value
     */
    public static String getStringValue(String primaryKey, String secondaryKey) {
        String val = (String) CFG.get(primaryKey);
        if (val == null) {
            val = (String) CFG.get(secondaryKey);
            if (val == null) {
                //异常抛出
                return null;
            } else {
                return val;
            }
        } else {
            return val;
        }
    }

    /**
     * Gets list value.
     *
     * @param primaryKey the primary key
     * @return the list value
     */
    public static List getListValue(String primaryKey) {
        List val = (List) CFG.get(primaryKey);
        if (val == null) {
            //异常抛出日志
            return null;
        } else {
            return val;
        }
    }

    /**
     * Gets or default value.
     *
     * @param <T>          the type parameter
     * @param primaryKey   the primary key
     * @param defaultValue the default value
     * @return the or default value
     */
    public static <T> T getOrDefaultValue(String primaryKey, T defaultValue) {
        Object val = CFG.get(primaryKey);
        if (val == null) {
            return defaultValue;
        } else {
            Class<?> type = defaultValue == null ? null : defaultValue.getClass();
            return (T) CompatibleTypeUtils.convert(val, type);
        }
    }

    /**
     * 订阅配置变化
     *
     * @param key      关键字
     * @param listener 配置监听器
     * @see
     */
    public static synchronized void subscribe(String key, RpcConfigListener listener) {
        List<RpcConfigListener> listeners = CFG_LISTENER.get(key);
        if (listeners == null) {
            listeners = new ArrayList<RpcConfigListener>();
            CFG_LISTENER.put(key, listeners);
        }
        listeners.add(listener);
    }

    /**
     * 取消订阅配置变化
     *
     * @param key      关键字
     * @param listener 配置监听器
     * @see
     */
    public static synchronized void unSubscribe(String key, RpcConfigListener listener) {
        List<RpcConfigListener> listeners = CFG_LISTENER.get(key);
        if (listeners != null) {
            listeners.remove(listener);
            if (listeners.size() == 0) {
                CFG_LISTENER.remove(key);
            }
        }
    }

    /**
     * 值是否发生变化
     *
     * @param oldObj 旧值
     * @param newObj 新值
     * @return 是否变化 boolean
     */
    protected static boolean changed(Object oldObj, Object newObj) {
        return oldObj == null ?
                newObj != null :
                !oldObj.equals(newObj);
    }

    /**
     * 用于排序的一个类
     */
    private static class CfgFile implements Sortable {
        private final URL url;
        private final int order;
        private final Map map;

        /**
         * Instantiates a new Cfg file.
         *
         * @param url   the url
         * @param order the order
         * @param map   the map
         */
        public CfgFile(URL url, int order, Map map) {
            this.url = url;
            this.order = order;
            this.map = map;
        }

        /**
         * Gets url.
         *
         * @return the url
         */
        public URL getUrl() {
            return url;
        }

        @Override
        public int getOrder() {
            return order;
        }

        /**
         * Gets map.
         *
         * @return the map
         */
        public Map getMap() {
            return map;
        }
    }

    /**
     * 配置变更会拿到通知
     *
     * @param <T> the type parameter
     */
    public interface RpcConfigListener<T> {
        /**
         * On change.
         *
         * @param oldValue the old value
         * @param newValue the new value
         */
        public void onChange(T oldValue, T newValue);
    }
}
