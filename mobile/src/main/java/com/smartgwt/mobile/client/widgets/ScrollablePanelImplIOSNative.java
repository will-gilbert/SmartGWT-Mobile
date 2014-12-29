package com.smartgwt.mobile.client.widgets;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.smartgwt.mobile.client.util.Page;
import com.smartgwt.mobile.client.util.events.OrientationChangeEvent;
import com.smartgwt.mobile.client.util.events.OrientationChangeHandler;
import com.smartgwt.mobile.internal.gwt.dom.client.DOMConstants;

class ScrollablePanelImplIOSNative extends ScrollablePanelImpl {

    private HandlerRegistration orientationChangeRegistration;
    private boolean preventNativeScrolling = false;
    private int prevScrollTop = 1;
    private Timer reapplyTouchOverflowScrollingTimer;

    @Override
    void init(ScrollablePanel self) {
        self.sinkEvents(Event.ONTOUCHSTART | Event.ONTOUCHMOVE);
    }

    @Override
    void destroy(ScrollablePanel self) {
        if (orientationChangeRegistration != null) {
            orientationChangeRegistration.removeHandler();
            orientationChangeRegistration = null;
        }
        super.destroy(self);
    }

    @Override
    void onLoad(final ScrollablePanel self) {
        super.onLoad(self);
        self.getElement().setScrollTop(prevScrollTop);
        self.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getOverflowScrollingPropertyName(), "touch");

        orientationChangeRegistration = Page.addOrientationChangeHandler(new OrientationChangeHandler() {
            @Override
            public void onOrientationChange(OrientationChangeEvent event) {
                ScrollablePanelImplIOSNative.this.onOrientationChange(self);
            }
        });
    }

    @Override
    void onUnload(ScrollablePanel self) {
        if (reapplyTouchOverflowScrollingTimer != null) {
            reapplyTouchOverflowScrollingTimer.cancel();
            reapplyTouchOverflowScrollingTimer = null;
        }
        if (orientationChangeRegistration != null) {
            orientationChangeRegistration.removeHandler();
            orientationChangeRegistration = null;
        }
        prevScrollTop = self.getElement().getScrollTop();
        self.getElement().getStyle().clearProperty(DOMConstants.INSTANCE.getOverflowScrollingPropertyName());
        super.onUnload(self);
    }

    @Override
    void reset(ScrollablePanel self) {
        self.getElement().setScrollTop(0);
    }

    // Use Joe Lambert's ScrollFix technique, slightly modified
    // http://blog.joelambert.co.uk/2011/10/14/a-fix-for-ios5-overflow-scrolling-scrollfix-js/
    @Override
    void onBrowserEvent(ScrollablePanel self, Event event) {
        self.superOnBrowserEvent(event);
        final Element elem = self.getElement();
        switch (event.getTypeInt()) {
            case Event.ONTOUCHSTART:
                final int scrollTop = elem.getScrollTop();
                if (scrollTop <= 0) elem.setScrollTop(1);
                else if (scrollTop + elem.getClientHeight() >= elem.getScrollHeight()) {
                    elem.setScrollTop(elem.getScrollHeight() - elem.getClientHeight() - 1);
                }

                // Prevent the screen from shifting up and down if there is no overflow content to scroll.
                // https://github.com/joelambert/ScrollFix/pull/6
                // https://github.com/albertogasparin/ScrollFix/commit/d6200f040b30f6aab6fe9cec4773ab36613af595
                preventNativeScrolling = elem.getScrollHeight() <= elem.getClientHeight();
                break;

            case Event.ONTOUCHMOVE:
                if (preventNativeScrolling ||
                        !"touch".equals(self.getElement().getStyle().getProperty(DOMConstants.INSTANCE.getOverflowScrollingPropertyName())))
                {
                    event.preventDefault();
                }
                break;
        }
    }

    @Override
    void onContentChange(ScrollablePanel self) {
        final Element elem = self.getElement();
        elem.setScrollLeft(elem.getScrollLeft());
        elem.setScrollTop(elem.getScrollTop());
    }

    private void onOrientationChange(ScrollablePanel self) {
        onContentChange(self);

        // Changing the orientation of an iOS device can lock up native scrolling. To fix, clear
        // and reapply -webkit-overflow-scrolling:touch. This is known to affect iOS 6, 6.1, 7.
        //
        // Note that reapplying -webkit-overflow-scrolling:touch must be done after a delay
        // (even the minimum "1ms" delay works).
        //
        // One problem is that clearing and reapplying -webkit-overflow-scrolling:touch introduces
        // flickering of the ScollablePanel's contents. One way of reducing the amount of
        // flickering is to only reapply -webkit-overflow-scrolling:touch if the scrollHeight
        // is greater than the clientHeight (and thus scrolling is necessary), but this first
        // requires handling of the internal 'scContentChanged' event in case content is added
        // to the ScrollablePanel afterward and scrolling becomes necessary.
        final Element elem = self.getElement();
        elem.getStyle().clearProperty(DOMConstants.INSTANCE.getOverflowScrollingPropertyName());
        if (reapplyTouchOverflowScrollingTimer == null) {
            reapplyTouchOverflowScrollingTimer = new Timer() {
                @Override
                public void run() {
                    assert this == reapplyTouchOverflowScrollingTimer;
                    reapplyTouchOverflowScrollingTimer = null;
                    elem.getStyle().setProperty(DOMConstants.INSTANCE.getOverflowScrollingPropertyName(), "touch");
                }
            };
            reapplyTouchOverflowScrollingTimer.schedule(1);
        }
    }
}
