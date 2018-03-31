package com.antrpc.common.serialization;

import com.antrpc.common.spi.BaseServiceLoader;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-03-31
 * Time: 11:01
 */
public class SerializerHolder {

    // SPI
    private static final Serializer serializer = BaseServiceLoader.load(Serializer.class);

    public static Serializer serializerImpl() {
        return serializer;
    }

}
