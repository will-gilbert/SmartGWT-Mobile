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
public enum Alignment implements ValueEnum {
    /**
     * Center within container.
     */
    CENTER("center"),
    /**
     * Stick to ends with equi-distant spaces between members.
     */
    JUSTIFY("justify"),
    /**
     * Stick to left side of container.
     */
    LEFT("left"),
    /**
     * Stick to right side of container.
     */
    RIGHT("right");

    private String value;

    Alignment(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}

