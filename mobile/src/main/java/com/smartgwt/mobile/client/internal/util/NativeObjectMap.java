package com.smartgwt.mobile.client.internal.util;

import com.google.gwt.core.client.JavaScriptObject;
import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public class NativeObjectMap<V> extends JavaScriptObject {

    public static <V> NativeObjectMap<V> create() {
        return JavaScriptObject.createObject().cast();
    }

    protected NativeObjectMap() {}

    public final native boolean containsKey(String key) /*-{
        if (null == key) throw @java.lang.NullPointerException::new(Ljava/lang/String;)("`key' cannot be `null'.");
        return this.hasOwnProperty(key);
    }-*/;

    public final native V get(String key) /*-{
        if (null == key) throw @java.lang.NullPointerException::new(Ljava/lang/String;)("`key' cannot be `null'.");
        var ret = this[key];
        if (undefined == ret) return null;
        return ret;
    }-*/;

    public final native void put(String key, V val) /*-{
        if (null == key) throw @java.lang.NullPointerException::new(Ljava/lang/String;)("`key' cannot be `null'.");
        this[key] = val;
    }-*/;
}
