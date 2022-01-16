package cn.dc.zero.rpc.core.serializer;


import cn.dc.zero.rpc.core.ext.Extensible;
/**
 * @Author:     DC
 * @Description:  序列化基类
 * @Date:    2021/12/30 22:49
 * @Version:    1.0
 */
@Extensible(singleton = false)
public abstract class RpcSerializer {

    public abstract <T> byte[] serialize(T obj);

    public abstract <T> Object deserialize(byte[] bytes);
}
