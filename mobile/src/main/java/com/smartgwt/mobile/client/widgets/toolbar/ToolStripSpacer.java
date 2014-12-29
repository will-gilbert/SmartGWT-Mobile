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

package com.smartgwt.mobile.client.widgets.toolbar;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.smartgwt.mobile.client.widgets.Canvas;

/**
 * A toolbar spacer is used in a toolbar for layout purposes. Adding a ToolbarSpacer to a Toolbar via {@link ToolStrip#addSpacer()}
 * results in the "next" toolbar item being pushed to the right.
 *
 * @see ToolStrip#addSpacer()
 */
public class ToolStripSpacer extends Canvas {

    public ToolStripSpacer() {
        DivElement divElement = Document.get().createDivElement();
        divElement.addClassName("sc-stretch");
        setElement(divElement);
    }
}
