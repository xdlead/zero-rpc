package cn.dc.zero.rpc.core.exception;

/**
 * @Author:     DC
 * @Description:  RPC异常基类
 * @Date:    2021/12/30 22:39
 * @Version:    1.0
 */
public class RpcException extends RuntimeException{
    /**
     * 异常类型
     */
    protected int errorType = RpcErrorType.UNKNOWN;

    protected RpcException() {

    }

    public RpcException(int errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public RpcException(int errorType, Throwable cause) {
        super(cause);
        this.errorType = errorType;
    }

    public RpcException(int errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }

    public int getErrorType() {
        return errorType;
    }

    @Deprecated
    public RpcException(String message) {
        super(message);
    }

    @Deprecated
    public RpcException(String message, Throwable t) {
        super(message, t);
    }
}
