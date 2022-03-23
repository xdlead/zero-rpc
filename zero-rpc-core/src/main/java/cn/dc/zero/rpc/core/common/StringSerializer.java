package cn.dc.zero.rpc.core.common;

/**
 * @Author: DC
 * @Description:
 * @Date: 2022/1/23 16:54
 * @Version: 1.0
 */
public class StringSerializer {

    public static byte[] encode(String s) {
        return s == null ? new byte[0] : s.getBytes(RpcConstants.DEFAULT_CHARSET);
    }

    public static String decode(byte[] data) {
        return data == null ? null : new String(data, RpcConstants.DEFAULT_CHARSET);
    }
}
