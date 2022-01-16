package cn.dc.zero.rpc.core.util;

import java.io.Closeable;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author ：d3137
 * @date ：Created in 2021/10/26 9:46
 * @description：IO工具类
 * @version:
 */
public class IOUtils {

    /**
     * 静默关闭
     *
     * @param closeable 可关闭的
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ignore) {
                // NOPMD
            }
        }
    }

    /**
     * 静默关闭 for jdk6
     *
     * @param closeable 可关闭的
     */
    public static void closeQuietly(ServerSocket closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ignore) {
                // NOPMD
            }
        }
    }

    /**
     * 静默关闭 for jdk6
     *
     * @param closeable 可关闭的
     */
    public static void closeQuietly(Socket closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ignore) {
                // NOPMD
            }
        }
    }
}
