package com.smartgwt.mobile.client.internal.util;


public class SCStaticImplBasic extends SCStaticImpl {

    static String prepStackTraceEntry(String entry) {
        if (entry == null) return null;
        entry = entry.replace("com_google_gwt_user_client_", "Gwtuc_");
        entry = entry.replace("com_google_gwt_", "Gwt_");
        entry = entry.replace("com_smartgwt_mobile_client_", "Sgwtmc_");
        entry = entry.replace("com_smartgwt_mobile_", "Sgwtm_");
        entry = entry.replace("java_lang_", "J_");
        return entry;
    }

    // When compiled in "Detailed" mode, gwtc outputs two functions that handle throwing
    // AssertionErrors. Replace these with simpler versions that log the message and/or
    // the name of the calling function.
    static native boolean replaceGWTThrowAssertionErrorFunctions() /*-{
        var orig;
        if (this.com_google_gwt_lang_Exceptions_throwAssertionError__Z) {
            orig = this.com_google_gwt_lang_Exceptions_throwAssertionError__Z;
            this.com_google_gwt_lang_Exceptions_throwAssertionError__Z = $entry(function com_google_gwt_lang_Exceptions_throwAssertionError__Z() {
                debugger;
                $wnd.console.error("AssertionError - " + @com.smartgwt.mobile.client.internal.util.SCStaticImplBasic::prepStackTraceEntry(Ljava/lang/String;)($wnd.String(arguments.callee.caller.name)));
            });
            this.com_google_gwt_lang_Exceptions_throwAssertionError__Z.orig = orig;
            $wnd.console.log("Replaced com_google_gwt_lang_Exceptions_throwAssertionError__Z()");
        }
        if (this.com_google_gwt_lang_Exceptions_throwAssertionError_1Object__Ljava_lang_Object_2Z) {
            orig = this.com_google_gwt_lang_Exceptions_throwAssertionError_1Object__Ljava_lang_Object_2Z;
            this.com_google_gwt_lang_Exceptions_throwAssertionError_1Object__Ljava_lang_Object_2Z = $entry(function com_google_gwt_lang_Exceptions_throwAssertionError_1Object__Ljava_lang_Object_2Z(message) {
                debugger;
                $wnd.console.error("AssertionError: " + message);
                $wnd.console.error(@com.smartgwt.mobile.client.internal.util.SCStaticImplBasic::prepStackTraceEntry(Ljava/lang/String;)($wnd.String(arguments.callee.caller.name)));
            });
            this.com_google_gwt_lang_Exceptions_throwAssertionError_1Object__Ljava_lang_Object_2Z.orig = orig;
            $wnd.console.log("Replaced com_google_gwt_lang_Exceptions_throwAssertionError_1Object__Ljava_lang_Object_2Z()");
        }
        return true;
    }-*/;

    static {
        assert replaceGWTThrowAssertionErrorFunctions();
    }

    @Override
    public void maybeInit() {}

    @Override
    public native void log(String category, Object value) /*-{
        $wnd.console.log(category + ": " + $wnd.String(value));
    }-*/;

    @Override
    public native void log(String category, String message, Object value) /*-{
        $wnd.console.log(category + ":" + message + ": " + $wnd.String(value));
    }-*/;

    @Override
    public native void warn(String category, String message) /*-{
        $wnd.console.warn(category + ":" + message);
    }-*/;
}
