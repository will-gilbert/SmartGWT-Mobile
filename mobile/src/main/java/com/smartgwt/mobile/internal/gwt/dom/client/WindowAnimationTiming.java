package com.smartgwt.mobile.internal.gwt.dom.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.smartgwt.mobile.SGWTInternal;

// http://www.w3.org/TR/animation-timing/
@SGWTInternal
public final class WindowAnimationTiming {

    @SGWTInternal
    public static abstract class FrameRequestCallback {
        @SuppressWarnings("unused")
        private JavaScriptObject wrappedCallbackFun;

        @SGWTInternal
        public abstract void _doStep(double time);
    }

    private static final JavaScriptObject requestAnimationFrameFun = getRequestAnimationFrameFun(),
            cancelAnimationFrameFun = getCancelAnimationFrameFun();
    private static native JavaScriptObject getRequestAnimationFrameFun() /*-{
        return (window.requestAnimationFrame ||
                window.mozRequestAnimationFrame ||
                window.webkitRequestAnimationFrame ||
                function (callback) {
                    return window.setTimeout(function () {
                        callback(@com.smartgwt.mobile.internal.gwt.dom.client.WindowAnimationTiming::getNow()());
                    }, 16);
                });
    }-*/;
    private static native JavaScriptObject getCancelAnimationFrameFun() /*-{
    return (window.cancelAnimationFrame ||
            window.mozCancelAnimationFrame ||
            window.webkitCancelAnimationFrame ||
            // The spec changed as of the Working Draft of 21 February 2012.
            // cancelAnimationFrame() used to be called cancelRequestAnimationFrame().
            window.cancelRequestAnimationFrame ||
            window.mozCancelRequestAnimationFrame ||
            window.webkitCancelRequestAnimationFrame ||
            window.clearTimeout);
    }-*/;

    public static native double getNow() /*-{
        if (window.performance && window.performance.now) return window.performance.now();
        else if (Date.now) return Date.now();
        else return (new Date).getTime();
    }-*/;

    public static native int requestAnimationFrame(FrameRequestCallback callback) /*-{
        if (callback == null) throw @java.lang.NullPointerException::new()();

        var wrappedCallbackFun = callback.@com.smartgwt.mobile.internal.gwt.dom.client.WindowAnimationTiming.FrameRequestCallback::wrappedCallbackFun;
        if (wrappedCallbackFun == null) {
            wrappedCallbackFun = $entry(function (time) {
                callback.@com.smartgwt.mobile.internal.gwt.dom.client.WindowAnimationTiming.FrameRequestCallback::_doStep(D)(time);
            });
            callback.@com.smartgwt.mobile.internal.gwt.dom.client.WindowAnimationTiming.FrameRequestCallback::wrappedCallbackFun = wrappedCallbackFun;
        }
        return (@com.smartgwt.mobile.internal.gwt.dom.client.WindowAnimationTiming::requestAnimationFrameFun).call(window, wrappedCallbackFun);
    }-*/;

    public static native void cancelAnimationFrame(int handle) /*-{
        (@com.smartgwt.mobile.internal.gwt.dom.client.WindowAnimationTiming::cancelAnimationFrameFun).call(window, handle);
    }-*/;

    private WindowAnimationTiming() {}
}
