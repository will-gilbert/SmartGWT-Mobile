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

package com.smartgwt.mobile.client.internal.theme.iphone;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.theme.ButtonsCssResource;
import com.smartgwt.mobile.client.theme.HeadingsCssResource;
import com.smartgwt.mobile.client.theme.PopoverCssResource;

@SGWTInternal
public interface PopoverCssResourceIPhone extends ButtonsCssResource, HeadingsCssResource, PopoverCssResource {

    public String popoverFadeOutAnimationName();
    public int popoverFadeOutAnimationDurationMillis();

    public int verticalPopoverTipWrapperHeightPx();
    public int verticalTipHalfWidthPx();
    public int popoverBorderRadiusPx();
    public int popoverOffsetWidthPx();
    public int smallFormFactorPopoverOffsetWidthPx();
    public int popoverHeaderHeightPx();
    public int popoverContentWrapperPaddingPx();

    @ClassName("popoverWrapper")
    public String popoverWrapperClass();

    @ClassName("topPopoverTipWrapper")
    public String topPopoverTipWrapperClass();

    @ClassName("bottomPopoverTipWrapper")
    public String bottomPopoverTipWrapperClass();

    @ClassName("popoverTip")
    public String popoverTipClass();

    @ClassName("popoverHeader")
    public String popoverHeaderClass();

    @ClassName("popoverContentWrapper")
    public String popoverContentWrapperClass();

    @ClassName("top")
    public String topClass();

    @ClassName("bottom")
    public String bottomClass();
}
