package cn.dc.zero.rpc.core.remote;

import java.lang.ref.SoftReference;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ：d3137
 * @date ：Created in 2021/11/10 16:27
 * @description：
 * @version:
 */
public class Url {

    private String originUrl;
    private String ip;
    private int port;
    private String uniqueKey;
    private int connectTimeout;
    private byte protocol;
    private byte version;
    private int connNum;
    private boolean connWarmup;
    private Properties properties;
    public static ConcurrentHashMap<String, SoftReference<Url>> parsedUrls = new ConcurrentHashMap();
    public static volatile boolean isCollected = false;

    protected Url(String originUrl) {
        this.version = 1;
        this.originUrl = originUrl;
    }

    public Url(String ip, int port) {
        this(ip + ':' + port);
        this.ip = ip;
        this.port = port;
        this.uniqueKey = this.originUrl;
    }

    public Url(String originUrl, String ip, int port) {
        this(originUrl);
        this.ip = ip;
        this.port = port;
        this.uniqueKey = ip + ':' + port;
    }

    public Url(String originUrl, String ip, int port, Properties properties) {
        this(originUrl, ip, port);
        this.properties = properties;
    }

    public Url(String originUrl, String ip, int port, String uniqueKey, Properties properties) {
        this(originUrl, ip, port);
        this.uniqueKey = uniqueKey;
        this.properties = properties;
    }

    public String getProperty(String key) {
        return this.properties == null ? null : this.properties.getProperty(key);
    }

    public String getOriginUrl() {
        return this.originUrl;
    }

    public String getIp() {
        return this.ip;
    }

    public int getPort() {
        return this.port;
    }

    public String getUniqueKey() {
        return this.uniqueKey;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        if (connectTimeout <= 0) {
            throw new IllegalArgumentException("Illegal value of connection number [" + this.connNum + "], must be a positive integer].");
        } else {
            this.connectTimeout = connectTimeout;
        }
    }

    public byte getProtocol() {
        return this.protocol;
    }

    public void setProtocol(byte protocol) {
        this.protocol = protocol;
    }

    public int getConnNum() {
        return this.connNum;
    }

    public void setConnNum(int connNum) {
        if (connNum > 0 && connNum <= 1000000) {
            this.connNum = connNum;
        } else {
            throw new IllegalArgumentException("Illegal value of connection number [" + connNum + "], must be an integer between [" + 1 + ", " + 1000000 + "].");
        }
    }

    public boolean isConnWarmup() {
        return this.connWarmup;
    }

    public void setConnWarmup(boolean connWarmup) {
        this.connWarmup = connWarmup;
    }

    public Properties getProperties() {
        return this.properties;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            Url url = (Url)obj;
            return this.getOriginUrl().equals(url.getOriginUrl());
        }
    }

    public int hashCode() {
        int result = 1;
        result = 31 * result + (this.getOriginUrl() == null ? 0 : this.getOriginUrl().hashCode());
        return result;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Origin url [" + this.originUrl + "], Unique key [" + this.uniqueKey + "].");
        return sb.toString();
    }

    protected void finalize() throws Throwable {
        try {
            isCollected = true;
            parsedUrls.remove(this.getOriginUrl());
        } catch (Exception var2) {
        }

    }

    public byte getVersion() {
        return this.version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }
}
