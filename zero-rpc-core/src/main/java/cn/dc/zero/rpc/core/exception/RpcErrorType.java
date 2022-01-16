package cn.dc.zero.rpc.core.exception;

/**
 * @author ：d3137
 * @date ：Created in 2021/11/25 9:39
 * @description：RPC错误类型  服务端 1开头  客户端 2开头
 * @version:
 */
public class RpcErrorType {
    public static final int UNKNOWN            = 0;

    /**
     * 客户端超时异常
     */
    public static final int CLIENT_TIMEOUT           = 200;
}
