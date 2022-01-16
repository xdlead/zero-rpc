package cn.dc.zero.rpc.core.ext;

public interface ExtensionLoaderListener<T> {
    /**
     * 当扩展点加载时，触发的事件
     *
     * @param extensionClass 扩展点类对象
     */
    void onLoad(ExtensionClass<T> extensionClass);
}
