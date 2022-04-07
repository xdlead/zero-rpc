package cn.dc.zero.rpc.core.client;


import cn.dc.zero.rpc.core.config.ConsumerConfig;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author ：d3137
 * @date ：Created in 2021/10/28 14:19
 * @description：
 * @version:
 */
public class ProviderInfo implements Serializable {

    private String originUrl;

    private ConsumerConfig consumerConfig;

    private String protocol;

    private String host;

    private int port = 8921;

    private String path;

    private String version;

    //权重
    private Integer weight =1;

    private String serializable;

    private final transient ConcurrentMap<String, Object> dynamicAttrs     = new ConcurrentHashMap<String, Object>();

    private final ConcurrentMap<String, String>           staticAttrs      = new ConcurrentHashMap<String, String>();

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public ConsumerConfig getConsumerConfig() {
        return consumerConfig;
    }

    public void setConsumerConfig(ConsumerConfig consumerConfig) {
        this.consumerConfig = consumerConfig;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getOriginUrl() {
        return originUrl;
    }

    public void setOriginUrl(String originUrl) {
        this.originUrl = originUrl;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getSerializable() {
        return serializable;
    }

    public void setSerializable(String serializable) {
        this.serializable = serializable;
    }

    public ConcurrentMap<String, Object> getDynamicAttrs() {
        return dynamicAttrs;
    }

    public ConcurrentMap<String, String> getStaticAttrs() {
        return staticAttrs;
    }

    public String getAttr(String key) {
        String val = (String) dynamicAttrs.get(key);
        return val == null ? staticAttrs.get(key) : val;
    }

    @Override
    public String toString() {
        return originUrl == null ? host + ":" + port : originUrl;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof  ProviderInfo)){
            return false;
        }
        ProviderInfo tmp = (ProviderInfo) o;
        if(!(host.equals(tmp.getHost()))){
            return false;
        }

        if(!(port == tmp.getPort())){
            return false;
        }
        if (protocol != null ? !protocol.equals(tmp.protocol) : tmp.protocol != null) {
            return false;
        }
        if (serializable != null ? !serializable.equals(tmp.serializable) : tmp.serializable != null) {
            return false;
        }
        return true;
    }
    @Override
    public int hashCode() {
        int result =0;

        result = 31 * result + (host != null ? host.hashCode(): 0);
        result = 31 * result + (protocol != null ? protocol.hashCode(): 0);
        result = 31 * result + (serializable != null ? serializable.hashCode() : 0);
        result = 31 * result + port;
        return result;
    }



}
