package com.smartgwt.mobile.client.widgets.form.fields;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.widgets.Canvas;
import com.smartgwt.mobile.internal.gwt.dom.client.DOMConstants;
import com.smartgwt.mobile.internal.gwt.dom.client.WindowAnimationTiming;

// An implementation based on requestAnimationFrame().
// Scripted animation is used because of a clipping issue with WebKit browsers:
// http://stackoverflow.com/questions/12521870/webkit-issue-with-the-parents-curved-border-not-clipping-children-undergoing-an/
@SGWTInternal
public class SwitchItemImplRAF extends SwitchItemImplDefault {

    /**
     * Determines whether this <code>SwitchItemImpl</code> implementation will work for the
     * current GWT permutation.
     * 
     * @return whether this implementation will work.
     */
    public static boolean willWork() {
        // If this is changed, be sure to update the <replace-with> in SmartGwtMobile.gwt.xml.
        return Canvas.isSafari() || Canvas._isIOSMin6_0();
    }

    private Integer nextRequestID = null;
    private WindowAnimationTiming.FrameRequestCallback stepFun = null;

    @Override
    void create(SwitchItem self) {
        assert willWork() : "`SwitchItemImplRAF' should not be used unless willWork() returns `true'.";
        super.create(self);
    }

    @Override
    void destroyImpl(SwitchItem self) {
        if (nextRequestID != null) {
            WindowAnimationTiming.cancelAnimationFrame(nextRequestID.intValue());
            nextRequestID = null;
            stepFun = null;
        }
        super.destroyImpl(self);
    }

    @Override
    void onLoad(SwitchItem self) {
        super.onLoad(self);
        setCheckedNonAnimated(self, isChecked(self));
    }

    @Override
    void setVisible(SwitchItem self, boolean visible) {
        super.setVisible(self, visible);
        if (visible) setCheckedNonAnimated(self, isChecked(self));
    }

    @Override
    void setChecked(final SwitchItem self, final boolean checked) {
        super.setChecked(self, checked);
        // Return early if the `SwitchItem' is either not attached to the document or
        // invisible because offset width calculations will not work.
        if (!self.isAttached() || !self.isVisible()) return;
        stepFun = new WindowAnimationTiming.FrameRequestCallback() {

            private Double startTime;
            private final int totalTranslatePx = self.getElement().getOffsetWidth() - 29;

            @Override
            public void _doStep(double time) {
                if (SwitchItemImplRAF.this.stepFun != this || !self.isAttached() || !self.isVisible()) return;
                nextRequestID = null;

                if (startTime == null) startTime = time;
                final double progress = time - startTime;

                assert progress >= 0;

                String onTextTransformText, offTextTransformText, knobTransformText;
                if (checked) {
                    onTextTransformText = "translate(" + (-totalTranslatePx * (1 - progress / CSS.switchTransitionDurationMillis())) + "px)";
                    offTextTransformText = "translate(" + (totalTranslatePx * progress / CSS.switchTransitionDurationMillis()) + "px)";
                    knobTransformText = "translate(" + (totalTranslatePx * progress / CSS.switchTransitionDurationMillis()) + "px)";
                } else {
                    onTextTransformText = "translate(" + (-totalTranslatePx * progress / CSS.switchTransitionDurationMillis()) + "px)";
                    offTextTransformText = "translate(" + (totalTranslatePx * (1 - progress / CSS.switchTransitionDurationMillis())) + "px)";
                    knobTransformText = "translate(" + (totalTranslatePx * (1 - progress / CSS.switchTransitionDurationMillis())) + "px)";
                }
                final boolean shouldContinue = (progress < CSS.switchTransitionDurationMillis());
                if (!shouldContinue) {
                    if (checked) {
                        onTextTransformText = "none";
                        offTextTransformText = "translate(" + totalTranslatePx + "px)";
                        knobTransformText = "translate(" + totalTranslatePx + "px)";
                    } else {
                        onTextTransformText = "translate(" + totalTranslatePx + "px)";
                        offTextTransformText = "none";
                        knobTransformText = "none";
                    }
                }
                onTextElem.getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), onTextTransformText);
                offTextElem.getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), offTextTransformText);
                knobElem.getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), knobTransformText);

                if (shouldContinue) {
                    nextRequestID = Integer.valueOf(WindowAnimationTiming.requestAnimationFrame(this));
                }
            }
        };
        nextRequestID = Integer.valueOf(WindowAnimationTiming.requestAnimationFrame(stepFun));
    }

    private void setCheckedNonAnimated(SwitchItem self, boolean checked) {
        super.setChecked(self, checked);
        if (!self.isAttached() || !self.isVisible()) return;
        final int totalTranslatePx = self.getElement().getOffsetWidth() - 29;
        final String onTextTransformText, offTextTransformText, knobTransformText;
        if (checked) {
            onTextTransformText = "none";
            offTextTransformText = "translate(" + totalTranslatePx + "px)";
            knobTransformText = "translate(" + totalTranslatePx + "px)";
        } else {
            onTextTransformText = "translate(" + totalTranslatePx + "px)";
            offTextTransformText = "none";
            knobTransformText = "none";
        }
        onTextElem.getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), onTextTransformText);
        offTextElem.getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), offTextTransformText);
        knobElem.getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), knobTransformText);
    }
}
