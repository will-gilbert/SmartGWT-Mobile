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

package com.smartgwt.mobile.client.types;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.theme.ButtonsCssResource;
import com.smartgwt.mobile.client.theme.ThemeResources;

/**
 * Enum with constants for various icon alignment options.
 *
 * @see com.smartgwt.mobile.client.widgets.BaseButton#setIconAlign(IconAlign)
 */
public enum IconAlign {

    LEFT,
    RIGHT;

    private static final ButtonsCssResource CSS = ThemeResources.INSTANCE.buttonsCSS();

    private IconAlign() {}

    @SGWTInternal
    public final String _getClassName() {
        switch (this) {
            case LEFT:
                return null;
            case RIGHT:
                return CSS.rightAlignButtonIconClass();
        }
        assert false : "IconAlign._getClassName(): '" + this + "' unhandled.";
        throw new RuntimeException();
    }
}
