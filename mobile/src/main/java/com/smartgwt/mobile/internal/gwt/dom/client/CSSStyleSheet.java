package com.smartgwt.mobile.internal.gwt.dom.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public class CSSStyleSheet extends JavaScriptObject {

    protected CSSStyleSheet() {}

    public final native CSSRule getOwnerRule() /*-{
        return this.ownerRule;
    }-*/;

    public final native CSSRuleList getCSSRules() /*-{
        return this.cssRules;
    }-*/;

    public final native int insertRule(String rule, int index) /*-{
        return this.insertRule(rule, index);
    }-*/;

    public final int appendRule(String rule) {
        return insertRule(rule, getCSSRules().length());
    }

    public final native void deleteRule(int index) /*-{
        return this.deleteRule(index);
    }-*/;
}
