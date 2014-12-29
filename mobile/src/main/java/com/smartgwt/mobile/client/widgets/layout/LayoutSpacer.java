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

package com.smartgwt.mobile.client.widgets.layout;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.smartgwt.mobile.client.widgets.Canvas;

/**
 * Add a LayoutSpacer to a Layout to take up space just like a normal member, without actually drawing anything.
 * Semantically equivalent to using an empty canvas, but higher performance for this particular use case.
 */
public class LayoutSpacer extends Canvas {

    public LayoutSpacer() {
        DivElement divElement = Document.get().createDivElement();
        divElement.addClassName("sc-stretch");
        setElement(divElement);
    }
}
