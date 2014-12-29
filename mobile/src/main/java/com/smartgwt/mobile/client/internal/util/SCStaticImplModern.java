package com.smartgwt.mobile.client.internal.util;

public class SCStaticImplModern extends SCStaticImpl {

    @Override
    public native void log(String category, Object value) /*-{
        $wnd.console.log("%s: %o", category, value);
    }-*/;

    @Override
    public native void log(String category, String message, Object value) /*-{
        $wnd.console.log("%s:%s: %o", category, message, value);
    }-*/;

    @Override
    public native void warn(String category, String message) /*-{
        $wnd.console.warn("%s:%s", category, message);
    }-*/;
}
