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

package com.smartgwt.mobile.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.core.Rectangle;
import com.smartgwt.mobile.client.internal.theme.iphone.PopoverCssResourceIPhone;
import com.smartgwt.mobile.client.internal.widgets.PopoverImpl;
import com.smartgwt.mobile.client.theme.ButtonsCssResource;
import com.smartgwt.mobile.client.theme.ThemeResources;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;

public class Popover extends Dialog {

    private static final ButtonsCssResource BUTTONS_CSS = BaseButton._CSS;
    @SGWTInternal
    public static final PopoverCssResourceIPhone _CSS = (PopoverCssResourceIPhone)ThemeResources.INSTANCE.popoverCSS();

    private boolean showCancelButton = true;
    private boolean showDoneButton = true;
    private Boolean smallFormFactor;
    private Button cancelButton;
    private Button doneButton;
    private HandlerRegistration cancelClickRegistration, doneClickRegistration;
    private int preferredContentHeight = 325;

    protected Popover(PopoverImpl impl) {
        super(impl);
        super._setHistoryMarker(";popover");
        super.setDismissOnOutsideClick(true);
        super.setModalMaskStyle(_CSS.popoverModalMaskClass());
        super.setBackgroundStyle(_CSS.popoverClass());

        impl.initPopover(this);

        cancelButton = new Button("Cancel");
        cancelButton._setClassName(BUTTONS_CSS.borderedButtonClass(), false);
        cancelButton._setClassName(BUTTONS_CSS.customTintedButtonClass(), false);
        cancelButton._removeClassName(BUTTONS_CSS.defaultButtonClass());
        cancelButton._removeClassName(BUTTONS_CSS.actionButtonClass());
        cancelClickRegistration = cancelButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (showCancelButton) cancelClick();
            }
        });
        this.<PopoverImpl>_getImpl().setLeftButton(this, cancelButton);

        doneButton = new Button("Done");
        doneButton._setClassName(BUTTONS_CSS.borderedButtonClass(), false);
        doneButton._setClassName(BUTTONS_CSS.importantButtonClass(), false);
        doneButton._removeClassName(BUTTONS_CSS.defaultButtonClass());
        doneButton._removeClassName(BUTTONS_CSS.actionButtonClass());
        doneClickRegistration = doneButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (showDoneButton) doneClick();
            }
        });
        this.<PopoverImpl>_getImpl().setRightButton(this, doneButton);
        doneButton.getElement().getStyle().clearMarginLeft();
    }

    public Popover() {
        this(GWT.<PopoverImpl>create(PopoverImpl.class));
    }

    public Popover(String title) {
        this();
        super.setTitle(title);
    }

    public Popover(String title, Canvas child) {
        this(title);
        internalSetChild(child);
    }

    @Override
    protected void _destroyPopup() {
        if (doneClickRegistration != null) {
            doneClickRegistration.removeHandler();
            doneClickRegistration = null;
        }
        doneButton.destroy();
        if (cancelClickRegistration != null) {
            cancelClickRegistration.removeHandler();
            cancelClickRegistration = null;
        }
        cancelButton.destroy();
        super._destroyPopup();
    }

    /**
     * The "Cancel" button is shown if showCancelButton is <code>true</code>; hidden otherwise.
     * 
     * @return whether to show the Cancel button. Default value: <code>true</code>
     */
    public final boolean getShowCancelButton() {
        return showCancelButton;
    }

    public void setShowCancelButton(Boolean showCancelButton) {
        final boolean newShowCancelButton = showCancelButton == null ? true : showCancelButton.booleanValue();
        this.showCancelButton = newShowCancelButton;
        cancelButton.getElement().getStyle().setVisibility(newShowCancelButton ? Style.Visibility.VISIBLE : Style.Visibility.HIDDEN);
    }

    /**
     * The "Done" button is shown if showDoneButton is <code>true</code>; hidden otherwise.
     * 
     * @return whether to show the Done button. Default value: <code>true</code>
     */
    public final boolean getShowDoneButton() {
        return showDoneButton;
    }

    public void setShowDoneButton(Boolean showDoneButton) {
        final boolean newShowDoneButton = showDoneButton == null ? true : showDoneButton.booleanValue();
        this.showDoneButton = newShowDoneButton;
        doneButton.getElement().getStyle().setVisibility(newShowDoneButton ? Style.Visibility.VISIBLE : Style.Visibility.HIDDEN);
    }

    public final Boolean getSmallFormFactor() {
        return smallFormFactor;
    }

    @SGWTInternal
    public final boolean _getSmallFormFactor() {
        return Canvas._booleanValue(getSmallFormFactor(), false);
    }

    public void setSmallFormFactor(Boolean smallFormFactor) {
        this.smallFormFactor = smallFormFactor;
        if (_getSmallFormFactor()) {
            getElement().addClassName(_CSS.smallFormFactorPopoverClass());
        } else {
            getElement().removeClassName(_CSS.smallFormFactorPopoverClass());
        }
    }

    private void internalSetChild(Canvas child) {
        this.<PopoverImpl>_getImpl().setChild(this, child);
    }

    /**
     * Sets the child widget of the <code>Popover</code>, which is added to the content area.
     *
     * <p><b>NOTE:</b> The <code>Popover</code> may need to limit the size of its content area
     * so that it fits entirely on screen. If a widget is moderately bulky, it is recommended
     * to use a {@link com.smartgwt.mobile.client.widgets.ScrollablePanel} for the child widget
     * so that any overflow remains accessible to the user. See the Widgets -&gt; Dialogs -&gt;
     * TableView with Popover example in Showcase for a sample.
     *
     * @param child child widget.
     * @see #getPreferredContentHeight()
     */
    public void setChild(Canvas child) {
        internalSetChild(child);
    }

    /**
     * The preferred height in pixels of the content area, which holds the child widget.
     * The <code>Popover</code> will try to set the height of the content area to at least
     * this many pixels (allowing more space if the child widget's offsetHeight is more than
     * the preferred content height); however, in no case will the content height be set so
     * that a part of the <code>Popover</code> is off-screen.
     *
     * @return the preferred content height in pixels. Default value: 325
     */
    public final int getPreferredContentHeight() {
        return preferredContentHeight;
    }

    /**
     * Sets {@link #getPreferredContentHeight() preferredContentHeight}.
     *
     * @param preferredContentHeight the new preferred content height.
     * @throws IllegalArgumentException if <code>preferredContentHeight</code> is negative.
     */
    public void setPreferredContentHeight(int preferredContentHeight) {
        if (preferredContentHeight < 0) throw new IllegalArgumentException("`preferredContentHeight' cannot be negative.");
        this.preferredContentHeight = preferredContentHeight;
    }

    @SGWTInternal
    public final void _doDefaultAction() {
        super.doDefaultAction();
    }

    public final void show(Canvas target) {
        showForArea(target.getAbsoluteLeft(), target.getAbsoluteTop(), target.getOffsetWidth(), target.getOffsetHeight());
    }

    /**
     * Show the popover for the given area of interest.
     *
     * @param rect the area of interest.
     */
    public final void showForArea(Rectangle rect) {
        showForArea(rect.getLeft(), rect.getTop(), rect.getWidth(), rect.getHeight());
    }

    /**
     * Show the popover for the given area of interest.
     *
     * @param clientX X coordinate relative to the viewport of the upper left corner of the area of interest.
     * @param clientY Y coordinate relative to the viewport of the upper left corner of the area of interest.
     * @param offsetWidth offset width of the area of interest.
     * @param offsetHeight offset height of the area of interest.
     */
    public void showForArea(int clientX, int clientY, int offsetWidth, int offsetHeight) {
        this.<PopoverImpl>_getImpl().showForArea(this, clientX, clientY, offsetWidth, offsetHeight);
    }
}
