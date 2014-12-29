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

/**
 * This class is used to display a title as a header (analogous to h2)
 * that is styled based on the platform.
 */
public class Header2 extends Header1 {

    /**
     * The default constructor.
     */
    public Header2() {
        getElement().setClassName(_CSS.heading2Class());
    }

    /**
     * Constructor that accepts the html contents.
     *
     * @param html the header html
     */
    public Header2(String html) {
        this();
        setHtml(html);
    }
}
