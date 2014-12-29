package com.smartgwt.mobile.client.internal.util;

import com.google.gwt.core.client.JavaScriptObject;
import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public class NativeIntMap extends JavaScriptObject {

    public static NativeIntMap create() {
        return JavaScriptObject.createObject().cast();
    }

    protected NativeIntMap() {}

    public final native boolean containsKey(String key) /*-{
        if (null == key) throw @java.lang.NullPointerException::new(Ljava/lang/String;)("`key' cannot be `null'.");
        return this.hasOwnProperty(key);
    }-*/;

    public final native int get(String key, int nullEquivalent) /*-{
        if (null == key) throw @java.lang.NullPointerException::new(Ljava/lang/String;)("`key' cannot be `null'.");
        var ret = this[key];
        if (undefined == ret) return nullEquivalent;
        return ret;
    }-*/;

    public final native void put(String key, int val) /*-{
        if (null == key) throw @java.lang.NullPointerException::new(Ljava/lang/String;)("`key' cannot be `null'.");
        this[key] = val;
    }-*/;
}
