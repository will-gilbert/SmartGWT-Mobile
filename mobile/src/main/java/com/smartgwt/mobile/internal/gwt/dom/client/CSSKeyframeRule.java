package com.smartgwt.mobile.internal.gwt.dom.client;

import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public class CSSKeyframeRule extends CSSRule {

    public static boolean is(CSSRule cssRule) {
        return cssRule != null && cssRule.getType() == KEYFRAME_RULE;
    }

    public static CSSKeyframeRule as(CSSRule cssRule) {
        assert is(cssRule);
        return (CSSKeyframeRule) cssRule;
    }

    protected CSSKeyframeRule() {}

    /**
     * Represents the key as the string representation of a floating point number between 0 and 1.
     */
    public final native String getKeyText() /*-{
        return this.keyText;
    }-*/;

    public final native float getKeyTextAsFloat() /*-{
        return $wnd.parseFloat(this.keyText);
    }-*/;
}
