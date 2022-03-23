package cn.dc.zero.rpc.remote.http.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http2.*;

/**
 * @Author: DC
 * @Description: http 客户端处理
 * @Date: 2022/1/20 22:59
 * @Version: 1.0
 */
public class HttpClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
//        final Http2Connection connection = new DefaultHttp2Connection(false);
//        connectionHandler = new HttpToHttp2ConnectionHandlerBuilder()
//                .frameListener(
//                        new DelegatingDecompressorFrameListener(connection, new InboundHttp2ToHttpAdapterBuilder(connection)
//                                .maxContentLength(transportConfig.getPayload()).propagateSettings(true).build()))
//                .connection(connection).build();
//        responseHandler = new Http2ClientChannelHandler();
//        settingsHandler = new Http2SettingsHandler(ch.newPromise());
//        String protocol = transportConfig.getProviderInfo().getProtocolType();
//        if (RpcConstants.PROTOCOL_TYPE_H2.equals(protocol)) {
//            configureSsl(ch);
//        } else if (RpcConstants.PROTOCOL_TYPE_H2C.equals(protocol)) {
//            if (!useH2cPriorKnowledge) {
//                configureClearTextWithHttpUpgrade(ch);
//            } else {
//                configureClearTextWithPriorKnowledge(ch);
//            }
//        }
    }
}
