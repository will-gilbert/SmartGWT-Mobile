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

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.theme.ProgressbarCssResource;
import com.smartgwt.mobile.client.theme.ThemeResources;
import com.smartgwt.mobile.client.widgets.events.PercentChangedEvent;
import com.smartgwt.mobile.internal.gwt.dom.client.DOMConstants;

/**
 * The Progressbar widget class implements progress bars (graphical bars whose lengths represent percentages, typically of task completion).
 */
public class Progressbar extends Canvas implements com.smartgwt.mobile.client.widgets.events.HasPercentChangedHandlers {

    @SGWTInternal
    public static final ProgressbarCssResource _CSS = ThemeResources.INSTANCE.progressbarCSS();

    private DivElement bar;
    private int breadth;
    private int length;
    private int percentDone = 0;
    private boolean vertical = false;

	/**
	 * Uses the css settings under theme/public/iOS/progressbar.css which defines both styling and animation
	 */
    public Progressbar() {
        final Document document = Document.get();
        final Element elem = document.createDivElement();
        setElement(elem);
        elem.setClassName(_CSS.progressbarClass());
        bar = document.createDivElement();
        bar.setClassName(_CSS._progressbarBarClass());
        elem.appendChild(bar);

        _sinkTransitionEndEvent();
    }

    /**
     * Thickness of the progressbar in pixels. This is effectively width for a vertical progressbar, or height for a horizontal
     * progressbar. <P> This property must be set instead of setting <code>width</code> or <code>height</code>.
     * Sets the breadth of the progressbar to newLength. This is the height of a horizontal progressbar, or the width of a vertical progressbar.
     *
     * @param breadth the new breadth of the progressbar. Default value is 20
     */
    public void setBreadth(int breadth) {
    	this.breadth = breadth;
    }

    /**
     * Thickness of the progressbar in pixels. This is effectively width for a vertical progressbar, or height for a horizontal
     * progressbar. <P> This property must be set instead of setting <code>width</code> or <code>height</code>.
     *
     * @return Returns the current height of a horizontal progressbar, or width of a vertical progressbar.
     */
    public final int getBreadth() {
        return breadth;
    }

    /**
     * Length of the progressbar in pixels. This is effectively height for a vertical progressbar, or width for a horizontal
     * progressbar. <P> This property must be set instead of setting <code>width</code> or <code>height</code>.
     * Sets the length of the progressbar to newLength. This is the width of a horizontal progressbar, or the height of a vertical progressbar.
     *
     * @param length the new length of the progressbar. Default value is 100
     */
    public void setLength(int length) {
    	this.length = length;
    }

    /**
     * Length of the progressbar in pixels. This is effectively height for a vertical progressbar, or width for a horizontal
     * progressbar. <P> This property must be set instead of setting <code>width</code> or <code>height</code>.
     *
     * @return Returns the current width of a horizontal progressbar, or height of a vertical progressbar.
     */
    public int getLength() {
        return length;
    }

    /**
     * Number from 0 to 100, inclusive, for the percentage to be displayed graphically in this progressbar.
     * Sets {@link #getPercentDone() percentDone} to <code>newPercentDone</code>.
     *
     * @param newPercentDone percent to show as done (0-100). Default value is 0
     * @param duration in seconds for bar to reach this percent done
     */
    public void setPercentDone(final int newPercentDone, int duration) {
        if (percentDone == newPercentDone) return;

        if (0 <= newPercentDone && newPercentDone <= 100) {
            percentDone = newPercentDone;
            final Style barStyle = bar.getStyle();
            if (duration == 0 || !isAttached() || !isVisible()) {
                barStyle.setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "none");
                barStyle.setWidth(newPercentDone, Style.Unit.PCT);
                PercentChangedEvent.fire(this, percentDone);
            } else {
                barStyle.setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "width");
                barStyle.setProperty(DOMConstants.INSTANCE.getTransitionDurationPropertyName(), duration + "s");
                new Timer() {
                    @Override
                    public void run() {
                        barStyle.setWidth(newPercentDone, Style.Unit.PCT);
                    }
                }.schedule(1);
            }
        } else {
            throw new RuntimeException(newPercentDone + " is out of bounds");
        }
    }

    /**
     * Number from 0 to 100, inclusive, for the percentage to be displayed graphically in this progressbar.
     *
     * @return int
     */
    public int getPercentDone() {
        return percentDone;
    }


    /**
     * Indicates whether this is a vertical or horizontal progressbar.
     *
     * @param vertical vertical Default value is false
     */
    public void setVertical(Boolean vertical) {
    	this.vertical = vertical;
    }

    /**
     * Indicates whether this is a vertical or horizontal progressbar.
     *
     * @return Boolean
     */
    public Boolean getVertical() {
        return vertical;
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);

        final String eventType = event.getType();
        if (eventType == DOMConstants.INSTANCE.getTransitionEndEventType() && bar.equals(event.getEventTarget())) {
            PercentChangedEvent.fire(this, percentDone);
            return;
        }
    }

    /**
     * Add a percentChanged handler.
     * <p/>
     * This method is called when the percentDone value changes. Observe this method to be notified upon a change to the
     * percentDone value.
     *
     * @param handler the percentChanged handler
     * @return {@link HandlerRegistration} used to remove this handler
     */
    public HandlerRegistration addPercentChangedHandler(com.smartgwt.mobile.client.widgets.events.PercentChangedHandler handler) {
        return addHandler(handler, PercentChangedEvent.getType());
    }
}
