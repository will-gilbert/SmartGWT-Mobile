/*
 * SmartGWT Mobile
 * Copyright 2008 and beyond, Isomorphic Software, Inc.
 *
 * SmartGWT Mobile is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.  SmartGWT Mobile is also
 * available under typical commercial license terms - see
 * http://smartclient.com/license
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

package com.smartgwt.mobile.client.internal.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.core.Function;
import com.smartgwt.mobile.client.widgets.Canvas;
import com.smartgwt.mobile.client.widgets.layout.Layout;
import com.smartgwt.mobile.internal.gwt.dom.client.DOMConstants;
import com.smartgwt.mobile.internal.gwt.dom.client.SuperElement;
import com.smartgwt.mobile.internal.gwt.dom.client.event.TransitionEndEvent;
import com.smartgwt.mobile.internal.gwt.dom.client.event.TransitionEndHandler;

@SGWTInternal
public final class AnimationUtil {

    private static final int SLIDE_TRANSITION_DURATION_MILLIS = 350,
            FADE_TRANSITION_DURATION_MILLIS = 200;

    public static enum Direction {

        UP("up"),
        DOWN("down"),
        RIGHT("right"),
        LEFT("left"),
        NONE("none");

        static {
            UP.opposite = DOWN;
            DOWN.opposite = UP;
            RIGHT.opposite = LEFT;
            LEFT.opposite = RIGHT;
        }

        private final String value;
        private Direction opposite;

        private Direction(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public Direction getOppositeDirection() {
            return opposite;
        }
    }

    public static class AnimationProperty {

        private String propertyName;
        private String propertyNameForCSSText;
        private String startValueText;
        private String endValueText;

        public AnimationProperty(String propertyName, String startValueText, String endValueText) {
            this(propertyName, propertyName, startValueText, endValueText);
        }

        public AnimationProperty(String propertyName, String propertyNameForCSSText, String startValueText, String endValueText) {
            this.propertyName = propertyName;
            this.propertyNameForCSSText = propertyNameForCSSText;
            this.startValueText = startValueText;
            this.endValueText = endValueText;
        }

        public final String getPropertyName() {
            return propertyName;
        }

        public final String getPropertyNameForCSSText() {
            return propertyNameForCSSText;
        }

        public final String getStartValueText() {
            return startValueText;
        }

        public final String getEndValueText() {
            return endValueText;
        }
    }

    public static abstract class AnimationTimingFunction {

        protected AnimationTimingFunction() {}

        public abstract String getCSSText();
    }

    private static class BuiltInAnimationTimingFunction extends AnimationTimingFunction {

        private String functionName;

        public BuiltInAnimationTimingFunction(String functionName) {
            super();
            this.functionName = functionName;
        }

        @Override
        public final String getCSSText() {
            return functionName;
        }
    }

    public static final AnimationTimingFunction LINEAR = new BuiltInAnimationTimingFunction("linear"),
            EASE_IN = new BuiltInAnimationTimingFunction("ease-in"),
            EASE_OUT = new BuiltInAnimationTimingFunction("ease-out"),
            EASE_IN_OUT = new BuiltInAnimationTimingFunction("ease-in-out");

    public static interface AnimationRegistration {

        public boolean isPaused();
        public void setPaused(boolean paused);
        public boolean isFinished();
        public void finish();
    }

    private static final AnimationUtilStaticImpl IMPL = GWT.create(AnimationUtilStaticImpl.class);

    public static AnimationRegistration animate(Element element, int durationMillis, Function<Void> callback, AnimationProperty... properties) {
        return animate(element, properties, durationMillis, null, callback);
    }

    public static AnimationRegistration animate(Element element, int durationMillis, AnimationTimingFunction timingFunction, Function<Void> callback, AnimationProperty... properties) {
        return animate(element, properties, durationMillis, timingFunction, callback);
    }

    public static AnimationRegistration animate(Element element, AnimationProperty[] properties, int durationMillis, Function<Void> callback) {
        return animate(element, properties, durationMillis, null, callback);
    }

    public static AnimationRegistration animate(Element element, AnimationProperty[] properties, int durationMillis, AnimationTimingFunction timingFunction, Function<Void> callback) {
        return IMPL.animate(element, properties, durationMillis, timingFunction, callback);
    }

    /**
     * Flip-transition between the old and new widget.
     *
     * @param oldPane the old widget
     * @param newPane the new widget
     * @param container the container panel
     */
    public static void flipTransition(final Canvas oldPane, Canvas newPane, final Layout container) {
        Style oldStyle = oldPane.getElement().getStyle();
        Style newStyle = newPane.getElement().getStyle();

        oldStyle.setPosition(Style.Position.ABSOLUTE);
        newStyle.setPosition(Style.Position.ABSOLUTE);

        // flip the new element (instantly)
        newStyle.setProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName(), "none");
        newStyle.setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "rotateY(-180deg)");

        // set both to invisible when flipped
        oldStyle.setProperty(DOMConstants.INSTANCE.getBackfaceVisibilityPropertyName(), "hidden");
        newStyle.setProperty(DOMConstants.INSTANCE.getBackfaceVisibilityPropertyName(), "hidden");
        // will be initially invisible
        container.addMember(newPane);

        // set both to animate
        oldStyle.setProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName(), DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + " 0.3s ease-in-out");
        newStyle.setProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName(), DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + " 0.3s ease-in-out");

        // rotate old to 180, making it invisible since the back face is showing
        oldStyle.setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "rotateY(180deg)");
        // flip the new widget back to 0
        newStyle.setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "rotateY(0deg)");

        new Timer() {
            public void run() {
                container.removeMember(oldPane);
            }
        }.schedule(350);
    }

    public static void fadeTransition(final Canvas oldPane, final Canvas newPane, final Layout container) {
        fadeTransition(oldPane, newPane, container, null, null, true);
    }

    /**
     * Fade-transition between the old and new widget.
     *
     * @param oldPane the old widget
     * @param newPane the new widget
     * @param container the container panel
     */
    public static void fadeTransition(final Canvas oldPane, final Canvas newPane, final Canvas container, final Function<Void> beforeCallback, final Function<Void> callback, final boolean removeOldPane) {
        if (container instanceof Layout) {
            assert ((Layout)container).hasMember(oldPane) : "`container' does not contain `oldPane' as a member.";
        } else {
            assert container.hasChild(oldPane) : "`container' does not contain `oldPane' as a child.";
        }
        oldPane.setVisible(true);

        oldPane.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName(), "none");
        newPane.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName(), "none");
        oldPane.getElement().getStyle().setOpacity(1.0);
        if (container instanceof Layout) {
            if (!((Layout)container).hasMember(newPane)) ((Layout)container).addMember(newPane);
        } else {
            if (!container.hasChild(newPane)) container.addChild(newPane);
        }
        newPane.setVisible(true);
        newPane.getElement().getStyle().setOpacity(0.0);

        // To avoid issues with simultaneity, we use Timers.
        // http://www.w3.org/TR/css3-transitions/#starting
        new Timer() {
            @Override
            public void run() {
                if (beforeCallback != null) beforeCallback.execute();

                oldPane.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName(), "opacity " + FADE_TRANSITION_DURATION_MILLIS + "ms linear");
                newPane.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName(), "opacity " + FADE_TRANSITION_DURATION_MILLIS + "ms linear");

                new Timer() {
                    @Override
                    public void run() {
                        oldPane.getElement().getStyle().setOpacity(0.0);
                        newPane.getElement().getStyle().setOpacity(1.0);

                        new TransitionEndHandler() {
                            private Timer fallbackTimer = new Timer() {
                                @Override
                                public void run() {
                                    if (fallbackTimer == this) {
                                        fallbackTimer = null;
                                        execute();
                                    }
                                }
                            };

                            {
                                fallbackTimer.schedule(FADE_TRANSITION_DURATION_MILLIS + 50);
                            }

                            private HandlerRegistration transitionEndRegistration = oldPane.getElement().<SuperElement>cast().addTransitionEndHandler(this);

                            @Override
                            public void onTransitionEnd(TransitionEndEvent event) {
                                if (transitionEndRegistration != null &&
                                        oldPane.equals(event.getEventTarget()))
                                {
                                    execute();
                                }
                            }

                            private void execute() {
                                if (fallbackTimer != null) {
                                    fallbackTimer.cancel();
                                    fallbackTimer = null;
                                }
                                if (transitionEndRegistration != null) {
                                    transitionEndRegistration.removeHandler();
                                    transitionEndRegistration = null;
                                }

                                if (removeOldPane) {
                                    if (container instanceof Layout) {
                                        ((Layout)container).removeMember(oldPane);
                                    } else {
                                        container.removeChild(oldPane);
                                    }
                                } else oldPane.setVisible(false);

                                // Clear the transitions and opacity values.
                                // This is particularly important for oldPane because it may be subsequently added to
                                // a Layout, but the uncleared opacity would keep it invisible.
                                final Style oldPaneStyle = oldPane.getElement().getStyle();
                                oldPaneStyle.setProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName(), "none");
                                oldPaneStyle.clearProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName());
                                oldPaneStyle.clearOpacity();
                                final Style newPaneStyle = newPane.getElement().getStyle();
                                newPaneStyle.setProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName(), "none");
                                newPaneStyle.clearProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName());
                                newPaneStyle.clearOpacity();

                                if (callback != null) {
                                    new Timer() {
                                        @Override
                                        public void run() {
                                            assert callback != null;
                                            callback.execute();
                                        }
                                    }.schedule(1);
                                }
                            }
                        };
                    }
                }.schedule(1);
            }
        }.schedule(1);
    }

    /**
     * Fade-transition for a canvas.
     *
     * @param pane the widget
     * @param fadein fade in or out
     */
    public static void fadeTransition(final Canvas pane, boolean fadein) {
        if(fadein) {
            pane.getElement().getStyle().setOpacity(0.0);
            pane.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName(), "opacity 0.2s linear");
            new Timer() {
                public void run() {
                    pane.getElement().getStyle().setOpacity(1.0);
                }
            }.schedule(10);
        } else {
            pane.getElement().getStyle().setOpacity(1.0);
            pane.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName(), "opacity 0.2s linear");
            new Timer() {
                public void run() {
                    pane.getElement().getStyle().setOpacity(0.0);
                }
            }.schedule(10);
        }

    }

    public static void slideTransition(Canvas oldPane, Canvas newPane, Layout container, Direction direction) {
        slideTransition(oldPane, newPane, container, direction, null, null);
    }

    public static void slideTransition(Canvas oldPane, final Canvas newPane, final Layout container, final Direction direction, final Function<Void> beforeCallback, final Function<Void> afterCallback) {
        slideTransition(oldPane, newPane, container, direction, beforeCallback, afterCallback, true);
    }

    /**
     * Slide-transition between the old and new widget.
     *
     * @param oldPane the old widget
     * @param newPane the new widget
     * @param container the container panel
     * @param beforeCallback (optional) a callback to execute before the slide transition is started.
     * @param afterCallback (optional) a callback to execute after the slide transition completes.
     * @param removeOldPane <code>true</code> to remove <code>oldPane</code> from <code>container</code>;
     * <code>false</code> to hide it after the slide transition finishes.
     */
    public static void slideTransition(final Canvas oldPane, final Canvas newPane, final Layout container, final Direction direction, final Function<Void> beforeCallback, final Function<Void> afterCallback, final boolean removeOldPane) {
        if(Canvas.isAndroid()) {
            fadeTransition(oldPane, newPane, container, beforeCallback, afterCallback, removeOldPane);
            return;
        }

        assert container.hasMember(oldPane) : "`container' does not contain `oldPane' as a member.";
        container.showMember(oldPane);

        // Have `newPane' start off screen.
        newPane.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName(), "none");
        if (!container.hasMember(newPane)) container.addMember(newPane);
        else container.showMember(newPane);
        switch (direction) {
            case DOWN:
                newPane.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate3d(0, -100%, 0)");
                break;
            case UP:
                newPane.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate3d(0, 100%, 0)");
                break;
            case LEFT:
                newPane.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate3d(100%, 0, 0)");
                break;
            default:
            case RIGHT:
                newPane.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate3d(-100%, 0, 0)");
                break;
        }

        // To avoid issues with simultaneity, we use Timers.
        // http://www.w3.org/TR/css3-transitions/#starting
        new Timer() {
            @Override
            public void run() {
                if (beforeCallback != null) beforeCallback.execute();
                doSlideTransition(oldPane, newPane, container, direction, afterCallback, removeOldPane);
            }
        }.schedule(1);
    }

    private static void doSlideTransition(final Canvas oldPane, final Canvas newPane, final Layout container, final Direction direction, final Function<Void> callback, final boolean removeOldPane) {
        oldPane.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName(), DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + " " + SLIDE_TRANSITION_DURATION_MILLIS + "ms ease-in-out");
        newPane.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName(), DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + " " + SLIDE_TRANSITION_DURATION_MILLIS + "ms ease-in-out");

        new Timer() {
            @Override
            public void run() {
                switch (direction) {
                    case DOWN:
                        oldPane.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate3d(0, 100%, 0)");
                        newPane.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate3d(0, 0%, 0)");
                        break;
                    case UP:
                        oldPane.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate3d(0, -100%, 0)");
                        newPane.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate3d(0, 0%, 0)");
                        break;
                    case LEFT:
                        oldPane.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate3d(-100%, 0, 0)");
                        newPane.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate3d(0%, 0, 0)");
                        break;
                    case RIGHT:
                    default:
                        oldPane.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate3d(100%, 0, 0)");
                        newPane.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate3d(0%, 0, 0)");
                        break;
                }
                completeSlideTransition(oldPane, newPane, container, direction, callback, removeOldPane);
            }
        }.schedule(1);
    }

    private static void completeSlideTransition(final Canvas oldPane, final Canvas newPane, final Layout container, final Direction direction, final Function<Void> callback, final boolean removeOldPane) {
        new TransitionEndHandler() {
            private Timer fallbackTimer = new Timer() {
                @Override
                public void run() {
                    if (fallbackTimer == this) {
                        fallbackTimer = null;
                        execute();
                    }
                }
            };

            {
                fallbackTimer.schedule(SLIDE_TRANSITION_DURATION_MILLIS + 50);
            }

            private HandlerRegistration transitionEndRegistration = oldPane.getElement().<SuperElement>cast().addTransitionEndHandler(this);

            @Override
            public void onTransitionEnd(TransitionEndEvent event) {
                if (transitionEndRegistration != null &&
                        oldPane.equals(event.getEventTarget()))
                {
                    execute();
                }
            }

            private void execute() {
                if (fallbackTimer != null) {
                    fallbackTimer.cancel();
                    fallbackTimer = null;
                }
                if (transitionEndRegistration != null) {
                    transitionEndRegistration.removeHandler();
                    transitionEndRegistration = null;
                }

                if (removeOldPane) container.removeMember(oldPane);
                else container.hideMember(oldPane);

                // Clear the transitions and transforms.
                // This is particularly important for oldPane because it may be subsequently added to
                // a Layout, but the uncleared transform would place it off screen.
                final Style oldPaneStyle = oldPane.getElement().getStyle();
                oldPaneStyle.setProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName(), "none");
                oldPaneStyle.clearProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName());
                oldPaneStyle.setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "none");
                oldPaneStyle.clearProperty(DOMConstants.INSTANCE.getTransformPropertyName());
                final Style newPaneStyle = newPane.getElement().getStyle();
                newPaneStyle.setProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName(), "none");
                newPaneStyle.clearProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName());
                newPaneStyle.setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "none");
                newPaneStyle.clearProperty(DOMConstants.INSTANCE.getTransformPropertyName());

                if (callback != null) {
                    new Timer() {
                        @Override
                        public void run() {
                            assert callback != null;
                            callback.execute();
                        }
                    }.schedule(1);
                }
            }
        };
    }

    private AnimationUtil() {}
}
