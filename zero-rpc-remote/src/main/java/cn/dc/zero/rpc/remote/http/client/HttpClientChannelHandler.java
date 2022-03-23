package cn.dc.zero.rpc.remote.http.client;

import cn.dc.zero.rpc.core.exception.RpcErrorType;
import cn.dc.zero.rpc.core.log.LogCodes;
import cn.dc.zero.rpc.core.remote.RemoteHandler;
import cn.dc.zero.rpc.core.remote.RpcResponse;
import cn.dc.zero.rpc.core.util.CommonUtils;
import cn.dc.zero.rpc.core.util.NetUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http2.HttpConversionUtil;
import io.netty.util.internal.PlatformDependent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author: DC
 * @Description:
 * @Date: 2022/1/23 15:37
 * @Version: 1.0
 */
public class HttpClientChannelHandler extends SimpleChannelInboundHandler<FullHttpResponse> {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(HttpClientChannelHandler.class);

    private static final ConcurrentHashMap<Integer, AbstractHttpRemoteHandler> handlerMap = new ConcurrentHashMap<>();

    public AbstractHttpRemoteHandler removeRequest(int requestId) {
        return handlerMap.remove(requestId);
    }

    public void putHandler(int requestId,AbstractHttpRemoteHandler remoteHandler){
        handlerMap.put(requestId,remoteHandler);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
        HttpHeaders headers = msg.headers();
        Integer streamId = headers.getInt(HttpConversionUtil.ExtensionHeaderNames.STREAM_ID.text());
//        if (streamId == null) {
//            if (LOGGER.isWarnEnabled()) {
//                LOGGER.warn("HttpResponseHandler unexpected message received: {}, data is {}", msg.toString(),
//                        NettyHelper.toString(msg.content()));
//            }
//            return;
//        }

        AbstractHttpRemoteHandler handler = removeRequest(streamId);
        if(handler == null){
            //抛出异常
        }

        handler.receiveHttpResponse(msg);

    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
//        Channel channel = ctx.channel();
//        if (LOGGER.isInfoEnabled()) {
//            LOGGER.info("Channel inactive: {}", channel);
//        }
//        final Exception e = new SofaRpcException(RpcErrorType.CLIENT_NETWORK, "Channel "
//                + NetUtils.channelToString(channel.localAddress(), channel.remoteAddress())
//                + " has been closed, remove future when channel inactive.");
//        Iterator<Map.Entry<Integer, Map.Entry<ChannelFuture, HttpClientChannelHandler>>> it =
//                streamIdPromiseMap.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry<Integer, Map.Entry<ChannelFuture, HttpClientChannelHandler>> mapEntry = it.next();
//            it.remove();
//            Map.Entry<ChannelFuture, HttpClientChannelHandler> entry = mapEntry.getValue();
//            entry.getValue().onException(e);
//        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        LOGGER.error(LogCodes.getLog(LogCodes.ERROR_CATCH_EXCEPTION), cause);
    }

    public void receiveHttpResponse(FullHttpResponse msg) {
        HttpHeaders headers = msg.headers();
        ByteBuf content = msg.content();
//        NettyByteBuffer data = new NettyByteBuffer(content);
//        try {
//            if (msg.status() == HttpResponseStatus.OK) {
//                // 正常返回
//                final RpcResponse response = new RpcResponse();
//                String isError = headers.get(RemotingConstants.HEAD_RESPONSE_ERROR);
//                if (CommonUtils.isTrue(isError)) {
//                    // 业务异常
//                    String errorMsg = StringSerializer.decode(data.array());
//                    Throwable throwable = new SofaRpcException(RpcErrorType.SERVER_BIZ, errorMsg);
//                    response.setAppResponse(throwable);
//                } else {
//                    // 获取序列化类型
//                    if (data.readableBytes() > 0) {
//                        byte serializeType;
//                        String codeName = headers.get(RemotingConstants.HEAD_SERIALIZE_TYPE);
//                        if (codeName != null) {
//                            serializeType = HttpTransportUtils.getSerializeTypeByName(codeName);
//                        } else {
//                            // HEAD_SERIALIZE_TYPE 没设置的话 再取 content-type 兜底下
//                            String contentType = StringUtils.toString(headers.get(HttpHeaderNames.CONTENT_TYPE));
//                            serializeType = HttpTransportUtils.getSerializeTypeByContentType(contentType);
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


