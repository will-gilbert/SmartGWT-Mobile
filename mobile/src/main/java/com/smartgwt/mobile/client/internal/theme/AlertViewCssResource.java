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

package com.smartgwt.mobile.client.internal.theme;

import com.google.gwt.resources.client.CssResource.Shared;
import com.smartgwt.mobile.client.theme.ButtonsCssResource;
import com.smartgwt.mobile.client.theme.DialogCssResource;
import com.smartgwt.mobile.client.theme.FormCssResource;

@Shared
public interface AlertViewCssResource extends ButtonsCssResource, FormCssResource, DialogCssResource {

    @ClassName("sc-alertview")
    public String alertViewClass();

    @ClassName("alertViewScreenCover")
    public String alertViewScreenCoverClass();

    @ClassName("alertViewOuterWrapper")
    public String alertViewOuterWrapperClass();

    @ClassName("alertViewWrapper")
    public String alertViewWrapperClass();

    @ClassName("alertViewButtons")
    public String alertViewButtonsClass();
}
