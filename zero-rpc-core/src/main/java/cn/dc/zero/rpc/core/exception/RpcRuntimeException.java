package cn.dc.zero.rpc.core.exception;

/**
 * @author ：d3137
 * @date ：Created in 2021/11/25 9:54
 * @description：
 * @version:
 */
public class RpcRuntimeException extends  RuntimeException{

    protected RpcRuntimeException() {

    }

    public RpcRuntimeException(String message) {
        super(message);
    }

    public RpcRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcRuntimeException(Throwable cause) {
        super(cause);
    }
}
