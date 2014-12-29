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

import com.smartgwt.mobile.client.internal.EventHandler;
import com.smartgwt.mobile.client.widgets.layout.SegmentedControl;

/**
 * Representation of a "segment" within a {@link SegmentedControl}.
 * This class is intended to be used only with a {@link SegmentedControl}
 * and not as a standalone widget.
 */
public class Segment extends BaseButton {

    /**
     * Default constructor.
     */
    public Segment() {
        getElement().addClassName(EventHandler.LONG_TOUCH_OKAY_CLASS_NAME);
        getElement().addClassName(_CSS.buttonSegmentClass());
    }

    /**
     * Segment class constructor.
     *
     * @param title the title of the segment
     */
    public Segment(String title) {
        super(title);
        getElement().addClassName(EventHandler.LONG_TOUCH_OKAY_CLASS_NAME);
        getElement().addClassName(_CSS.buttonSegmentClass());
    }
}
