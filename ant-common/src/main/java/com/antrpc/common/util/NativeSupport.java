package com.antrpc.common.util;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-03-30
 * Time: 9:45
 */
public class NativeSupport {

    private static final boolean SUPPORT_NATIVE_ET;

    static {
        boolean epool ;
        try{
            Class.forName("io.netty.channel.epool.Native");
            epool = true;
        }catch (Exception e){
            epool = false;
        }
        SUPPORT_NATIVE_ET = epool;
    }

    public static boolean isSupportNativeET(){
        return SUPPORT_NATIVE_ET;
    }


}
