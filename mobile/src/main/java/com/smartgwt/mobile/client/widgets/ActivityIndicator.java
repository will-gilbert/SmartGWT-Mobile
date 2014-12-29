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
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.widgets.ActivityIndicatorImpl;
import com.smartgwt.mobile.client.theme.ActivityIndicatorCssResource;
import com.smartgwt.mobile.client.theme.ThemeResources;

/**
 * A spinning indicator used to display activity where the total duration is not known.
 */
public class ActivityIndicator extends Canvas {

    @SGWTInternal
    public static final ActivityIndicatorCssResource _CSS = ThemeResources.INSTANCE.activityIndicatorCSS();

    private ActivityIndicatorImpl impl;

    public ActivityIndicator() {
        DivElement rootDiv = Document.get().createDivElement();
        rootDiv.setClassName(_CSS.activityIndicatorClass());
        setElement(rootDiv);
        impl = GWT.create(ActivityIndicatorImpl.class);
        add(impl, getElement());
    }
}
