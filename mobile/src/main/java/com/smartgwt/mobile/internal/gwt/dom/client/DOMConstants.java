package com.smartgwt.mobile.internal.gwt.dom.client;

import com.google.gwt.core.client.GWT;
import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public class DOMConstants {

    public static final DOMConstants INSTANCE = GWT.create(DOMConstants.class);

    public String getAtKeyframesText() {
        return "@keyframes";
    }

    public String getAnimationNamePropertyName() {
        return "animationName";
    }

    public String getAnimationPlayStatePropertyName() {
        return "animationPlayState";
    }

    public String getAnimationShorthandPropertyName() {
        return "animation";
    }

    public String getAnimationEndEventType() {
        return "animationend";
    }

    public String getBackfaceVisibilityPropertyName() {
        return "backfaceVisibility";
    }

    public String getBackgroundSizePropertyName() {
        return "backgroundSize";
    }

    public String getBoxShadowPropertyName() {
        return "boxShadow";
    }

    public native int getCSSKeyframeRuleType() /*-{
        return $wnd.CSSRule.KEYFRAME_RULE;
    }-*/;

    public native int getCSSKeyframesRuleType() /*-{
        return $wnd.CSSRule.KEYFRAMES_RULE;
    }-*/;

    public String getOverflowScrollingPropertyName() {
        return "overflowScrolling";
    }

    public String getTransformPropertyName() {
        return "transform";
    }

    public String getTransformPropertyNameForCSSText() {
        return "transform";
    }

    public String getTransformOriginPropertyName() {
        return "transformOrigin";
    }

    public String getTransitionShorthandPropertyName() {
        return "transition";
    }

    public String getTransitionDelayPropertyName() {
        return "transitionDelay";
    }

    public String getTransitionDurationPropertyName() {
        return "transitionDuration";
    }

    public String getTransitionPropertyPropertyName() {
        return "transitionProperty";
    }

    public String getTransitionTimingFunctionPropertyName() {
        return "transitionTimingFunction";
    }

    public String getTransitionEndEventType() {
        return "transitionend";
    }
}
