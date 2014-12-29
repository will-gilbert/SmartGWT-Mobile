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

/**
 */

public enum VerticalAlignment implements ValueEnum {
    /**
     * At the top of the container
     */
    TOP("top"),
    /**
     * Center within container.
     */
    CENTER("center"),
    /**
     * At the bottom of the container
     */
    BOTTOM("bottom");
    private String value;

    VerticalAlignment(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}

