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

package com.smartgwt.mobile.client.util;

import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;


/**
 * Internal helper class.
 */
public class JSOHelper {

    private JSOHelper() {
    }

    public static boolean isJSO(Object object) {
        return object instanceof JavaScriptObject;
    }

    public static native boolean isDate(Object obj) /*-{
        if (typeof obj === "object") {
            if (obj) {
                if (obj instanceof Date ||
                    obj instanceof $wnd.Date ||
                    Object.prototype.toString.call(obj) == "[object Date]")
                {
                    return true;
                }
            }
        }
        return false;
    }-*/;

    public static native boolean isArray(Object obj) /*-{
        // http://javascript.crockford.com/remedial.html
        if (typeof obj === "object") {
            if (obj) {
                if (obj instanceof Array ||
                    obj instanceof $wnd.Array ||
                    Object.prototype.toString.call(obj) == "[object Array]")
                {
                    return true;
                }
            }
        }
        return false;
    }-*/;

    public static native boolean isString(Object obj) /*-{
        var t = (typeof obj);
        if (t === "string") return true;
        if (t === "object") {
            if (t != null) {
                if (obj instanceof String ||
                    obj instanceof $wnd.String ||
                    Object.prototype.toString.call(obj) == "[object String]")
                {
                    return true;
                }
            }
        }
        return false;
    }-*/;

    public static boolean isJavaBoolean(Object obj) {
        return obj instanceof Boolean;
    }

    public static Date toDate(double millis) {
        return new Date((long)millis);
    }
}
