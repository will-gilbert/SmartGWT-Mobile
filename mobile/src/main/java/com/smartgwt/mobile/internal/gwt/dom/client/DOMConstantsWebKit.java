package com.smartgwt.mobile.internal.gwt.dom.client;

public class DOMConstantsWebKit extends DOMConstants {

    @Override
    public String getAtKeyframesText() {
        return "@-webkit-keyframes";
    }

    @Override
    public String getAnimationNamePropertyName() {
        return "webkitAnimationName";
    }

    @Override
    public String getAnimationPlayStatePropertyName() {
        return "webkitAnimationPlayState";
    }

    @Override
    public String getAnimationShorthandPropertyName() {
        return "webkitAnimation";
    }

    @Override
    public String getAnimationEndEventType() {
        return "webkitAnimationEnd";
    }

    @Override
    public String getBackfaceVisibilityPropertyName() {
        return "webkitBackfaceVisibility";
    }

    @Override
    public String getBoxShadowPropertyName() {
        return "webkitBoxShadow";
    }

    @Override
    public native int getCSSKeyframeRuleType() /*-{
        if ("WEBKIT_KEYFRAME_RULE" in $wnd.CSSRule) {
            return $wnd.CSSRule.WEBKIT_KEYFRAME_RULE;
        }
        return $wnd.CSSRule.KEYFRAME_RULE;
    }-*/;

    @Override
    public native int getCSSKeyframesRuleType() /*-{
        if ("WEBKIT_KEYFRAMES_RULE" in $wnd.CSSRule) {
            return $wnd.CSSRule.WEBKIT_KEYFRAMES_RULE;
        }
        return $wnd.CSSRule.KEYFRAMES_RULE;
    }-*/;

    @Override
    public String getOverflowScrollingPropertyName() {
        return "webkitOverflowScrolling";
    }

    @Override
    public String getTransformPropertyName() {
        return "webkitTransform";
    }

    @Override
    public String getTransformPropertyNameForCSSText() {
        return "-webkit-transform";
    }

    @Override
    public String getTransformOriginPropertyName() {
        return "webkitTransformOrigin";
    }

    @Override
    public String getTransitionShorthandPropertyName() {
        return "webkitTransition";
    }

    @Override
    public String getTransitionDelayPropertyName() {
        return "webkitTransitionDelay";
    }

    @Override
    public String getTransitionDurationPropertyName() {
        return "webkitTransitionDuration";
    }

    @Override
    public String getTransitionPropertyPropertyName() {
        return "webkitTransitionProperty";
    }

    @Override
    public String getTransitionTimingFunctionPropertyName() {
        return "webkitTransitionTimingFunction";
    }

    @Override
    public String getTransitionEndEventType() {
        return "webkitTransitionEnd";
    }
}
