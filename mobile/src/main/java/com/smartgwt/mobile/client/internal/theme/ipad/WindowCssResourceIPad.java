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

package com.smartgwt.mobile.client.internal.theme.ipad;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.theme.AlertViewCssResource;
import com.smartgwt.mobile.client.internal.theme.ios.WindowCssResourceIOS;

@SGWTInternal
public interface WindowCssResourceIPad extends AlertViewCssResource, WindowCssResourceIOS {

    public String windowBackgroundFadeInAnimationName();
    public String windowBackgroundFadeOutAnimationName();
    public int windowBackgroundFadeInAnimationDurationMillis();
    public int windowBackgroundFadeOutAnimationDurationMillis();
}
