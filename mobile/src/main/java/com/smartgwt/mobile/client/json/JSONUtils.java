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

package com.smartgwt.mobile.client.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;

/**
 * Static utility methods to convert Javascript objects and object arrays to SmartGWT Mobile
 * Record and RecordLists respectively.  Note that the JSNI methods assume that they will be
 * passed appropriate JS objects - so if you pass something other than an Array to toRecordList(),
 * for example, you'll see crashes. 
 */

public class JSONUtils {

    public static Record toRecord(JavaScriptObject jso) {
        Record record = new Record();
        int keyCount = getPropertyCount(jso);
        for (int i = 0; i < keyCount; i++) {
            String key = getPropertyName(jso, i);
            Object value = getPropertyValue(jso, key);
            if (value instanceof JavaScriptObject) {
                if ("array".equals(getPropertyType(jso, key))) {
                    Object listItemValue = getListItemValue((JavaScriptObject)value, 0);
                    if (listItemValue == null || listItemValue instanceof JavaScriptObject) {
                        value = toRecordList((JavaScriptObject)value);
                    } else {
                        value = toGenericList((JavaScriptObject)value);
                    }
                } else {
                    value = toRecord((JavaScriptObject)value);
                }
            }
            record.put(key, value);
        }
        return record;
    }

    public static Map<String, Object> serverResponseToMap(String js) {

        // HACK: The SmartClient Server returns JS that absolutely depends on the presence of
        // a SmartClient-augmented Date class.  To work around this temporarily, we'll parse
        // the dependency out of the returned JS text and replace it with a native Date
        // creation using the same params.  Note that this DOES NOT GIVE THE SAME RESULT unless
        // you happen to be in GMT timezone - it's just a temporary bodge.
        js = js.replaceAll("Date.parseServerDate", "new Date");

        JavaScriptObject rawResponse = evalJSONString(js);
        return toRecord(getPropertyAsJSO(rawResponse, "response"));
    }

    public static RecordList serverResponseToRecordList(String js) {
        JavaScriptObject rawResponse = evalJSONString(js);
        return toRecordList(getDataListFromServerResponse(rawResponse));
    }

    public static RecordList toRecordList(String js) {
        return toRecordList(evalJSONString(js));
    }

    public static RecordList toRecordList(JavaScriptObject jso) {
        RecordList list = new RecordList();
        final int recordCount = getRecordCount(jso);
        for (int i = 0; i < recordCount; i++) {
            Object value = getListItemValue(jso, i);
            if (value instanceof JavaScriptObject) {
                JavaScriptObject jsRecord = getJSRecord(jso, i);
                list.add(toRecord(jsRecord));
            } else {
                // This should never happen - this method should only be called for lists of
                // complex objects
            }
        }
        return list;
    }

    public static List<Object> toGenericList(JavaScriptObject jso) {
        List<Object> list = new ArrayList<Object>();
        int recordCount = getRecordCount(jso);
        for (int i = 0; i < recordCount; i++) {
            list.add(getListItemValue(jso, i));
        }
        return list;
    }

    public static native JavaScriptObject getDataListFromServerResponse(JavaScriptObject jso) /*-{
        if (jso.response) return jso.response.data;    
    }-*/;

    public static native JavaScriptObject evalJSONString(String json) /*-{
        var isHosted = !@com.google.gwt.core.client.GWT::isScript()();
        if (isHosted) {
            return eval("(" + json + ")");
        } else {
            return $wnd.eval.call($wnd, "(" + json + ")");
        }
    }-*/;

    public static native void eval(String jsFrag) /*-{
        // See the comment in SmartGwtMobileEntryPoint.java. In hosted mode, we eval() in the
        // code frame, but in compiled mode, we eval() in the host frame.
        var isHosted = !@com.google.gwt.core.client.GWT::isScript()();
        if (isHosted) {
            eval(jsFrag);
        } else {
            $wnd.eval.call($wnd, jsFrag);
        }
    }-*/;

    public static native int getPropertyCount(JavaScriptObject jso) /*-{
        if (jso == null) return 0;
        var count = 0;
        for (var key in jso) {
            if (jso.hasOwnProperty(key)) count++;
        }
        return count;
    }-*/;

    public static native String getPropertyName(JavaScriptObject jso, int i) /*-{
        var count = 0;
        for (var key in jso) {
            if (count++ == i) return key;
        }
    }-*/;

    public static Object getPropertyValue(JavaScriptObject jso, String keyName) {
        Object value;
        String type = getPropertyType(jso, keyName);
        if ("string".equals(type)) value = getStringValue(jso, keyName);
        else if ("number".equals(type)) value = getNumberValue(jso, keyName);
        else if ("date".equals(type)) value = new Date((long)getDateValue(jso, keyName));
        else if ("boolean".equals(type)) value = getBooleanValue(jso, keyName);
        else if ("javaObject".equals(type)) value = getObjectValue(jso, keyName);
        else value = getPropertyAsJSO(jso, keyName);

        return value;
    }

    public static Object getListItemValue(JavaScriptObject jso, int index) {
        Object value;
        String type = getListItemType(jso, index);
        if ("string".equals(type)) value = getStringValue(jso, index);
        else if ("number".equals(type)) value = getNumberValue(jso, index);
        else if ("date".equals(type)) value = new Date((long)getDateValue(jso, index));
        else if ("boolean".equals(type)) value = getBooleanValue(jso, index);
        else if ("javaObject".equals(type)) value = getObjectValue(jso, index);
        else value = getListItemAsJSO(jso, index);

        return value;
    }

    public static native String getPropertyType(JavaScriptObject jso, String keyName) /*-{
        var value = jso[keyName];
        var type = typeof value;
        if (type == 'object' && value != null) {
            if (value.constructor.toString().match(/date/i) != null) type = 'date';
            else if (value.constructor.toString().match(/string/i) != null) type = 'string';
            else if (value.constructor.toString().match(/array/i) != null) type = 'array';
        }
        if ($wnd.SmartGWTMobile.isNativeJavaObject(value)) {
            type = 'javaObject';
        }

        return type;
    }-*/;

    public static native String getListItemType(JavaScriptObject jso, int index) /*-{
        // This logic is a duplicate of getPropertyType() - can't work out a cleaner factoring
        // because JSNI methods cannot call other JSNI methods, and it's not safe to just
        // return a JSO from a JSNI method - if the value you're returning is a primitive,
        // it will crash (avoiding that crash elsewhere is the whole purpose of this method)
        var value = jso[index];
        var type = typeof value;
        if (type == 'object' && value != null) {
            if (value.constructor.toString().match(/date/i) != null) type = 'date';
            else if (value.constructor.toString().match(/string/i) != null) type = 'string';
            else if (value.constructor.toString().match(/array/i) != null) type = 'array';
        }
        if ($wnd.SmartGWTMobile.isNativeJavaObject(value)) {
            type = 'javaObject';
        }
        return type;
    }-*/;

    private static native JavaScriptObject getPropertyAsJSO(JavaScriptObject jso, String keyName) /*-{
        return jso[keyName];
    }-*/;

    private static native String getStringValue(JavaScriptObject jso, String keyName) /*-{
        return jso[keyName];
    }-*/;

    private static native double getNumberValue(JavaScriptObject jso, String keyName) /*-{
        return jso[keyName];
    }-*/;

    private static native double getDateValue(JavaScriptObject jso, String keyName) /*-{
        return jso[keyName].getTime();
    }-*/;

    private static native boolean getBooleanValue(JavaScriptObject jso, String keyName) /*-{
        return jso[keyName];
    }-*/;

    private static native Object getObjectValue(JavaScriptObject jso, String keyName) /*-{
        return jso[keyName];
    }-*/;

    private static native String getStringValue(JavaScriptObject jso, int index) /*-{
        return jso[index];
    }-*/;

    private static native double getNumberValue(JavaScriptObject jso, int index) /*-{
        return jso[index];
    }-*/;

    private static native double getDateValue(JavaScriptObject jso, int index) /*-{
        return jso[index].getTime();
    }-*/;

    private static native boolean getBooleanValue(JavaScriptObject jso, int index) /*-{
        return jso[index];
    }-*/;

    private static native Object getObjectValue(JavaScriptObject jso, int index) /*-{
        return jso[index];
    }-*/;

    private static native JavaScriptObject getListItemAsJSO(JavaScriptObject jso, int index) /*-{
        return jso[index];
    }-*/;

    private static native JavaScriptObject getJSRecord(JavaScriptObject jso, int i) /*-{
        return jso[i];
    }-*/;

    private static native int getRecordCount(JavaScriptObject jso) /*-{
        return jso.length;
    }-*/;
}
