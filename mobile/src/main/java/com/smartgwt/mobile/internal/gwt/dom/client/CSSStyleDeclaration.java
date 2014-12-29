package com.smartgwt.mobile.internal.gwt.dom.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public class CSSStyleDeclaration extends JavaScriptObject {

    protected CSSStyleDeclaration() {}

    public final native String getCSSText() /*-{
        return this.cssText;
    }-*/;

    public final native String get(String propertyName) /*-{
        return this[propertyName];
    }-*/;

    public final native String getPropertyValue(String propertyName) /*-{
        return this.getPropertyValue(propertyName);
    }-*/;
}
