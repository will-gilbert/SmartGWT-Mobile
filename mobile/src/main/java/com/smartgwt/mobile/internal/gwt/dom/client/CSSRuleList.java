package com.smartgwt.mobile.internal.gwt.dom.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public class CSSRuleList extends JavaScriptObject {

    protected CSSRuleList() {}

    public final native int length() /*-{
        return this.length;
    }-*/;

    public final native CSSRule get(int index) /*-{
        return this.item(index);
    }-*/;
}
