package cn.dc.zero.rpc.remote.netty.message;




import cn.dc.zero.rpc.core.remote.ResponseFuture;
import cn.dc.zero.rpc.core.remote.RpcRequest;
import cn.dc.zero.rpc.core.remote.RpcResponse;
import cn.dc.zero.rpc.core.remote.RpcResponseCallback;

import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @Author:     DC
 * @Description:  
 * @Date:    2022/1/16 20:32
 * @Version:    1.0
 */
public class NettyResponseFuture<V> extends ResponseFuture<V> {

    protected RpcRequest request;

    protected RpcResponse rpcResponse;

    public NettyResponseFuture(RpcRequest request, int timeout) {
        super(timeout);
        this.request = request;
    }

    @Override
    protected ResponseFuture addCallbacks(List<RpcResponseCallback> rpcResponseCallbacks) {
        return null;
    }

    @Override
    protected ResponseFuture addCallback(RpcResponseCallback rpcResponseCallback) {
        return null;
    }

    @Override
    protected V getNow() throws ExecutionException {
        return null;
    }

    public RpcResponse getRpcResponse(int timeout, TimeUnit unit) throws CancellationException,
            TimeoutException, InterruptedException, ExecutionException {
        long realTimeOut = unit.toMillis(timeout);
        long remainTime = realTimeOut - (sendTime - genTime); // 剩余时间
        if (remainTime <= 0) { // 没有剩余时间不等待
            if (isDone()) { // 直接看是否已经返回
                return getNowResponse();
            }
        } else { // 等待剩余时间
            if (await(remainTime, TimeUnit.MILLISECONDS)) {
                return getNowResponse();
            }
        }
        this.setDoneTime();
        throw new TimeoutException();
    }

    protected RpcResponse getNowResponse() throws ExecutionException, CancellationException {
        if (cause != null) {
            if (cause == CANCELLATION_CAUSE) {
                throw (CancellationException) cause;
            } else {
                throw new ExecutionException(cause);
            }
        } else if (result instanceof RpcResponse) {
            return (RpcResponse) result;
        } else {
            throw new ExecutionException(new IllegalArgumentException("Not sofa response!"));
        }
    }
}
