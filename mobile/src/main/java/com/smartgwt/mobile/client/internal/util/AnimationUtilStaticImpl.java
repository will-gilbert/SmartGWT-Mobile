package com.smartgwt.mobile.client.internal.util;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Timer;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.core.Function;
import com.smartgwt.mobile.client.internal.util.AnimationUtil.AnimationProperty;
import com.smartgwt.mobile.client.internal.util.AnimationUtil.AnimationRegistration;
import com.smartgwt.mobile.client.internal.util.AnimationUtil.AnimationTimingFunction;

@SGWTInternal
public class AnimationUtilStaticImpl {

    static int numAnimations = 0;

    public static abstract class AbstractAnimationRegistration implements AnimationRegistration {

        protected final Element element;
        private AnimationProperty[] properties;
        private boolean paused = false;
        private boolean finished = false;
        private Function<Void> callback;

        protected AbstractAnimationRegistration(Element element, AnimationProperty[] properties, Function<Void> callback) {
            this.element = element;
            this.properties = properties;
            this.callback = callback;
        }

        @Override
        public final boolean isPaused() {
            return paused;
        }

        @Override
        public final void setPaused(boolean paused) {
            if (finished) {
                throw new IllegalStateException("The animation has already finished.");
            }
            if (this.paused != paused) {
                try {
                    if (paused) {
                        doPause();
                    } else {
                        doResume();
                    }
                } finally {
                    this.paused = paused;
                }
            }
        }

        protected abstract void doPause();

        protected abstract void doResume();

        @Override
        public final boolean isFinished() {
            return finished;
        }

        @Override
        public final void finish() {
            if (paused) {
                throw new IllegalStateException("The animation is paused.");
            }
            if (!finished) {
                try {
                    doFinish();
                } finally {
                    finished = true;
                    final Style style = element.getStyle();
                    for (AnimationProperty property : properties) {
                        style.setProperty(property.getPropertyName(), property.getEndValueText());
                    }
                    if (callback != null) {
                        callback.execute();
                    }
                }
            }
        }

        protected abstract void doFinish();
    }

    private static class DefaultAnimationRegistration extends AbstractAnimationRegistration {

        private Timer timer;

        DefaultAnimationRegistration(Element element, AnimationProperty[] properties, Function<Void> callback) {
            super(element, properties, callback);
            doResume();
        }

        @Override
        protected final void doPause() {
            timer.cancel();
        }

        @Override
        protected final void doResume() {
            timer = new Timer() {
                @Override
                public void run() {
                    finish();
                }
            };
            timer.schedule(1);
        }

        @Override
        protected final void doFinish() {}
    }

    // The default implementation simply sets the ending property values and calls the callback
    // if provided.
    public AnimationRegistration animate(Element element, AnimationProperty[] properties, int durationMs, AnimationTimingFunction timingFunction, final Function<Void> callback) {
        ++numAnimations;
        return new DefaultAnimationRegistration(element, properties, callback);
    }
}
