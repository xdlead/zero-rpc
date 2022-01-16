package cn.dc.zero.rpc.core.remote;

import java.io.Serializable;

public class RpcResponse implements Serializable {
    private Integer requestId;
    private String errorMsg;
    private Object result;

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
