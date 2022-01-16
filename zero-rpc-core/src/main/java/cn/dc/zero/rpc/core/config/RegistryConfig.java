package cn.dc.zero.rpc.core.config;

import java.io.Serializable;

/**
 *
 */
public class RegistryConfig implements Serializable {

    private String address;

    private String protocol;

    private int connectTimeout;

    private boolean register;

    private boolean subscribe;

    public String getAddress() {
        return address;
    }

    public RegistryConfig setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getProtocol() {
        return protocol;
    }

    public RegistryConfig setProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public RegistryConfig setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public boolean isRegister() {
        return register;
    }

    public RegistryConfig setRegister(boolean register) {
        this.register = register;
        return this;
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    public RegistryConfig setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
        return this;
    }
}
