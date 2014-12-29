package com.smartgwt.mobile.internal.gwt.dom.client;

public class DOMConstantsGecko extends DOMConstants {

    @Override
    public String getAtKeyframesText() {
        return "@-moz-keyframes";
    }

    @Override
    public String getAnimationNamePropertyName() {
        return "MozAnimationName";
    }

    @Override
    public String getAnimationPlayStatePropertyName() {
        return "MozAnimationPlayState";
    }

    @Override
    public String getAnimationShorthandPropertyName() {
        return "MozAnimation";
    }

    @Override
    public String getAnimationEndEventType() {
        return "mozAnimationEnd";
    }

    @Override
    public String getBackfaceVisibilityPropertyName() {
        return "MozBackfaceVisibility";
    }

    @Override
    public String getBoxShadowPropertyName() {
        return "MozBoxShadow";
    }

    @Override
    public native int getCSSKeyframeRuleType() /*-{
        if ("MOZ_KEYFRAME_RULE" in $wnd.CSSRule) {
            return $wnd.CSSRule.MOZ_KEYFRAME_RULE;
        }
        return $wnd.CSSRule.KEYFRAME_RULE;
    }-*/;

    @Override
    public native int getCSSKeyframesRuleType() /*-{
        if ("MOZ_KEYFRAMES_RULE" in $wnd.CSSRule) {
            return $wnd.CSSRule.MOZ_KEYFRAMES_RULE;
        }
        return $wnd.CSSRule.KEYFRAMES_RULE;
    }-*/;

    @Override
    public String getTransformPropertyName() {
        return "MozTransform";
    }

    @Override
    public String getTransformPropertyNameForCSSText() {
        return "-moz-transform";
    }

    @Override
    public String getTransformOriginPropertyName() {
        return "MozTransformOrigin";
    }

    @Override
    public String getTransitionShorthandPropertyName() {
        return "MozTransition";
    }

    @Override
    public String getTransitionDelayPropertyName() {
        return "MozTransitionDelay";
    }

    @Override
    public String getTransitionDurationPropertyName() {
        return "MozTransitionDuration";
    }

    @Override
    public String getTransitionPropertyPropertyName() {
        return "MozTransitionProperty";
    }

    @Override
    public String getTransitionTimingFunctionPropertyName() {
        return "MozTransitionTimingFunction";
    }

    @Override
    public String getTransitionEndEventType() {
        return "transitionend"; // https://developer.mozilla.org/en/CSS/CSS_transitions
    }
}
