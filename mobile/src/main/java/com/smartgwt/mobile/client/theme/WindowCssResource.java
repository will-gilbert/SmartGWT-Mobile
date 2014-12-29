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

package com.smartgwt.mobile.client.theme;

import com.google.gwt.resources.client.CssResource.Shared;
import com.smartgwt.mobile.client.internal.theme.PopupCssResource;

@Shared
public interface WindowCssResource extends PopupCssResource {

    @ClassName("modalMask")
    public String modalMaskClass();

    @ClassName("windowContainer")
    public String windowContainerClass();

    @ClassName("windowBackground")
    public String windowBackgroundClass();

    @ClassName("headerless")
    public String headerlessClass();

    @ClassName("windowHeader")
    public String windowHeaderClass();

    @ClassName("windowHeaderLabel")
    public String windowHeaderLabelClass();

    @ClassName("windowBody")
    public String windowBodyClass();
}
