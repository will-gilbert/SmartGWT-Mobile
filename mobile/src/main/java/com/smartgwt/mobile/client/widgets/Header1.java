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

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.SpanElement;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.theme.HeadingsCssResource;
import com.smartgwt.mobile.client.theme.ThemeResources;

/**
 * This class is used to display a title as a large header (analogous to h1)
 * that is styled based on the platform.
 */
public class Header1 extends Canvas {

    @SGWTInternal
    public static final HeadingsCssResource _CSS = ThemeResources.INSTANCE.headingsCSS();

    private SpanElement span;

    /**
     * The header constructor.
     */
    public Header1() {
        final Document document = Document.get();
        HeadingElement element = document.createHElement(1);
        element.setClassName(_CSS.headingClass());
        element.addClassName(_CSS.heading1Class());
        span = Document.get().createSpanElement();
        span.setClassName(_CSS.headingContentClass());
        element.appendChild(span);
        setElement(element);
    }

    /**
     * The header constructor.
     *
     * @param html the header html
     */
    public Header1(String html) {
        this();
        span.setInnerHTML(html);
    }

    @SGWTInternal
    public final SpanElement _getSpan() {
        return span;
    }

    /**
     * Set the header html.
     *
     * @param html the header html
     */
    public void setHtml(String html) {
        span.setInnerHTML(html);
        _fireContentChangedEvent();
    }

    /**
     * Return the header html.
     *
     * @return the header html
     */
    public String getHtml() {
        return span.getInnerHTML();
    }
}
