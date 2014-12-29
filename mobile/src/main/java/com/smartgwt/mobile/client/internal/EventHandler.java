package com.smartgwt.mobile.client.internal;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.TouchEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.util.ElementUtil;
import com.smartgwt.mobile.client.widgets.Canvas;
import com.smartgwt.mobile.internal.gwt.dom.client.SuperDocument;
import com.smartgwt.mobile.internal.gwt.dom.client.SuperElement;

@SGWTInternal
public class EventHandler {

    static {
        captureEvents();

        Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                ++numResizes;
            }
        });
    }

    public static final String LONG_TOUCH_OKAY_CLASS_NAME = "sc-long-touch-okay";

    public static NativeEvent lastTouchStartEvent = null;

    public static Element lastClickedElem = null;

    public static int numResizes = 0;

    public static boolean couldShowSoftKeyboard(SuperElement element) {
        // Translate a LABEL element to its linked form element (if any).
        final String htmlFor;
        if (element != null && "LABEL".equals(element.getTagName()) &&
            (htmlFor = element.<LabelElement>cast().getHtmlFor()) != null &&
            !htmlFor.isEmpty())
        {
            element = Document.get().getElementById(htmlFor).cast();
        }

        // Make sure that the element is enabled.
        // Checking the `disabled' property is not sufficient:
        // http://www.whatwg.org/specs/web-apps/current-work/multipage/association-of-controls-and-forms.html#enabling-and-disabling-form-controls:-the-disabled-attribute
        if (element != null && element.matches(":enabled")) {
            final String tagName = element.getTagName();
            if ("INPUT".equals(tagName)) {
                final InputElement inputElement = element.cast();
                final String inputType = inputElement.getType();

                // On iPad, native date & time pickers are displayed as popovers.
                if (Canvas.isIPad() &&
                    ("date".equals(inputType) ||
                     "time".equals(inputType) ||
                     "datetime".equals(inputType) ||
                     "datetime-local".equals(inputType)))
                {
                    return false;
                }

                if (!inputElement.isReadOnly() &&
                    !"button".equals(inputType) &&
                    !"checkbox".equals(inputType) &&
                    !"file".equals(inputType) &&
                    !"hidden".equals(inputType) &&
                    !"image".equals(inputType) &&
                    !"radio".equals(inputType) &&
                    !"reset".equals(inputType) &&
                    !"submit".equals(inputType))
                {
                    return true;
                }
            } else if ("SELECT".equals(tagName)) {
                // On iPad, the native picker is displayed as a popover.
                return !Canvas.isIPad();
            } else if ("TEXTAREA".equals(tagName)) {
                final TextAreaElement textareaElement = element.cast();
                if (!textareaElement.isReadOnly()) return true;
            }
        }

        return false;
    }

    public static boolean couldShowSoftKeyboard(NativeEvent event) {
        SuperElement element;
        if ("focusout".equals(event.getType())) {
            // For the 'focusout' event type, the related target is the event target receiving
            // focus: https://developer.mozilla.org/en-US/docs/Web/Reference/Events/focusout
            element = EventUtil.getElement(event.getRelatedEventTarget()).<SuperElement>cast();
        } else {
            element = EventUtil.getElement(event.getEventTarget()).<SuperElement>cast();
        }

        return couldShowSoftKeyboard(element);
    }

    public static native boolean isFastClickEvent(NativeEvent event) /*-{
        return !!event._fastClick;
    }-*/;

    static void captureEvents() {
        Event.addNativePreviewHandler(new NativePreviewHandler() {

            private boolean trackingClick = false;
            private int clientX, clientY;
            private Long startTime = null;
            private Timer longTouchTimer = null;

            private boolean isLongTouchOkay(Element element) {
                for (; element != null; element = element.getParentElement()) {
                    if (ElementUtil.hasClassName(element, LONG_TOUCH_OKAY_CLASS_NAME)) return true;
                }
                return false;
            }

            // On iOS, it is important to ignore the browser's events on text-type <input>, <select>, and <textarea> elements
            // because programmatically-created MouseEvents dispatched to those types of input elements
            // do NOT cause the virtual keyboard to pop open and do not give the input element focus.
            // This means that it is impossible to avoid the ~200ms delay between user tapping on a text input
            // and the input receiving keyboard focus.
            // See http://www.quora.com/Mobile-Safari-iPhone-or-iPad-with-JavaScript-how-can-I-launch-the-on-screen-keyboard for
            // background information.
            //
            // Programmatic click events are known to work on button and checkbox INPUTs.
            // It is likely that they also work on radio and reset INPUTs, but this theory has
            // not been tested yet.
            private boolean shouldIgnore(Element targetElem) {
                if (targetElem == null) return true;

                final String tagName = targetElem.getTagName();
                if ("INPUT".equals(tagName)) {
                    final String inputType = targetElem.<InputElement>cast().getType();

                    // For a submit button, we need to ignore 'click' events on it because if
                    // the click event originates from tapping the return key on the soft keyboard
                    // ("Go"/"Search" button), then canceling the click causes the submit event
                    // not to fire.
                    //
                    // However, if the submit button was the last 'touchstart' event target,
                    // then we want fast click because there is too noticeable a delay between
                    // tapping the submit button and the submit event being fired. A synthetic
                    // 'click' event on the submit button *does* result in form submission.
                    if ("submit".equals(inputType)) {
                        final JsArray<Touch> lastTouchStartTouches;
                        final Element lastTouchStartTarget;
                        if (lastTouchStartEvent != null &&
                            (lastTouchStartTouches = lastTouchStartEvent.getTouches()) != null &&
                            lastTouchStartTouches.length() == 1 &&
                            (lastTouchStartTarget = EventUtil.getElement(lastTouchStartTouches.get(0).getTarget())) != null &&
                            // the submit button is not the last 'touchstart' target
                            !targetElem.isOrHasChild(lastTouchStartTarget))
                        {
                            return true;
                        }
                    } else if (!"button".equals(inputType) &&
                               !"checkbox".equals(inputType) &&
                               !"file".equals(inputType))
                    {
                        return true;
                    }
                } else if ("SELECT".equals(tagName) ||
                           "TEXTAREA".equals(tagName))
                {
                    return true;
                } else if ("LABEL".equals(tagName)) {
                    if (!Canvas._isIOS4OrOlder()) {
                        return true;
                    }
                }

                return false;
            }

            private void cancelTracking() {
                if (longTouchTimer != null) {
                    longTouchTimer.cancel();
                    longTouchTimer = null;
                }
                trackingClick = false;
            }

            private void onTouchStart(NativePreviewEvent previewEvent) {
                final NativeEvent event = previewEvent.getNativeEvent();
                final JsArray<Touch> touches = event.getTouches();
                final int clientX, clientY;
                if (touches != null) {
                    if (touches.length() != 1) {
                        cancelTracking();
                        return;
                    }
                    final Touch touch = touches.get(0);
                    clientX = touch.getClientX();
                    clientY = touch.getClientY();
                } else {
                    clientX = event.getClientX();
                    clientY = event.getClientY();
                }
                this.clientX = clientX;
                this.clientY = clientY;

                final Element targetElem = EventUtil.getTargetElem(event);
                if (isLongTouchOkay(targetElem)) {
                    startTime = null;
                } else {
                    startTime = System.currentTimeMillis();
                }
                trackingClick = true;
                if (targetElem != null) {
                    longTouchTimer = new Timer() {

                        @Override
                        public void run() {
                            if (longTouchTimer == this) {
                                longTouchTimer = null;
                                if (trackingClick) {
                                    // http://stackoverflow.com/questions/433919/javascript-simulate-right-click-through-code
                                    final NativeEvent contextMenuEvent = Document.get().createMouseEvent("contextmenu", true, true, 1, 0, 0, clientX, clientY, false, false, false, false, NativeEvent.BUTTON_RIGHT, null);
                                    targetElem.dispatchEvent(contextMenuEvent);
                                }
                            }
                        }
                    };
                    longTouchTimer.schedule(250);
                }
            }

            private void onTouchMove(NativePreviewEvent previewEvent) {
                if (! trackingClick) return;

                if (startTime != null && System.currentTimeMillis() - startTime.longValue() > 500L) {
                    cancelTracking();
                    return;
                }

                NativeEvent event = previewEvent.getNativeEvent();
                final JsArray<Touch> touches = event.getTouches();
                final int clientX, clientY;
                if (touches != null) {
                    if (touches.length() != 1) {
                        cancelTracking();
                        return;
                    }
                    final Touch touch = touches.get(0);
                    clientX = touch.getClientX();
                    clientY = touch.getClientY();
                } else {
                    clientX = event.getClientX();
                    clientY = event.getClientY();
                }

                final int deltaX = clientX - this.clientX,
                        deltaY = clientY - this.clientY;
                if (10 < Math.abs(deltaX) || 10 < Math.abs(deltaY)) {
                    cancelTracking();
                    return;
                }
            }

            private native void markFastClick(NativeEvent event) /*-{
                event._fastClick = true;
            }-*/;

            private void onTouchEnd(NativePreviewEvent previewEvent) {
                if (! trackingClick) return;
                cancelTracking();

                Long timeDiff = null;
                if (startTime != null) {
                    timeDiff = Long.valueOf(System.currentTimeMillis() - startTime.longValue());
                    if (timeDiff.longValue() > 500L) return;
                }

                NativeEvent event = previewEvent.getNativeEvent();
                final JsArray<Touch> touches = event.getChangedTouches();
                final int clientX, clientY;
                if (touches != null) {
                    if (touches.length() != 1) return;
                    final Touch touch = touches.get(0);
                    clientX = touch.getClientX();
                    clientY = touch.getClientY();
                } else {
                    clientX = event.getClientX();
                    clientY = event.getClientY();
                }

                int deltaX = clientX - this.clientX,
                        deltaY = clientY - this.clientY;
                if (10 < Math.abs(deltaX) || 10 < Math.abs(deltaY)) {
                    return;
                }

                final Element targetElem;
                if (Canvas.useIOSNativeScrolling()) {
                    // When an element is natively scrolled, the target of the 'touchend' event
                    // is way off. To work around this issue, use the elementFromPoint() method
                    // of `document' to calculate the real element target of the event.
                    // https://developer.mozilla.org/en-US/docs/DOM/document.elementFromPoint
                    // http://gototen.org/2011/12/08/unreliable-touchend-events-on-ios5/
                    //
                    // FIXME elementFromPoint() is no longer a work-around for this issue on iOS 7, 7.1
                    // *on iPad* (iPhone running iOS 7, 7.1, iPad running iOS 6.0, 6.1 are fine).
                    // See the Widgets > TableViews > Grouped Tables Showcase sample on iPad running
                    // iOS 7.x. Start a long drag and during the momentum scroll, tap on the screen -
                    // the popup dialog on screen is for the wrong row (it's the row that would have been
                    // the target of the event at the start of the momentum scroll). However, if you
                    // let the momentum scroll "bounce back" at one of the ends, then the issue disappears.
                    final SuperDocument document = Document.get().cast();
                    targetElem = document.getElementFromPoint(clientX, clientY);
                } else {
                    targetElem = EventUtil.getTargetElem(event);
                }
                if (!shouldIgnore(targetElem)) {
                    if (timeDiff == null || timeDiff.longValue() < 300L) {
                        final NativeEvent clickEvent = Document.get().createClickEvent(1, 0, 0, this.clientX, this.clientY, false, false, false, false);
                        markFastClick(clickEvent);
                        new Timer() {
                            @Override
                            public void run() {
                                targetElem.dispatchEvent(clickEvent);
                                lastClickedElem = targetElem;
                            }
                        }.schedule(1);
                    }
                } else lastClickedElem = targetElem;
            }

            @Override
            public void onPreviewNativeEvent(NativePreviewEvent previewEvent) {
                if (previewEvent == null) return;
                final int eventType = previewEvent.getTypeInt();
                final NativeEvent event = previewEvent.getNativeEvent();
                final EventTarget eventTarget = event.getEventTarget();
                if (TouchEvent.isSupported()) {
                    switch (eventType) {
                        case Event.ONTOUCHSTART:
                            // Mobile browsers can give <input> and <textarea> elements keyboard focus if they
                            // are moved in the way. One work-around is to save this touchstart event for later.
                            // When a text input element receives focus, check if the target of `lastTouchStartEvent'
                            // is the input element. If not, call blur() to give up keyboard focus.
                            // See: http://stackoverflow.com/questions/11064680/is-this-a-bug-tapping-an-element-can-result-in-a-different-element-receiving-keyboard-focus
                            lastTouchStartEvent = previewEvent.getNativeEvent();

                            onTouchStart(previewEvent);
                            return;
                        case Event.ONTOUCHMOVE:
                            onTouchMove(previewEvent);
                            return;
                        case Event.ONTOUCHEND:
                            onTouchEnd(previewEvent);

                            if (Element.is(eventTarget) &&
                                !shouldIgnore(eventTarget.<Element>cast()))
                            {
                                event.preventDefault();
                            }
                            return;
                        case Event.ONTOUCHCANCEL:
                            trackingClick = false;
                            return;
                    }
                } else {
                    switch (eventType) {
                        case Event.ONMOUSEDOWN:
                            onTouchStart(previewEvent);
                            return;
                        case Event.ONMOUSEMOVE:
                            onTouchMove(previewEvent);
                            return;
                        case Event.ONMOUSEUP:
                            onTouchEnd(previewEvent);
                            return;
                    }
                }
                if (eventType == Event.ONCLICK) {
                    if (!isFastClickEvent(event) &&
                        Element.is(eventTarget) &&
                        !shouldIgnore(eventTarget.<Element>cast()))
                    {
                        // Reset `lastTouchStartEvent'. This is needed for iOS 4.3.2, which does
                        // not fire touch events on input elements: http://jsfiddle.net/3zjQW/1/
                        if (Canvas._isIOS4OrOlder()) {
                            lastTouchStartEvent = null;
                        }

                        previewEvent.cancel();
                        return;
                    }
                }
            }
        });
    }

    public static void maybeInit() {}
}

