package cn.dc.zero.rpc.serializer.jdk;

import cn.dc.zero.rpc.core.exception.RpcRuntimeException;
import cn.dc.zero.rpc.core.ext.Extension;
import cn.dc.zero.rpc.core.serializer.RpcSerializer;

import java.io.*;

/**
 * @Author:     DC
 * @Description:  JDK序列化
 * @Date:    2022/1/16 20:30
 * @Version:    1.0
 */
@Extension("jdk")
public class JdkSerializer extends RpcSerializer {
    @Override
    public <T> byte[] serialize(T obj) {

        ByteArrayOutputStream os = new ByteArrayOutputStream ();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(os);
            oos.writeObject(obj);
            oos.flush();
        } catch (IOException e) {
            //抛出异常
            throw new RpcRuntimeException("Serialize error",e);

        }finally {
            try {
                os.close();
            } catch (IOException e) {
                //抛出异常
                throw new RpcRuntimeException("stream close error",e);
            }
            try {
                oos.close();
            } catch (IOException e) {
               //抛出异常
                throw new RpcRuntimeException("stream close error",e);
            }
        }
        return os.toByteArray();
    }

    @Override
    public <T> Object deserialize(byte[] bytes) {
        Object object=null;
        ByteArrayInputStream bis=new ByteArrayInputStream(bytes);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bis);
            object = ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            //
            throw new RpcRuntimeException("Deserialize error",e);
        }finally {
            try {
                bis.close();
            } catch (IOException e) {
                //抛出异常
                throw new RpcRuntimeException("stream close error",e);
            }
            try {
                ois.close();
            } catch (IOException e) {
                //抛出异常
                throw new RpcRuntimeException("stream close error",e);
            }


        }

        return object;
    }
}
