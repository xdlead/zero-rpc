package cn.dc.zero.rpc.core.remote;

import java.util.List;
import java.util.concurrent.*;

/**
 * @Author:     DC
 * @Description:  RPC调用FUTURE
 * @Date:    2021/12/30 23:05
 * @Version:    1.0
 */
public abstract class ResponseFuture<V> implements  Future<V> {

    protected static final CancellationException CANCELLATION_CAUSE = new CancellationException();

    protected volatile Object    result;

    protected volatile Throwable                 cause;

    protected volatile boolean                 done;

    protected final int                          timeout;

    /**
     * Future生成时间
     */
    protected final long                         genTime            = System.currentTimeMillis();
    /**
     * Future已发送时间
     */
    protected volatile long                      sendTime;
    /**
     * Future完成的时间
     */
    protected volatile long                      doneTime;

    /**
     *
     */
    private short waiters;

    protected ResponseFuture(int timeout) {
        this.timeout = timeout;
    }

    protected Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public long getDoneTime() {
        return doneTime;
    }

    public void setDoneTime() {
        this.doneTime = System.currentTimeMillis();
    }

    /**
     * 增加多个响应监听器
     *
     * @return 对象本身
     */
    protected abstract  ResponseFuture addCallbacks(List<RpcResponseCallback> rpcResponseCallbacks);

    /**
     * 增加单个响应监听器
     *
     * @return 对象本身
     */
    protected abstract ResponseFuture addCallback(RpcResponseCallback rpcResponseCallback);

    @Override
    public V get() throws InterruptedException, ExecutionException {
        try {
            return get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            throw new ExecutionException(e);
        }
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        long realTimeOut = unit.toMillis(timeout);
        long remainTime = realTimeOut - (sendTime - genTime); // 剩余时间
        if (remainTime <= 0) { // 没有剩余时间不等待
            if (isDone()) { // 直接看是否已经返回
                return getNow();
            }
        } else { // 等待剩余时间
            if (await(remainTime, TimeUnit.MILLISECONDS)) {
                return getNow();
            }
        }
        this.setDoneTime();
        throw new TimeoutException();
    }

    protected boolean await(long timeout, TimeUnit unit)
            throws InterruptedException {
        return await0(unit.toNanos(timeout), true);
    }

    private boolean await0(long timeoutNanos, boolean interruptAble) throws InterruptedException {
        if (isDone()) {
            return true;
        }
        if (timeoutNanos <= 0) {
            return isDone();
        }
        if (interruptAble && Thread.interrupted()) {
            throw new InterruptedException(toString());
        }
        long startTime = System.nanoTime();
        long waitTime = timeoutNanos;
        boolean interrupted = false;
        try {
            synchronized (this) {
                if (isDone()) {
                    return true;
                }
                incWaiters();
                try {
                    for (;;) {
                        try {
                            wait(waitTime / 1000000, (int) (waitTime % 1000000));
                        } catch (InterruptedException e) {
                            if (interruptAble) {
                                throw e;
                            } else {
                                interrupted = true;
                            }
                        }

                        if (isDone()) {
                            return true;
                        } else {
                            waitTime = timeoutNanos - (System.nanoTime() - startTime);
                            if (waitTime <= 0) {
                                return isDone();
                            }
                        }
                    }
                } finally {
                    decWaiters();
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    protected abstract V getNow() throws ExecutionException;

    private void incWaiters() {
        if (waiters == Short.MAX_VALUE) {
            throw new IllegalStateException("too many waiters: " + this);
        }
        waiters++;
    }

    private void decWaiters() {
        waiters--;
    }

    /**
     * 设置正常返回结果
     *
     * @param result 正常返回值
     */
    public void setSuccess(V result) {

        if (setSuccess0(result)) {
            return;
        }
        throw new IllegalStateException("complete already: " + this);
    }

    protected boolean setSuccess0(V result) {
        if (isDone()) {
            return false;
        }
        synchronized (this) {
            // Allow only once.
            if (isDone()) {
                return false;
            }
            if (this.result == null) {
                this.result = result;
            }
            this.done = true;
            this.setDoneTime();
            if (hasWaiters()) {
                notifyAll();
            }
        }
        return true;
    }

    private boolean hasWaiters() {
        return waiters > 0;
    }

    @Override
    public boolean isCancelled() {
        return cause == CANCELLATION_CAUSE;
    }

    @Override
    public boolean isDone() {
        return done;
    }


    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        boolean res = this.cancel0(mayInterruptIfRunning);
        return res;
    }

    private boolean cancel0(boolean mayInterruptIfRunning) {
        if (isDone()) {
            return false;
        }
        synchronized (this) {
            if (isDone()) {
                return false;
            }
            this.cause = CANCELLATION_CAUSE;
            this.setDoneTime();
            if (hasWaiters()) {
                notifyAll();
            }
        }
        return true;
    }
}
