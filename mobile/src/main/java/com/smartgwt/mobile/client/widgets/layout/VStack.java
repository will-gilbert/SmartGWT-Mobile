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

import com.smartgwt.mobile.client.types.LayoutPolicy;

/**
 * A subclass of Layout that simply stacks members on the vertical axis without trying to manage their height.  On the
 * horizontal axis, any members that do not have explicit widths will be sized to match the width of the stack.
 *
 * @see com.smartgwt.mobile.client.widgets.layout.Layout#getVPolicy
 */

//TODO INTERNAL NOTES
/*
 1. It's a Layout with orientation:vertical and vPolicy:NONE
 2. Once implemented, add a sample to the showcase
*/
public class VStack extends Layout {

    public VStack() {
        super.setStyleName("sc-layout-vbox");
        super.setVertical(true);
        super.setVPolicy(LayoutPolicy.NONE);
    }
}
