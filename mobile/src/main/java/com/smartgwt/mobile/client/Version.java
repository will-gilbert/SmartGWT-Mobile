
/*
 * SmartGWT (GWT for SmartClient)
 * Copyright 2008 and beyond, Isomorphic Software, Inc.
 *
 * SmartGWT is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.  SmartGWT is also
 * available under typical commercial license terms - see
 * http://smartclient.com/license
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

package com.smartgwt.mobile.client;

import java.util.Date;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.core.client.GWT;

/**
 * Class that returns meta information like version number, major version, minor version and build date.
 */
public final class Version {

    private static VersionConstants constants = GWT.create(VersionConstants.class); 

    private static final DateTimeFormat BUILD_DATE_FORMAT = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss Z");

    /**
     * @return the library version
     */
    public static String getVersion() {
        return constants.projectVersion();
    }

    /**
     * @return the library major version
     */
    public static String getMajor() {
        return "1";
    }

    /**
     * @return the library minor version
     */
    public static String getMinor() {
        return "0";
    }

    /**
     * @return the library build time
     */
    public static Date getBuildDate() {

        Date buildDate;

        try {
            buildDate = BUILD_DATE_FORMAT.parse(constants.buildDate());
        } catch (java.lang.IllegalArgumentException exception) {
            buildDate = new Date(0);
        }

        return buildDate;

    }
}