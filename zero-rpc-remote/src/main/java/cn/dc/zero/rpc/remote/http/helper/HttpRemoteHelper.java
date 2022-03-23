package cn.dc.zero.rpc.remote.http.helper;

import cn.dc.zero.rpc.core.serializer.RpcSerializerFactory;

/**
 * @Author: DC
 * @Description:
 * @Date: 2022/1/23 21:36
 * @Version: 1.0
 */
public class HttpRemoteHelper {
    /**
     * 根据序列化名称获得序列化类型
     *
     * @param serialization 序列化类型名称
     * @return 序列化编码
     */
//    public static byte getSerializeTypeByName(String serialization) throws SofaRpcException {
//        String sz = serialization.toLowerCase();
//        Byte code;
//        if (RpcConstants.SERIALIZE_HESSIAN2.equals(sz) || RpcConstants.SERIALIZE_HESSIAN.equals(sz)) {
//            code = RpcSerializerFactory.getCodeByAlias(RpcConstants.SERIALIZE_HESSIAN2);
//        } else {
//            code = SerializerFactory.getCodeByAlias(serialization);
//        }
//        if (code != null) {
//            return code;
//        } else {
//            throw new SofaRpcException(RpcErrorType.SERVER_DESERIALIZE, LogCodes.getLog(
//                    LogCodes.ERROR_UNSUPPORTED_SERIALIZE_TYPE, serialization));
//        }
//    }
//
//    /**
//     * Parse serialize type from content type
//     *
//     * @param contentType Content-type of http request
//     * @return serialize code
//     * @throws SofaRpcException unknown content type
//     */
//    public static byte getSerializeTypeByContentType(String contentType) throws SofaRpcException {
//        if (StringUtils.isNotBlank(contentType)) {
//            String ct = contentType.toLowerCase();
//            if (ct.contains("text/plain") || ct.contains("text/html") || ct.contains("application/json")) {
//                return getSerializeTypeByName(RpcConstants.SERIALIZE_JSON);
//            } else if (ct.contains(RpcConstants.SERIALIZE_PROTOBUF)) {
//                return getSerializeTypeByName(RpcConstants.SERIALIZE_PROTOBUF);
//            } else if (ct.contains(RpcConstants.SERIALIZE_HESSIAN)) {
//                return getSerializeTypeByName(RpcConstants.SERIALIZE_HESSIAN2);
//            }
//        }
//        throw new SofaRpcException(RpcErrorType.SERVER_DESERIALIZE, LogCodes.getLog(
//                LogCodes.ERROR_UNSUPPORTED_CONTENT_TYPE, contentType, RemotingConstants.HEAD_SERIALIZE_TYPE));
//    }
//
//    protected static String[] getInterfaceIdAndMethod(String uri) {
//        String[] result;
//        int i = uri.indexOf('?');
//        if (i > 0) {
//            uri = uri.substring(0, i);
//        }
//        String[] end = uri.split("/");
//        if (end.length < 3) {
//            throw new SofaRpcException(RpcErrorType.SERVER_DESERIALIZE,
//                    "The correct URI format is: http://ip:port/serviceName/methodName");
//        }
//        int resultLength = 2;
//        result = new String[resultLength];
//        //从第二个元素开始copy 第一个是空字符串
//        System.arraycopy(end, 1, result, 0, resultLength);
//        return result;
//    }
}
