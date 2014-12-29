package com.smartgwt.mobile.internal.gwt.dom.client;

import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public class CSSKeyframesRule extends CSSRule {

    public static boolean is(CSSRule cssRule) {
        return cssRule != null && cssRule.getType() == KEYFRAMES_RULE;
    }

    public static CSSKeyframesRule as(CSSRule cssRule) {
        assert is(cssRule);
        return (CSSKeyframesRule) cssRule;
    }

    protected CSSKeyframesRule() {}

    /**
     * @return the name of the keyframes, used by the <code>animation-name</code> property.
     */
    public final native String getName() /*-{
        return this.name;
    }-*/;

    public final native CSSRuleList getCSSRules() /*-{
        return this.cssRules;
    }-*/;

    public final native void appendRule(String rule) /*-{
        this.appendRule(rule);
    }-*/;

    public final native void deleteRule(String keyText) /*-{
        this.deleteRule(keyText);
    }-*/;

    public final native CSSKeyframeRule findRule(String keyText) /*-{
        return this.findRule(keyText);
    }-*/;
}
