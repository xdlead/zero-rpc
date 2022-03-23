package cn.dc.zero.rpc.remote.http.client;

import cn.dc.zero.rpc.core.common.StringSerializer;
import cn.dc.zero.rpc.core.exception.RpcErrorType;
import cn.dc.zero.rpc.core.exception.RpcException;
import cn.dc.zero.rpc.core.exception.RpcRuntimeException;
import cn.dc.zero.rpc.core.remote.RemoteHandler;
import cn.dc.zero.rpc.core.remote.RemotingConstants;
import cn.dc.zero.rpc.core.remote.RpcResponse;
import cn.dc.zero.rpc.core.util.CommonUtils;
import cn.dc.zero.rpc.core.util.StringUtils;
import cn.dc.zero.rpc.remote.http.helper.HttpRemoteHelper;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.concurrent.Executor;

/**
 * @Author: DC
 * @Description:
 * @Date: 2022/1/23 16:26
 * @Version: 1.0
 */
public class AbstractHttpRemoteHandler implements RemoteHandler {
    @Override
    public void onResponse(Object result) {

    }

    @Override
    public void onException(Throwable e) {

    }

    @Override
    public Executor getExecutor() {
        return null;
    }

    public void receiveHttpResponse(FullHttpResponse msg) {
//        HttpHeaders headers = msg.headers();
//        ByteBuf content = msg.content();
//        HttpByteBuffer data = new HttpByteBuffer(content);
//        try {
//            if (msg.status() == HttpResponseStatus.OK) {
//                // 正常返回
//                final RpcResponse response = new RpcResponse();e
////                String isError = headers.get(    RemotingConstants.HEAD_RESPONSE_ERROR);
//                String isError = "false";
//                if (CommonUtils.isTrue(isError)) {
//                    // 业务异常
//                    String errorMsg = StringSerializer.decode(data.array());
//                    Throwable throwable = new RpcException(RpcErrorType.SERVER_BIZ, errorMsg);
//                    response.setResult(throwable);
//                } else {
//                    // 获取序列化类型
//                    if (data.readableBytes() > 0) {
//                        byte serializeType;
//                        String codeName = headers.get(RemotingConstants.HEAD_SERIALIZE_TYPE);
//                        if (codeName != null) {
//                            serializeType = HttpRemoteHelper.getSerializeTypeByName(codeName);
//                        } else {
//                            // HEAD_SERIALIZE_TYPE 没设置的话 再取 content-type 兜底下
//                            String contentType = StringUtils.toString(headers.get(HttpHeaderNames.CONTENT_TYPE));
//                            serializeType = HttpRemoteHelper.getSerializeTypeByContentType(contentType);
//                        }
//                        response.setSerializeType(serializeType);
//                        content.retain();
//                        response.setData(data);
//                    }
//                }
//                onResponse(response);
//            } else {
//                // 系统异常
//                String errorMsg = StringSerializer.decode(data.array());
//                Throwable throwable = new SofaRpcException(RpcErrorType.SERVER_UNDECLARED_ERROR, errorMsg);
//                onException(throwable);
//            }
//        } catch (final Exception e) {
//            onException(e);
//        }
    }
}
