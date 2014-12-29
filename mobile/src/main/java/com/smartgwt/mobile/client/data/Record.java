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
package com.smartgwt.mobile.client.data;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.smartgwt.mobile.client.internal.SelectionItem;
import com.smartgwt.mobile.client.internal.data.HasAttributes;

// `Record' extends `LinkedHashMap' so that value maps deserialized from JSON data are
// `LinkedHashMap's. See FormItem._copyFieldConfiguration().
public class Record extends LinkedHashMap<String, Object> implements HasAttributes, SelectionItem {

    /**
     * Creates a shallow copy of <code>record</code> containing its attributes for properties
     * named in <code>properties</code>.
     *
     * @param record the record to copy attributes from.
     * @param properties an array of properties to copy.
     * @return a copy of <code>record</code> containing only the attributes for properties
     * named in <code>properties</code>.
     */
    public static Record copyAttributes(Record record, String... properties) {
        Record copyOfRecord = new Record();
        copyAttributesInto(copyOfRecord, record, properties);
        return copyOfRecord;
    }

    /**
     * Shallow copies the attributes of <code>record</code> to <code>destRecord</code> for
     * properties named in <code>properties</code>.
     *
     * @param destRecord (out) destination record into which attributes are copied.
     * @param record the record to copy attributes from.
     * @param properties an array of properties to copy.
     */
    public static void copyAttributesInto(Record destRecord, Record record, String... properties) {
        for (String property : properties) {
            if (property == null) continue;
            Object attr = record.getAttributeAsObject(property);
            destRecord.setAttribute(property, attr);
        }
    }

    @Override
    public void setAttribute(String property, Object value) {
        put(property, value);
    }

    public String getAttribute(String property) {
        final Object value = get(property);
        if (value == null) return null;
        return value.toString();
    }

    public String getAttributeAsString(String property) {
        return getAttribute(property);
    }

    public Boolean getAttributeAsBoolean(String property) {
        return (Boolean) get(property);
    }

    public Integer getAttributeAsInt(String property) {
        final Number n = (Number)get(property);
        if (n == null) return null;
        if (n instanceof Integer) return (Integer)n;
        return Integer.valueOf(n.intValue());
    }

    public Long getAttributeAsLong(String property) {
        final Number n = (Number)get(property);
        if (n == null) return null;
        if (n instanceof Long) return (Long)n;
        return Long.valueOf(n.longValue());
    }

    public Float getAttributeAsFloat(String property) {
        final Number n = (Number)get(property);
        if (n == null) return null;
        if (n instanceof Float) return (Float)n;
        return Float.valueOf(n.floatValue());
    }

    public Double getAttributeAsDouble(String property) {
        final Number n = (Number)get(property);
        if (n == null) return null;
        if (n instanceof Double) return (Double)n;
        return Double.valueOf(n.doubleValue());
    }

    public double[] getAttributeAsDoubleArray(String property) {
        return (double[]) get(property);
    }

    public int[] getAttributeAsIntArray(String property) {
        return (int[]) get(property);
    }

    public String[] getAttributeAsStringArray(String property) {
        return (String[]) get(property);
    }

    public Date getAttributeAsDate(String property) {
        return (Date) get(property);
    }

    @Override
    public Object getAttributeAsObject(String property) {
        return get(property);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getAttributeAsList(String property) {
        return (List<T>) get(property);
    }

    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> getAttributeAsMap(String property) {
        return (Map<K, V>) get(property);
    }

    public Record getAttributeAsRecord(String property) {
        return (Record) get(property);
    }
}
