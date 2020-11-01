package com.radiance.tonclient;

import com.radiance.scala.tonclient.TonContext;

public class TONContext {

    public static native String loadLibrary(String path);
    public static native String createContext(String config);
    public static native void destroyContext(int context);
    public static native void request(int context, String functionName, String params, int requestId);
    public static native String requestSync(int context, String functionName, String params);

    private static void responseHandler(int id, String params, int type, boolean finished) {
        TonContext.responseHandler(id, params, type, finished);
    }
}
