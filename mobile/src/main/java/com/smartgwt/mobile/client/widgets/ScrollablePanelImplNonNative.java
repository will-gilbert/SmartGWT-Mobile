package com.smartgwt.mobile.client.widgets;

import java.util.Date;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.smartgwt.mobile.client.internal.types.AndroidWindowSoftInputMode;
import com.smartgwt.mobile.client.theme.ScrollablePanelCssResource;
import com.smartgwt.mobile.internal.gwt.dom.client.ClientRect;
import com.smartgwt.mobile.internal.gwt.dom.client.DOMConstants;
import com.smartgwt.mobile.internal.gwt.dom.client.SuperDocument;
import com.smartgwt.mobile.internal.gwt.dom.client.SuperElement;

class ScrollablePanelImplNonNative extends ScrollablePanelImpl {

    private enum Direction {
        north,
        south
    };

    private static final ScrollablePanelCssResource CSS = ScrollablePanel._CSS;

    private boolean active = false, scrolled = false, scrollable = false, scrolling = false, mouseWheelActive = false;
    private int height, verticalTrackHeight, startY = 0, lastY = 0, totalY = 0, wrapperHeight = 0;
    private long startTime = 0L, lastMouseWheelEventTime;
    private float barProp, deceleration = 0.001F;
    private Direction direction;
    private Event startEvent;
    private Timer endTimer;
    private HandlerRegistration resizeRegistration;

    // The thin area at the very right of the ScrollablePanel that spans top to bottom.
    private Canvas verticalTrack;

    // The visual indicator within the scrollbar of the current scroll position (the dark gray
    // rounded rectangle).
    private Canvas verticalThumb;

    @Override
    void init(ScrollablePanel self) {
        verticalTrack = new Canvas(Document.get().createDivElement());
        verticalTrack._setClassName(CSS.verticalScrollbarTrackClass(), false);
        verticalThumb = new Canvas(Document.get().createDivElement());
        verticalThumb._setClassName(CSS.verticalScrollbarThumbClass(), false);
        verticalTrack.addChild(verticalThumb);
        self.add(verticalTrack, self.getElement());
        self.sinkEvents(Event.GESTUREEVENTS | Event.TOUCHEVENTS | Event.MOUSEEVENTS | Event.FOCUSEVENTS | Event.ONMOUSEWHEEL | Event.ONSCROLL | Event.KEYEVENTS);
        self._sinkContentChangedEvent();
        self._sinkRequestScrollToEvent();
    }

    @Override
    void destroy(ScrollablePanel self) {
        if (endTimer != null) {
            endTimer.cancel();
            endTimer = null;
        }
        if (resizeRegistration != null) {
            resizeRegistration.removeHandler();
            resizeRegistration = null;
        }
        super.destroy(self);
    }

    @Override
    void onLoad(final ScrollablePanel self) {
        super.onLoad(self);
        onContentChange(self);

        // In the "Android Developers" group, topic "How to detect the event when the soft keyboard hides?"
        // https://groups.google.com/d/topic/android-developers/HDKR8V7ZwEQ/discussion
        // quill on May 20, 2009:
        // > Hey everyone,
        // > I want to do something when the soft keyboard hide. Is there any
        // > function to detect that event?
        // > Thanks!
        //
        // Dianne Hackborn:
        // > No, you only know because your content view is resized.
        //
        //
        // Topic "How to Detect Soft Keyboard / Resize?"
        // https://groups.google.com/d/topic/android-developers/el00Xnz6zR4/discussion
        // Rob on May 7, 2009:
        // > Hi,
        // >
        // > How can you detect when the soft keyboard is enabled and the layout
        // > resized?
        // >
        // > Thanks.
        //
        // Yusuf Saib:
        // > I have bad news and good news. There is no direct API to detect this.
        // > But you can detect if your layout is forcibly changed by Android due
        // > to the soft keyboard [dis]appearing, which you can detect.
        //
        //
        // http://stackoverflow.com/questions/8436505/android-detect-visible-keyboard
        // http://stackoverflow.com/questions/3081276/android-detect-softkeyboard-open
        // http://stackoverflow.com/questions/5457486/is-there-a-way-to-tell-if-the-soft-keyboard-is-shown
        //
        // Issue 56420: Fullscreen activity prevents resize event in WebView/WebKit
        // http://code.google.com/p/android/issues/detail?id=56420
        resizeRegistration = Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                new Timer() {
                    @Override
                    public void run() {
                        if (!self.isAttached()) return;

                        // Always run onContentChange() on resize.
                        //
                        // It used to be that onContentChange() was not called if there was an active
                        // element and android.windowSoftInputMode was 'adjustResize', but this led
                        // to an issue where following the instructions at http://forums.smartclient.com/showpost.php?p=107051&postcount=11
                        // and then hiding the soft keyboard *while keeping focus in the SearchItem*
                        // reproduced the bug. Ways of hiding the keyboard while retaining focus within
                        // the item include:
                        // - Tapping the soft keyboard's Go button.
                        // - Pressing the device's back button.
                        // - Open device settings. In the emulator: While the keyboard is on screen,
                        //   press and hold the tiny keyboard icon at the top left and drag it down
                        //   all the way to the bottom. Then tap the square-shaped button at the top
                        //   right, followed by "BRIGHTNESS".
                        onContentChange(self);

                        if (AndroidWindowSoftInputMode.ADJUST_RESIZE == Canvas._getAndroidWindowSoftInputMode()) {
                            final SuperDocument doc = Document.get().<SuperDocument>cast();
                            final SuperElement activeElement = doc.getActiveElement();
                            if (activeElement != null && activeElement != doc.getBody().<SuperElement>cast() &&
                                    // It is important to check that we actually have the activeElement as a
                                    // descendant because we are going to do the same thing *as if* we received
                                    // an 'scRequestResizeTo' event. But, we only receive 'scRequestResizeTo'
                                    // events if the event target is a descendant.
                                    //
                                    // Do this check last because it's relatively expensive.
                                    self.getElement().isOrHasChild(activeElement))
                            {
                                onRequestScrollTo(self, activeElement);
                            }
                        }
                    }
                }.schedule(250);
            }
        });
    }

    @Override
    void onUnload(ScrollablePanel self) {
        if (resizeRegistration != null) {
            resizeRegistration.removeHandler();
            resizeRegistration = null;
        }
        super.onUnload(self);
    }

    private void calculateScrollBarHeights(ScrollablePanel self) {
        verticalTrackHeight = self.getElement().getClientHeight();
        int thumbHeight = Math.round(((float)verticalTrackHeight) * verticalTrackHeight / height);
        verticalThumb.getElement().getStyle().setHeight(thumbHeight, Unit.PX);
        int barMaxScroll = verticalTrackHeight - thumbHeight - 4;
        final int maxScrollTop = height - verticalTrackHeight;
        barProp = ((float)barMaxScroll) / maxScrollTop;
    }

    private int getVerticalThumbPos() {
        return Math.round(barProp * -totalY);
    }

    @Override
    void reset(ScrollablePanel self) {
        scrollTo(self, 0);
    }

    @Override
    void onBrowserEvent(ScrollablePanel self, Event event) {
        self.superOnBrowserEvent(event);
        switch (event.getTypeInt()) {
            case Event.ONMOUSEWHEEL:
                if (!mouseWheelActive) {
                    mouseWheelActive = true;
                    onStart(self, event);
                } else {
                    onMouseWheelMove(self, event);
                }
                break;
            case Event.ONMOUSEDOWN:
                if (!self.touched) {
                    onStart(self, event);
                }
                break;
            case Event.ONTOUCHSTART:
            case Event.ONGESTURESTART:
                self.touched = true;
                onStart(self, event);
                break;
            case Event.ONMOUSEMOVE:
                if (!self.touched) {
                    onMove(self, event);
                }
                break;
            case Event.ONTOUCHMOVE:
            case Event.ONGESTURECHANGE:
            case Event.ONSCROLL:
                onMove(self, event);
                break;
            case Event.ONMOUSEUP:
                if (!self.touched) {
                    onEnd(self, event);
                }
                break;
            case Event.ONTOUCHEND:
            case Event.ONTOUCHCANCEL:
            case Event.ONGESTUREEND:
                onEnd(self, event);
                break;
        }

        if (Canvas._CONTENT_CHANGED_EVENT_TYPE.equals(event.getType())) {
            //SC.logWarn("ScrollablePanelImplNonNative received an 'scContentChanged' event");
            onContentChange(self);
        } else if (Canvas._REQUEST_SCROLL_TO_EVENT_TYPE.equals(event.getType())) {
            final EventTarget eventTarget = event.getEventTarget();
            if (Element.is(eventTarget)) {
                event.stopPropagation(); // We're going to handle it.
                onRequestScrollTo(self, eventTarget.<Element>cast());
            }
        }
    }

    @Override
    native int getScrollTop(ScrollablePanel self) /*-{
        var element = self.@com.smartgwt.mobile.client.widgets.ScrollablePanel::_getInnerElement()();
        var computedStyle = $wnd.getComputedStyle(element, null);
        var transformValue = computedStyle[@com.smartgwt.mobile.internal.gwt.dom.client.DOMConstants::INSTANCE.@com.smartgwt.mobile.internal.gwt.dom.client.DOMConstants::getTransformPropertyName()()];
        if (transformValue != null) {
            var parts = $wnd.String(transformValue).replace(/[^0-9-.,]/g, '').split(',');
            if (parts.length == 16) {
                var ret = -$wnd.parseFloat(parts[7]);
                if (!$wnd.isNaN(ret)) {
                    return $wnd.Math.round(ret);
                }
            } else if (parts.length == 6) {
                // A 2D 3x2 matrix
                var ret = -$wnd.parseFloat(parts[5]);
                if (!$wnd.isNaN(ret)) {
                    return $wnd.Math.round(ret);
                }
            }
        }
        return 0;
    }-*/;

    private void onStart(ScrollablePanel self, Event event) {
        if (!self.isEnabled()) return;
        if (!active) {
            self._getInnerElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName(), "none");
            lastY = (event.getTouches() != null && event.getTouches().length() > 0) ? event.getTouches().get(0).getClientY(): event.getClientY();
            height = self._getInnerElement().getOffsetHeight();
            wrapperHeight = self.getElement().getOffsetHeight();
            if (wrapperHeight < height) {
                calculateScrollBarHeights(self);
                active = true;
                scrollable = true;
                startEvent = event;
                startTime = new Date().getTime();
                final int computedY = -getScrollTop(self);
                if (computedY != totalY) {
                    totalY = computedY;
                    if (Canvas.isAndroid()) {
                        self._getInnerElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate(0px, " + totalY + "px)");
                    } else {
                        self._getInnerElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate3d(0px, " + totalY + "px, 0px)");
                    }
                    final int verticalThumbPos = getVerticalThumbPos();
                    if (Canvas.isAndroid()) {
                        verticalThumb.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate(0px, " + verticalThumbPos + "px)");
                    } else {
                        verticalThumb.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate3d(0px, " + verticalThumbPos + "px, 0px)");
                    }
                    verticalTrack.getElement().getStyle().setOpacity(0);
                    scrolled = true;
                    scrolling = true;
                }
            }
        }
    }

    private void onMove(ScrollablePanel self, Event event) {
        if (!self.isEnabled()) return;
        if (active) {
            if (scrollable) {
                scrolled = true;
                int Y = (event.getTouches() != null && event.getTouches().length() > 0) ? event.getTouches().get(0).getClientY(): event.getClientY();
                int deltaY = Y - lastY;
                lastY = Y;
                totalY += deltaY;
                direction = deltaY < 0 ? Direction.south : Direction.north;
                verticalTrack.getElement().getStyle().setOpacity(1);
                if (Canvas.isAndroid()) {
                    self._getInnerElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate(0px, " + totalY + "px)");
                } else {
                    self._getInnerElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate3d(0px, " + totalY + "px, 0)");
                }
                final int verticalThumbPos = getVerticalThumbPos();
                if (Canvas.isAndroid()) {
                    verticalThumb.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate(0px, " + verticalThumbPos + "px)");
                } else {
                    verticalThumb.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate3d(0px, " + verticalThumbPos + "px, 0px)");
                }
            }
        }
    }

    private void onEnd(final ScrollablePanel self, Event event) {
        if (!self.isEnabled()) return;
        if (active && scrollable && scrolled && !scrolling) {
            boolean pastEnd = false;
            int duration = (int)(new Date().getTime() - startTime);
            int distance = Math.abs(totalY - startY);
            float speed = ((float)distance) / duration;
            int newDistance = Math.round((speed * speed) / (2 * deceleration));
            int newTime = Math.round(speed / deceleration);
            totalY += direction == Direction.north ? newDistance : -newDistance;
            final int minTotalY = height < wrapperHeight ? 0 : wrapperHeight - height,
                    computedY = Math.min(Math.max(minTotalY, totalY), 0);
            if (totalY != computedY) {
                totalY = computedY;
                newTime = 500;
                pastEnd = true;
            }
            if (pastEnd || speed >= 0.3F) {
                final int verticalThumbPos = getVerticalThumbPos();
                self._getInnerElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName(), DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + " " + newTime + "ms cubic-bezier(0.33,0.66,0.66,1)");
                verticalThumb.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName(), DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + " " + newTime + "ms cubic-bezier(0.33,0.66,0.66,1)");
                if (Canvas.isAndroid()) {
                    self._getInnerElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate(0px, " + totalY + "px)");
                    verticalThumb.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate(0px, " + verticalThumbPos + "px)");
                } else {
                    self._getInnerElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate3d(0px, " + totalY + "px, 0px)");
                    verticalThumb.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate3d(0px, " + verticalThumbPos + "px, 0px)");
                }
            }
            endTimer = new Timer() {
                @Override
                public void run() {
                    if (endTimer != this) return;
                    self._getInnerElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName(), "none 0s");
                    verticalThumb.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName(), "none 0s");
                    verticalTrack.getElement().getStyle().setOpacity(0);
                }
            };
            endTimer.schedule(newTime + 20);
        }
        active = false;
        scrolled = false;
        scrolling = false;
        scrollable = false;
        startEvent = null;
        startY = totalY;
        startTime = 0;
    }

    private void onMouseWheelMove(final ScrollablePanel self, final Event event) {
        if (!self.isEnabled()) return;
        if (active) {
            if (scrollable) {
                scrolled = true;
                int deltaY = self.getWheelDelta(event);
                lastY += deltaY;
                totalY += deltaY;
                direction = deltaY < 0 ? Direction.south : Direction.north;
                verticalTrack.getElement().getStyle().setOpacity(1);
                final int verticalThumbPos = getVerticalThumbPos();
                if (Canvas.isAndroid()) {
                    self._getInnerElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate(0px, " + totalY + "px)");
                    verticalThumb.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate(0px, " + verticalThumbPos + "px)");
                } else {
                    self._getInnerElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate3d(0px, " + totalY + "px, 0px)");
                    verticalThumb.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate3d(0px, " + verticalThumbPos + "px, 0px)");
                }
                lastMouseWheelEventTime = new Date().getTime();
                new Timer() {
                    @Override
                    public void run() {
                        if (mouseWheelActive) {
                            long currentTime = new Date().getTime();
                            final int mouseWheelDeltaTime = new Long(currentTime - lastMouseWheelEventTime).intValue();
                            if (mouseWheelDeltaTime > 80) {
                                onEnd(self, event);
                                mouseWheelActive = false;
                            }
                        }
                    }
                }.schedule(80);
            }
        }
    }

    @Override
    void onContentChange(ScrollablePanel self) {
        scrollTo(self, -totalY);
    }

    private void onRequestScrollTo(final ScrollablePanel self, Element targetElement) {
        if (targetElement == null) return;

        final ClientRect targetBCR = targetElement.<SuperElement>cast().getBoundingClientRect().getBorderRect(5),
                wrapperBCR = self.getElement().<SuperElement>cast().getBoundingClientRect();
        if (!(wrapperBCR.getTop() <= targetBCR.getTop() &&
                targetBCR.getBottom() <= wrapperBCR.getBottom()))
        {
            final double d;
            if ((targetBCR.getTop() <= wrapperBCR.getTop()) ||
                    (wrapperBCR.getHeight() <= targetBCR.getHeight()))
            {
                d = targetBCR.getTop() - wrapperBCR.getTop();
            } else {
                d = targetBCR.getBottom() - wrapperBCR.getBottom();
            }
            // Call scrollTo() after a timeout to prevent a problem seen in Android 4.2.2 emulator
            // that the blinking editor caret can disappear if we call scrollTo() immediately.
            new Timer() {
                @Override
                public void run() {
                    scrollTo(self, getScrollTop(self) + (int)d);
                }
            }.schedule(1);
        }
    }

    private void scrollTo(ScrollablePanel self, int scrollTop) {
        height = self._getInnerElement().getOffsetHeight();
        wrapperHeight = self.getElement().getOffsetHeight();
        calculateScrollBarHeights(self);
        final int minTotalY = height < wrapperHeight ? 0 : wrapperHeight - height;
        final int computedY = Math.min(Math.max(minTotalY, -scrollTop), 0);
        //SC.logWarn("scrollTop = " + scrollTop + ", height = " + height + ", wrapperHeight = " + wrapperHeight + ", minTotalY = " + minTotalY + ", computedY = " + computedY);

        self._getInnerElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName(), "none 0s");
        verticalThumb.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName(), "none 0s");
        verticalTrack.getElement().getStyle().setOpacity(0);

        totalY = computedY;
        if (Canvas.isAndroid()) {
            self._getInnerElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate(0px, " + totalY + "px)");
        } else {
            self._getInnerElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate3d(0px, " + totalY + "px, 0px)");
        }
        final int verticalThumbPos = getVerticalThumbPos();
        if (Canvas.isAndroid()) {
            verticalThumb.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate(0px, " + verticalThumbPos + "px)");
        } else {
            verticalThumb.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate3d(0px, " + verticalThumbPos + "px, 0px)");
        }

        active = false;
        scrolled = false;
        scrolling = false;
        scrollable = false;
        startEvent = null;
        startY = totalY;
        startTime = 0;
        if (endTimer != null) {
            endTimer.cancel();
            endTimer = null;
        }
    }
}
