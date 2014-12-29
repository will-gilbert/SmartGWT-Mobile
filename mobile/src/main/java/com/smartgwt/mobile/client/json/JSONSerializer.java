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

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.data.Criteria;
import com.smartgwt.mobile.client.data.DSRequest;
import com.smartgwt.mobile.client.data.DataSource;
import com.smartgwt.mobile.client.data.DataSourceField;
import com.smartgwt.mobile.client.data.SimpleType;
import com.smartgwt.mobile.client.internal.Array;
import com.smartgwt.mobile.client.types.ValueEnum;
import com.smartgwt.mobile.client.util.DateUtil;
import com.smartgwt.mobile.client.util.LogicalDate;
import com.smartgwt.mobile.client.util.LogicalTime;
import com.smartgwt.mobile.client.util.SC;

public class JSONSerializer {

    private static final DateTimeFormat DEFAULT_DATETIME_FORMAT = SimpleType.DATETIME_FORMAT;
    private static final TimeZone UTC = TimeZone.createTimeZone(0);

    static {
        assert (new com.smartgwt.mobile.client.data.Record()) instanceof Map;
    }

    @SGWTInternal
    public static String _serialize(Object in, boolean strict, DateTimeFormat datetimeFormat) {
        final StringBuilder out = new StringBuilder();
        datetimeFormat = (datetimeFormat == null ? DEFAULT_DATETIME_FORMAT : datetimeFormat);
        if (in instanceof DSRequest) {
            final DSRequest dsRequest = (DSRequest)in;
            final DataSource ds = DataSource.getDataSource(dsRequest.getDataSource());

            out.append('{');
            boolean writtenOne = false;
            for (Map.Entry<?, ?> e : dsRequest._getSerializableFields(null, null).entrySet()) {
                final Object key = e.getKey();
                if (key == null) continue;
                final Object value = e.getValue();
                if (!_isSerializable(value)) continue;

                if (writtenOne) out.append(',');
                writtenOne = true;
                _appendQuotedFormTo(out, key.toString(), strict);
                out.append(':');

                if (ds != null && "data".equals(key) && !(value instanceof Criteria)) {
                    if (Array.isArray(value)) {
                        _serializeListOfMapsTo(out, Array.asList(value), strict, datetimeFormat, ds._getMergedFields());
                    } else if (value instanceof List) {
                        _serializeListOfMapsTo(out, (List<?>)value, strict, datetimeFormat, ds._getMergedFields());
                    } else if (value instanceof Map) {
                        _serializeMapTo(out, (Map<?, ?>)value, strict, datetimeFormat, ds._getMergedFields());
                    } else {
                        _serializeTo(out, value, strict, datetimeFormat, null);
                    }
                } else if (ds != null && "oldValues".equals(key) && value instanceof Map) {
                    _serializeMapTo(out, (Map<?, ?>)value, strict, datetimeFormat, ds._getMergedFields());
                } else {
                    _serializeTo(out, value, strict, datetimeFormat, null);
                }
            }
            out.append('}');
        } else if (in != null) {
            _serializeTo(out, in, strict, datetimeFormat, null);
        }
        return out.toString();
    }

    @SGWTInternal
    public static boolean _isSimpleTypeValue(Object value) {
        if (value == null ||
            value instanceof CharSequence ||
            value instanceof Enum ||
            value instanceof Boolean ||
            value instanceof Number ||
            value instanceof Date ||
            value instanceof Character)
        {
            return true;
        }
        return false;
    }

    @SGWTInternal
    public static boolean _isSerializable(Object value) {
        return (_isSimpleTypeValue(value) ||
                value instanceof Map ||
                value instanceof List ||
                Array.isArray(value));
    }

    @SGWTInternal
    public static StringBuilder _appendQuotedFormTo(StringBuilder out, CharSequence in, boolean strict) {
        assert out != null;
        if (in == null) return out.append("null");

        out.append('"');
        for (int j = 0; j < in.length(); j++) {
            final char c = in.charAt(j);
            if (strict) {
                if (('\u0000' <= c && c <= '\u001F') ||
                    ('\u007F' <= c && c <= '\u009F'))
                {
                    // Control characters
                    // http://www.fileformat.info/info/unicode/category/Cc/list.htm
                    if (c == '\b') out.append("\\b");
                    else if (c == '\t') out.append("\\t");
                    else if (c == '\n') out.append("\\n");
                    else if (c == '\f') out.append("\\f");
                    else if (c == '\r') out.append("\\r");
                    else {
                        String hexRep = Integer.toString((int)c, 16);
                        if (hexRep.length() == 1) out.append("\\u000");
                        else {
                            assert hexRep.length() == 2;
                            out.append("\\u00");
                        }
                        out.append(hexRep);
                    }
                } else if (c == '"') out.append("\\\"");
                else if (c == '\\') out.append("\\\\");
                else out.append(c);
            } else {
                // ISC RESTHandler does not support the \ uXXXX syntax in string literals for
                // specifying a Unicode character by code point.
                if (c == '\b') out.append("\\b");
                else if (c == '\t') out.append("\\t");
                else if (c == '\n') out.append("\\n");
                else if (c == '\f') out.append("\\f");
                else if (c == '\r') out.append("\\r");
                else if (c == '"') out.append("\\\"");
                else if (c == '\\') out.append("\\\\");
                else out.append(c);
            }
        }
        out.append('"');
        return out;
    }

    @SGWTInternal
    public static StringBuilder _appendQuotedFormTo(StringBuilder out, Object in, boolean strict, DateTimeFormat datetimeFormat) {
        if (in == null) return out.append("null");
        else if (in instanceof CharSequence) return _appendQuotedFormTo(out, (CharSequence)in, strict);
        else if (in instanceof Date) { // Special serialization for Dates
            final Date d = (Date)in;
            if (d instanceof LogicalDate) {
                out.append('"');
                ((LogicalDate)d).appendTo(out);
                out.append('"');
            } else if (d instanceof LogicalTime) {
                out.append('"');
                ((LogicalTime)d).appendTo(out);
                out.append('"');
            } else {
                _appendQuotedFormTo(out, datetimeFormat.format(d, UTC), strict);
            }
            return out;
        } else if (in instanceof ValueEnum) return _appendQuotedFormTo(out, ((ValueEnum)in).getValue(), strict);
        else if (in instanceof Boolean || in instanceof Number) return out.append(in);
        return _appendQuotedFormTo(out, in.toString(), strict);
    }

    @SGWTInternal
    public static void _serializeTo(StringBuilder out, Object value, boolean strict, DateTimeFormat datetimeFormat, DataSourceField dsField) {
        assert _isSerializable(value);

        if (_isSimpleTypeValue(value)) {
            final SimpleType type = (dsField == null ? null : SimpleType.getType(dsField.getType()));
            final Object transformedValue;
            if (type == null || value == null) {
                transformedValue = value;
            } else if (type._inheritsFrom(SimpleType.INTEGER_TYPE)) {
                assert dsField != null;
                assert value != null;
                if (!(value instanceof Number)) {
                    Long l = null;
                    try {
                        l = Long.valueOf(value.toString());
                    } catch (NumberFormatException ex) {
                        SC.logWarn("The value for integer DS field '" + dsField.getName() + "' was not a number: " + value);
                    }
                    transformedValue = l;
                } else {
                    transformedValue = Long.valueOf(((Number)value).longValue());
                }
            } else if (type._inheritsFrom("float")) {
                assert dsField != null;
                assert value != null;
                if (!(value instanceof Number)) {
                    Double d = null;
                    try {
                        d = Double.valueOf(value.toString());
                    } catch (NumberFormatException ex) {
                        SC.logWarn("The value for float DS field '" + dsField.getName() + "' was not a number: " + value);
                    }
                    transformedValue = d;
                } else {
                    transformedValue = Double.valueOf(((Number)value).doubleValue());
                }
            } else if (type._inheritsFrom("date")) {
                assert dsField != null;
                if (!(value instanceof Date)) {
                    SC.logWarn("The value for DS field '" + dsField.getName() + "' was not a `java.util.Date'.");
                    transformedValue = null;
                } else if (type._inheritsFrom(SimpleType.DATETIME_TYPE)) {
                    if (value instanceof LogicalDate) {
                        SC.logWarn("The value for datetime DS field '" + dsField.getName() + "' was a `LogicalDate'.");
                    } else if (value instanceof LogicalTime) {
                        SC.logWarn("The value for datetime DS field '" + dsField.getName() + "' was a `LogicalTime'.");
                    }
                    transformedValue = value;
                } else {
                    if (!(value instanceof LogicalDate)) {
                        SC.logWarn("The value for date DS field '" + dsField.getName() + "' was not a `LogicalDate'.");
                        transformedValue = DateUtil.getLogicalDateOnly((Date)value);
                    } else {
                        transformedValue = value;
                    }
                }
            } else if (type._inheritsFrom("time")) {
                assert dsField != null;
                if (!(value instanceof Date)) {
                    SC.logWarn("The value for time DS field '" + dsField.getName() + "' was not a `java.util.Date'.");
                    transformedValue = null;
                } else if (!(value instanceof LogicalTime)) {
                    SC.logWarn("The value for time DS field '" + dsField.getName() + "' was not a `LogicalTime'.");
                    transformedValue = DateUtil.getLogicalTimeOnly((Date)value);
                } else {
                    transformedValue = value;
                }
            } else transformedValue = value;
            _appendQuotedFormTo(out, transformedValue, strict, datetimeFormat);
        } else if (value instanceof Map) {
            final DataSource typeDS = dsField == null ? null : dsField.getTypeAsDataSource();
            final Map<String, DataSourceField> nestedFields = typeDS == null ? Collections.<String, DataSourceField>emptyMap() : typeDS._getMergedFields();
            _serializeMapTo(out, (Map<?, ?>)value, strict, datetimeFormat, nestedFields);
        } else if (value instanceof List) {
            final DataSource typeDS = dsField == null ? null : dsField.getTypeAsDataSource();
            final Map<String, DataSourceField> nestedFields = typeDS == null ? Collections.<String, DataSourceField>emptyMap() : typeDS._getMergedFields();
            if (nestedFields != null) {
                _serializeListOfMapsTo(out, (List<?>)value, strict, datetimeFormat, nestedFields);
            } else {
                _serializeListTo(out, (List<?>)value, strict, datetimeFormat, dsField);
            }
        } else if (Array.isArray(value)) {
            _serializeListTo(out, Array.asList(value), strict, datetimeFormat, dsField);
        }
    }

    @SGWTInternal
    public static void _serializeMapTo(StringBuilder out, Map<?, ?> map, boolean strict, DateTimeFormat datetimeFormat, Map<String, DataSourceField> dsFields) {
        assert map != null;

        out.append('{');

        boolean writtenOne = false;
        for (Map.Entry<?, ?> e : map.entrySet()) {
            final Object key = e.getKey();
            if (key == null) continue;
            final Object value = e.getValue();
            if (!_isSerializable(value)) continue;

            if (writtenOne) out.append(',');
            writtenOne = true;
            _appendQuotedFormTo(out, key.toString(), strict);
            out.append(':');

            final DataSourceField dsField = (dsFields == null ? null : dsFields.get(key));
            _serializeTo(out, value, strict, datetimeFormat, dsField);
        }

        out.append('}');
    }

    @SGWTInternal
    public static void _serializeListTo(StringBuilder out, List<?> list, boolean strict, DateTimeFormat datetimeFormat, DataSourceField dsField) {
        assert list != null;

        out.append('[');

        boolean writtenOne = false;
        for (final Object object : list) {
            if (!_isSerializable(object)) continue;

            if (writtenOne) out.append(',');
            writtenOne = true;
            _serializeTo(out, object, strict, datetimeFormat, dsField);
        }

        out.append(']');
    }

    @SGWTInternal
    public static void _serializeListOfMapsTo(StringBuilder out, List<?> list, boolean strict, DateTimeFormat datetimeFormat, Map<String, DataSourceField> dsFields) {
        assert list != null;

        out.append('[');

        boolean writtenOne = false;
        for (final Object object : list) {
            if (!(object instanceof Map)) continue;

            if (writtenOne) out.append(',');
            writtenOne = true;
            _serializeMapTo(out, (Map<?, ?>)object, strict, datetimeFormat, dsFields);
        }

        out.append(']');
    }
}
