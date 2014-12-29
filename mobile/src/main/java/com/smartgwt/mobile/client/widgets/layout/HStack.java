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

//TODO INTERNAL NOTES
/*
 1. It's a Layout with orientation:horizontal and hPolicy:NONE
 2. Once implemented, add a sample to the showcase
*/
public class HStack extends Layout {

    public HStack() {
        super.setStyleName("sc-layout-hbox");
        super.setVertical(false);
        super.setHPolicy(LayoutPolicy.NONE);
    }
}
