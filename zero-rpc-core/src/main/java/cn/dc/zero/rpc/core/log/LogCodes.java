package cn.dc.zero.rpc.core.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: DC
 * @Description:
 * @Date: 2022/1/2 17:04
 * @Version: 1.0
 */
public class LogCodes {

    private static Logger logger = LoggerFactory.getLogger(LogCodes.class);

    protected final static String              LOG                                        = "RPC-%s: %s ";

    protected static final Map<String, String> LOG_CODES                                  = new ConcurrentHashMap<String, String>();

    //启动错误
    //服务端启动错误
    public static final String ERROR_PROVIDER_CONFIG   = "01001001";

    public static final String ERROR_GET_PROXY_CLASS = "01001002";

    public static final String ERROR_BUILD_PROVIDER_CONFIG = "01001003";
    public static final String ERROR_BUILD_PROVIDER_REGISTRY = "01001004";

    //消费端启动错误
    public static final String ERROR_CONSUMER_CONFIG   = "01002001";

    public static final String ERROR_BUILD_CONSUMER_CONFIG   = "01002003";

    public static final String ERROR_BUILD_CONSUMER_REGISTRY = "01002004";

    //其他启动错误
    public static String CODE_DOES_NOT_EXIST   = "03001001";

    public static final String  ERROR_LOAD_PROXY = "03001002";

    public static final String  ERROR_LOAD_FILTER_CHAIN = "03001003";

    public static final String  ERROR_LOAD_SERIALIZER = "03001003";

    public static final String  ERROR_LOAD_REGISTRY = "03001003";

    public static final String  ERROR_LOAD_SERVER = "03001003";

    //运行错误
    //消费端运行错误
    public static final String PROVIDER_NOT_AVAILABLE = "02001001";

    public static final String REMOTE_NOT_AVAILABLE = "02001002";

    public static final String LOG_FORMAT_ERROR = "LogCode.getLog format error,code=";



    public static String getLog(String code, Object... messages) {
        String message = LOG_CODES.get(code);

        if (message == null) {
            logger.error(CODE_DOES_NOT_EXIST + code);
            return CODE_DOES_NOT_EXIST + code;
        }
        try {
            return String.format(LOG, code, MessageFormat.format(message, messages));
        } catch (Throwable e) {
            logger.error(LOG_FORMAT_ERROR, e);
        }
        return LOG_FORMAT_ERROR + code;
    }
}
