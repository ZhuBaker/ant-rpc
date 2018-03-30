package com.antrpc.remoting.model;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-03-29
 * Time: 15:18
 */
public class ByteHolder {

    private transient byte[] bytes;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public int size(){
        return bytes == null ? 0 : bytes.length;
    }
}
