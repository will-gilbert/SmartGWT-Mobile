package com.smartgwt.mobile.client.internal.util;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.mobile.client.core.Function;
import com.smartgwt.mobile.client.internal.util.AnimationUtil.AnimationProperty;
import com.smartgwt.mobile.client.internal.util.AnimationUtil.AnimationRegistration;
import com.smartgwt.mobile.client.internal.util.AnimationUtil.AnimationTimingFunction;
import com.smartgwt.mobile.internal.gwt.dom.client.CSSKeyframesRule;
import com.smartgwt.mobile.internal.gwt.dom.client.CSSRule;
import com.smartgwt.mobile.internal.gwt.dom.client.CSSRuleList;
import com.smartgwt.mobile.internal.gwt.dom.client.CSSStyleSheet;
import com.smartgwt.mobile.internal.gwt.dom.client.DOMConstants;
import com.smartgwt.mobile.internal.gwt.dom.client.DOMImplementationCSS;
import com.smartgwt.mobile.internal.gwt.dom.client.SuperElement;

/**
 * @see <a href="http://www.w3.org/TR/css3-animations/">CSS Animations - W3C</a>
 */
public class AnimationUtilStaticImplCSSAnimations extends AnimationUtilStaticImpl {

    protected class StandardAnimationRegistration extends AbstractAnimationRegistration {

        private String animationName;
        private HandlerRegistration animationEndRegistration;
        private Integer insertionIndex;

        public StandardAnimationRegistration(Element element, AnimationProperty[] properties, int animationNumber, String animationName, String rule, Function<Void> callback) {
            super(element, properties, callback);
            this.animationName = animationName;
            final JavaScriptObject animationEndListener = createAnimationEndListener(animationNumber, animationName);
            animationEndRegistration = element.<SuperElement>cast().addEventListener(DOMConstants.INSTANCE.getAnimationEndEventType(), animationEndListener, false);
            insertionIndex = cssStyleSheet.appendRule(rule);
        }

        protected native JavaScriptObject createAnimationEndListener(int animationNumber, String animationName) /*-{
            var self = this;
            var listener = function handleAnimationEnd(event) {
                self.@com.smartgwt.mobile.client.internal.util.AnimationUtilStaticImplCSSAnimations.StandardAnimationRegistration::onAnimationEnd(Lcom/google/gwt/dom/client/NativeEvent;Ljava/lang/String;D)(event, event.animationName, event.elapsedTime);
            };
            return listener;
        }-*/;

        @Override
        protected void doPause() {
            doPauseResume(true);
        }

        @Override
        protected void doResume() {
            doPauseResume(false);
        }

        private void doPauseResume(boolean paused) {
            final Style style = element.getStyle();
            if (animationName.equals(style.getProperty(DOMConstants.INSTANCE.getAnimationNamePropertyName()))) {
                style.setProperty(DOMConstants.INSTANCE.getAnimationPlayStatePropertyName(), paused ? "paused" : "running");
            }
        }

        private void onAnimationEnd(NativeEvent event, String animationName, double elapsedTime) {
            if (this.animationName.equals(animationName)) {
                finish();
            }
        }

        @Override
        protected void doFinish() {
            animationEndRegistration.removeHandler();
            final CSSRuleList cssRules = cssStyleSheet.getCSSRules();
            assert insertionIndex != null;
            int i = Math.min(cssRules.length() - 1, insertionIndex.intValue());
            while (i >= 0) {
                CSSRule cssRule = cssRules.get(i);
                if (CSSKeyframesRule.is(cssRule)) {
                    CSSKeyframesRule keyframesRule = CSSKeyframesRule.as(cssRule);
                    if (animationName.equals(keyframesRule.getName())) {
                        cssStyleSheet.deleteRule(i);
                        break;
                    }
                }
                --i;
            }
            final Style style = element.getStyle();
            if (animationName.equals(style.getProperty(DOMConstants.INSTANCE.getAnimationNamePropertyName()))) {
                style.setProperty(DOMConstants.INSTANCE.getAnimationShorthandPropertyName(), "none");
            }
        }
    }

    protected final CSSStyleSheet cssStyleSheet = DOMImplementationCSS.get().createCSSStyleSheet();

    protected AnimationRegistration createAnimationRegistration(Element element, AnimationProperty[] properties, int animationNumber, String animationName, String rule, int durationMillis, AnimationTimingFunction timingFunction, Function<Void> callback) {
        if (timingFunction == null) timingFunction = AnimationUtil.LINEAR;
        StandardAnimationRegistration ret = this.new StandardAnimationRegistration(element, properties, animationNumber, animationName, rule, callback);
        element.getStyle().setProperty(DOMConstants.INSTANCE.getAnimationShorthandPropertyName(), animationName + " " + durationMillis + "ms " + timingFunction.getCSSText());
        return ret;
    }

    @Override
    public AnimationRegistration animate(Element element, AnimationProperty[] properties, int durationMillis, AnimationTimingFunction timingFunction, Function<Void> callback) {
        final String currentAnimationName = element.getStyle().getProperty(DOMConstants.INSTANCE.getAnimationNamePropertyName());
        if (currentAnimationName != null && ! currentAnimationName.isEmpty() && ! "none".equals(currentAnimationName)) {
            throw new IllegalStateException("Animation '" + currentAnimationName + "' is already set.");
        }

        final int animationNumber = ++numAnimations;
        final String animationName = "isc-animation" + animationNumber;
        StringBuilder sb = new StringBuilder();
        sb.append(DOMConstants.INSTANCE.getAtKeyframesText()).append(' ').append(animationName).append(" {\n");
        sb.append("    from {\n");
        for (AnimationProperty property : properties) {
            final String startValueText = property.getStartValueText();
            if (startValueText != null) {
                sb.append("        ").append(property.getPropertyNameForCSSText()).append(": ").append(startValueText).append(";\n");
            }
        }
        sb.append("    }\n");
        sb.append("    to {\n");
        for (AnimationProperty property : properties) {
            sb.append("        ").append(property.getPropertyNameForCSSText()).append(": ").append(property.getEndValueText()).append(";\n");
        }
        sb.append("    }\n");
        sb.append("}\n");
        final String rule = sb.toString();
        return createAnimationRegistration(element, properties, animationNumber, animationName, rule, durationMillis, timingFunction, callback);
    }
}

