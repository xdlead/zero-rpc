package cn.dc.zero.rpc.core.exception;

/**
 * @author ：d3137
 * @date ：Created in 2021/11/25 9:56
 * @description：
 * @version:
 */
public class RpcTimeOutException extends  RpcException{

    private static final long serialVersionUID = 1L;

    public RpcTimeOutException(String desc) {
        super(RpcErrorType.CLIENT_TIMEOUT, desc);
    }

    public RpcTimeOutException(Throwable t) {
        super(RpcErrorType.CLIENT_TIMEOUT, t);
    }

    public RpcTimeOutException(String desc, Throwable t) {
        super(RpcErrorType.CLIENT_TIMEOUT, desc, t);
    }
}
