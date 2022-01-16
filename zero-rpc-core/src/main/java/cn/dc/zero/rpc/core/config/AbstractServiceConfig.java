package cn.dc.zero.rpc.core.config;



import cn.dc.zero.rpc.core.filter.Filter;
import cn.dc.zero.rpc.core.util.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @Author:     DC
 * @Description:  抽象配置类
 * @Date:    2022/1/13 23:24
 * @Version:    1.0
 */
public abstract class AbstractServiceConfig<S extends AbstractServiceConfig> implements Serializable {
    /**
     * 过滤器实体类集合
     */
    protected transient List<Filter> filterRef;

    /**
     * 过滤器名称集合，通过extension加载器初始化
     */
    protected transient List<String> filterAlias;

    /**
     * 接口标识
     */
    protected String interfaceId;

    /**
     * 服务唯一标识
     */
    protected String uniqueId ;


    protected transient volatile Class    proxyClass;

    /**
     * 注册器配置集合
     */
    protected  transient List<RegistryConfig> registryConfigs;

    public List<Filter> getFilterRef() {
        return filterRef;
    }

    public S setFilterRef(List<Filter> filterRef) {
        this.filterRef = filterRef;
        return castThis();
    }

    public List<String> getFilterAlias() {
        return filterAlias;
    }

    public S setFilterAlias(List<String> filterAlias) {
        this.filterAlias = filterAlias;
        return castThis();
    }

    public List<RegistryConfig> getRegistryConfigs() {
        return registryConfigs;
    }

    public S setRegistryConfigs(List<RegistryConfig> registryConfigs) {
        this.registryConfigs = registryConfigs;
        return castThis();
    }

    public String getInterfaceId() {
        if(StringUtils.isNotBlank(interfaceId)){
            return interfaceId;
        }
        return proxyClass.getName();
    }

    public S setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
        return castThis();
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public S setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
        return castThis();
    }


    protected S castThis() {
        return (S) this;
    }

    protected abstract Class<?> getProxyClass();


}
