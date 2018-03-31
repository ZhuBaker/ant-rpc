package com.antrpc.common.serialization;

/**
 * Created with IntelliJ IDEA.
 * Description: 序列化接口
 * User: zhubo
 * Date: 2018-03-31
 * Time: 10:49
 */
public interface Serializer {
    /**
     * 将对象序列化成byte[]
     * @param obj
     * @return
     */
    <T> byte[] writeObject(T obj);

    /**
     * 将byte数组反序列成对象
     * @param bytes
     * @param clazz
     * @return
     */
    <T> T readObject(byte[] bytes, Class<T> clazz);
}
