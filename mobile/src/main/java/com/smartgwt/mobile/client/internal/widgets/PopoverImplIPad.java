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

package com.smartgwt.mobile.client.internal.widgets;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Event;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.EventUtil;
import com.smartgwt.mobile.client.internal.theme.iphone.PopoverCssResourceIPhone;
import com.smartgwt.mobile.client.types.AnimationEffect;
import com.smartgwt.mobile.client.widgets.Button;
import com.smartgwt.mobile.client.widgets.Canvas;
import com.smartgwt.mobile.client.widgets.Dialog;
import com.smartgwt.mobile.client.widgets.Popover;
import com.smartgwt.mobile.client.widgets.Window;

// For the time being until a separate iPad theme is created, the iPhone theme displays popovers the
// same as iPad.
// A true popover is iPad-only (http://developer.apple.com/library/ios/#documentation/userexperience/conceptual/mobilehig/UIElementGuidelines/UIElementGuidelines.html#//apple_ref/doc/uid/TP40006556-CH13-SW40);
// however, on iPhone a close analog is a full-screen, NavStack-like panel that slides up from
// the bottom.
// The difference can be seen in Mobile Safari. Tap the bookmarks toolbar item in Mobile Safari
// for iPad and the user sees a popover. Tap the bookmarks item in Mobile Safari for iPhone
// and a full-screen panel slides up from the bottom.
@SGWTInternal
public class PopoverImplIPad extends DialogImplIOS implements PopoverImpl {

    private static final PopoverCssResourceIPhone CSS = (PopoverCssResourceIPhone)Popover._CSS;
    private static final int MIN_POPOVER_MARGIN_PX = 10;

    //private Popover self;
    private transient Element wrapperElem,
            topPopoverTipWrapperElem, topPopoverTipElem,
            contentWrapperElem,
            bottomPopoverTipWrapperElem, bottomPopoverTipElem;
    private Button leftButton = null, rightButton = null;
    private Canvas child = null;
    private int lastX, lastY, lastWidth, lastHeight;

    @Override
    public final void initDialog(Dialog self) {
        super.initDialog(self);
        self.sinkEvents(Event.ONCLICK);
    }

    @Override
    public void initPopover(Popover self) {
        final Document document = Document.get();
        wrapperElem = document.createDivElement();
        wrapperElem.setClassName(CSS.popoverWrapperClass());

        topPopoverTipWrapperElem = document.createDivElement();
        topPopoverTipWrapperElem.setClassName(CSS.topPopoverTipWrapperClass());
        topPopoverTipElem = document.createDivElement();
        topPopoverTipElem.setClassName(CSS.popoverTipClass());
        topPopoverTipWrapperElem.appendChild(topPopoverTipElem);
        wrapperElem.appendChild(topPopoverTipWrapperElem);

        self._getHeaderElem().setClassName(CSS.popoverHeaderClass());

        contentWrapperElem = document.createDivElement();
        contentWrapperElem.setClassName(CSS.popoverContentWrapperClass());
        self._getBackgroundElem().replaceChild(contentWrapperElem, self._getInnerElement());
        contentWrapperElem.appendChild(self._getInnerElement());

        self.getElement().replaceChild(wrapperElem, self._getBackgroundElem());
        wrapperElem.appendChild(self._getBackgroundElem());

        bottomPopoverTipWrapperElem = document.createDivElement();
        bottomPopoverTipWrapperElem.setClassName(CSS.bottomPopoverTipWrapperClass());
        bottomPopoverTipElem = document.createDivElement();
        bottomPopoverTipElem.setClassName(CSS.popoverTipClass());
        bottomPopoverTipWrapperElem.appendChild(bottomPopoverTipElem);
        wrapperElem.appendChild(bottomPopoverTipWrapperElem);
    }

    @Override
    public AnimationEffect getDefaultAnimateShowEffect(Window self) {
        return null;
    }

    @Override
    public String lookupShowAnimationName(AnimationEffect animateShowEffect) {
        return null;
    }

    @Override
    public AnimationEffect getDefaultAnimateHideEffect(Window self) {
        return AnimationEffect.FADE;
    }

    @Override
    public String lookupHideAnimationName(AnimationEffect animateHideEffect) {
        if (animateHideEffect == null) return null;
        switch (animateHideEffect) {
            case FADE:
                return CSS.popoverFadeOutAnimationName();
            default:
                return null;
        }
    }

    @Override
    public void setLeftButton(Popover self, Button leftButton) {
        //final PanelHeader header = self._getHeader();
        if (this.leftButton != null) {
            self.remove(this.leftButton);
        }
        if (leftButton != null) {
            self._insert(leftButton, self._getHeaderElem(), 0, true);
        }
        this.leftButton = leftButton;
    }

    @Override
    public void setRightButton(Popover self, Button rightButton) {
        if (this.rightButton != null) {
            self.remove(this.rightButton);
        }
        if (rightButton != null) {
            self._add(rightButton, self._getHeaderElem());
        }
        this.rightButton = rightButton;
    }

    @Override
    public void setChild(Popover self, Canvas child) {
        if (this.child != null) {
            self.remove(this.child);
        }
        if (child != null) self._insert(child, self._getInnerElement(), 1, true);
        this.child = child;
    }

    @Override
    public void showForArea(Popover self, int clientX, int clientY, int offsetWidth, int offsetHeight) {
        lastX = clientX;
        lastY = clientY;
        lastWidth = offsetWidth;
        lastHeight = offsetHeight;
        self.show();
    }

    @Override
    public Element getAnimatedElem(Window self) {
        return wrapperElem;
    }

    @Override
    public int getShowAnimationDurationMillis(Window self) {
        assert false : "No popover show animations are defined. See PopoverImplIPad.lookupShowAnimationName()";
        return 0;
    }

    @Override
    public void doShow(Window windowSelf) {
        final Popover self = (Popover)windowSelf;

        final int windowClientWidth = com.google.gwt.user.client.Window.getClientWidth(),
                windowClientHeight = com.google.gwt.user.client.Window.getClientHeight();

        // Figure out whether this should be top-pointing or bottom-pointing.
        final boolean topPointing = lastY <= windowClientHeight / 2;
        final Element element = wrapperElem;
        final int remainingHeight;
        if (topPointing) {
            element.addClassName(CSS.topClass());
            element.removeClassName(CSS.bottomClass());
            final int preferredTop = lastY + lastHeight;
            final int top = (preferredTop > windowClientHeight / 2 ? lastY + lastHeight / 2 : preferredTop);
            remainingHeight = windowClientHeight - top;
            element.getStyle().setTop(top, Style.Unit.PX);
            element.getStyle().clearBottom();
        } else {
            element.addClassName(CSS.bottomClass());
            element.removeClassName(CSS.topClass());
            final int preferredBottom = windowClientHeight - lastY;
            final int bottom = (preferredBottom > windowClientHeight / 2 ? windowClientHeight - (lastY + lastHeight / 2) : preferredBottom);
            remainingHeight = windowClientHeight - bottom;
            element.getStyle().setBottom(bottom, Style.Unit.PX);
            element.getStyle().clearTop();
        }

        final int minPopoverHeightPx = CSS.verticalPopoverTipWrapperHeightPx() + CSS.popoverHeaderHeightPx() + 2 * CSS.popoverContentWrapperPaddingPx() + 1;
        final Element contentElem = self._getInnerElement();
        contentElem.getStyle().clearHeight();
        if (!self._getSmallFormFactor()) {
            contentElem.getStyle().setHeight(Math.min(remainingHeight - minPopoverHeightPx, Math.max(contentElem.getOffsetHeight(), self.getPreferredContentHeight())), Style.Unit.PX);
        }

        // Horizontally position the popover wrapper elem (`element') and popover tip.
        // Attempt to center the popover around `lastX'.
        final int x = lastX + lastWidth / 2;
        final int popoverOffsetWidthPx = (self._getSmallFormFactor() ? CSS.smallFormFactorPopoverOffsetWidthPx() : CSS.popoverOffsetWidthPx());
        final int left = Math.max(MIN_POPOVER_MARGIN_PX, Math.min(x - popoverOffsetWidthPx / 2, windowClientWidth - MIN_POPOVER_MARGIN_PX - popoverOffsetWidthPx));
        element.getStyle().setPaddingLeft(left, Style.Unit.PX);
        final int verticalTipLeft = Math.max(left + CSS.popoverBorderRadiusPx() + CSS.verticalTipHalfWidthPx(), Math.min(x, left + popoverOffsetWidthPx - CSS.popoverBorderRadiusPx() - CSS.verticalTipHalfWidthPx()));
        (topPointing ? topPopoverTipElem : bottomPopoverTipElem).getStyle().setLeft(verticalTipLeft, Style.Unit.PX);

        super.doShow(self);
    }

    @Override
    public int getHideAnimationDurationMillis(Window self) {
        return CSS.popoverFadeOutAnimationDurationMillis();
    }

    @Override
    public void onBrowserEvent(Window self, Event event) {
        super.onBrowserEvent(self, event);

        final Element targetElem = EventUtil.getTargetElem(event);
        if (targetElem != null) {
            switch (event.getTypeInt()) {
                case Event.ONCLICK:
                    if (self.getDismissOnOutsideClick() && wrapperElem.isOrHasChild(targetElem) && !contentWrapperElem.isOrHasChild(targetElem)) {
                        self.hide();
                    }
                    break;
            }
        }
    }
}
