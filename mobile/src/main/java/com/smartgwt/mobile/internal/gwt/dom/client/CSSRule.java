package com.smartgwt.mobile.internal.gwt.dom.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public class CSSRule extends JavaScriptObject {

    public static final int UNKNOWN_RULE = 0,
            STYLE_RULE = 1,
            CHARSET_RULE = 2,
            IMPORT_RULE = 3,
            MEDIA_RULE = 4,
            FONT_FACE_RULE = 5,
            PAGE_RULE = 6,

            // CSS Animations
            // http://www.w3.org/TR/css3-animations/#dom-interfaces
            KEYFRAMES_RULE = DOMConstants.INSTANCE.getCSSKeyframesRuleType(),
            KEYFRAME_RULE = DOMConstants.INSTANCE.getCSSKeyframeRuleType();

    protected CSSRule() {}

    public final native int getType() /*-{
        return this.type;
    }-*/;

    public final native String getCSSText() /*-{
        return this.cssText;
    }-*/;

    public final native CSSStyleSheet getParentStyleSheet() /*-{
        return this.parentStyleSheet;
    }-*/;

    public final native CSSRule getParentRule() /*-{
        return this.parentRule;
    }-*/;
}
