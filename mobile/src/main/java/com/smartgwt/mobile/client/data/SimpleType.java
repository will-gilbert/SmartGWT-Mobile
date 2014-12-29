package com.smartgwt.mobile.client.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.JsDate;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.TimeZone;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.data.CanFormatDateTime;
import com.smartgwt.mobile.client.internal.util.ObjectUtil;
import com.smartgwt.mobile.client.internal.util.XMLUtil;
import com.smartgwt.mobile.client.util.DateUtil;
import com.smartgwt.mobile.client.util.JSOHelper;
import com.smartgwt.mobile.client.util.LogicalDate;
import com.smartgwt.mobile.client.util.LogicalTime;
import com.smartgwt.mobile.client.util.TimeUtil;

public abstract class SimpleType {
    private static final Map<String, SimpleType> registeredTypes = new HashMap<String, SimpleType>();

    public static final SimpleType TEXT_TYPE = new SimpleType("text") {
        @Override
        public String parseInput(String text) {
            if (text == null) return null;
            return text.trim();
        }
    };

    public static final SimpleType INTEGER_TYPE = new SimpleType("integer") {
        @Override
        public Number parseInput(String text) {
            // http://ideone.com/DOkXRo
            if (text == null) return null;
            text = text.trim();
            if (text.length() == 0) return null;
            final long l;
            try {
                l = Long.parseLong(text);
            } catch (NumberFormatException ex) {
                return null;
            }
            return ObjectUtil.normalize((Long)l);
        }

        @Override
        public Object _fromVal(JSONValue val) {
            final JSONNumber num = (val == null ? null : val.isNumber());
            if (num != null) return ObjectUtil.normalize((Long)(long)num.doubleValue());
            else return super._fromVal(val);
        }
    };

    public static final SimpleType BOOLEAN_TYPE = new SimpleType("boolean") {
        @Override
        public Boolean parseInput(String text) {
            if (text == null) return null;
            text = text.trim();
            if ("true".equalsIgnoreCase(text) || "1".equals(text)) return Boolean.TRUE;
            else if ("false".equalsIgnoreCase(text) || "0".equals(text)) return Boolean.FALSE;
            else return null;
        }

        @Override
        public Object _fromVal(JSONValue val) {
            final JSONBoolean bool = (val == null ? null : val.isBoolean());
            if (bool != null) return Boolean.valueOf(bool.booleanValue());
            else return super._fromVal(val);
        }
    };

    public static final DateTimeFormat DATE_FORMAT = DateTimeFormat.getFormat("yyyy-MM-dd"),
            DATETIME_FORMAT = DateTimeFormat.getFormat("yyyy-MM-dd'T'HH:mm:ss"),
            TIME_FORMAT = DateTimeFormat.getFormat("HH:mm:ss");

    private static class DateTimeSimpleType extends SimpleType implements CanFormatDateTime {

        // GWT (including versions 2.4, 2.5, and the current trunk at revision 11429) has a bug
        // in its parsing code that handles the 'k' pattern char:
        //     final DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd kk:mm:ss.SZ");
        //     assertEquals(767000L, dtf.parse("1970-01-01 24:12:47.000-0000").getTime()); // Fails
        //     dtf.parseStrict("1970-01-01 24:12:47.000-0000"); // Throws
        // http://code.google.com/p/google-web-toolkit/issues/detail?id=7861
        //
        // This function converts the input string, if in "yyyy-MM-dd kk:mm:ss" format, to
        // "yyyy-MM-dd'T'HH:mm:ss" format.
        private static native String convertToFormat1(String text) /*-{
            return text.replace(/^(\d+)-(\d+)-(\d+) (\d+):(\d+):(\d+)/, function (match, p0, p1, p2, p3, p4, p5, p6) {
                var hrs = parseInt(p3, 10) % 24;
                var ret = p0 + "-" + p1 + "-" + p2 + "T";
                if (hrs < 10) ret += "0";
                ret += hrs + ":" + p4 + ":" + p5;
                return ret;
            });
        }-*/;

        private final DateTimeFormat externalFormat, format1, format2;

        public DateTimeSimpleType() {
            super("datetime");
            this.externalFormat = DATETIME_FORMAT;
            this.format1 = DateTimeFormat.getFormat(DATETIME_FORMAT.getPattern() + ".SZ");
            this.format2 = DateTimeFormat.getFormat("yyyy-MM-dd kk:mm:ss.SZ");
        }

        @Override
        public Object _fromVal(JSONValue val) {
            if (val != null) {
                final JSONObject obj = val.isObject();
                if (obj != null) {
                    final Object jObj = obj.getJavaScriptObject();
                    if (jObj instanceof Date) return (Date)jObj;
                    else if (JSOHelper.isDate(jObj)) {
                        final JsDate jsD = (JsDate)jObj;
                        return new Date((long)jsD.getTime());
                    }
                }
                final JSONNumber num = val.isNumber();
                if (num != null) {
                    return new Date((long)num.doubleValue());
                }
            }
            return super._fromVal(val);
        }

        @Override
        public Date parseInput(String text) {
            @SuppressWarnings("unused")
            final String origText = text;
            if (text == null) return null;
            final String datetimePrefix = "$$DATE$$:";
            if ((text = text.trim()).startsWith(datetimePrefix)) {
                text = text.substring(datetimePrefix.length()).trim();
            }
            final int periodPos = text.indexOf('.');
            final String textSuffix;
            if (periodPos >= 0) {
                textSuffix = "-0000";
            } else {
                textSuffix = ".000-0000";
            }
            try {
                return format1.parse(convertToFormat1(text) + textSuffix);
            } catch (IllegalArgumentException ex) {
                return null;
            }
        }

        @Override
        public String format(Date date, TimeZone timeZone) {
            return externalFormat.format(date, timeZone);
        }
    }

    public static final SimpleType DATETIME_TYPE = new DateTimeSimpleType();

    static {
        final SimpleType floatType = new SimpleType("float") {
            @Override
            public Double parseInput(String text) {
                if (text == null) return null;
                text = text.trim();
                if (text.length() == 0) return null;
                if (text.charAt(0) == '+') {
                    text = text.substring(1);
                    if (text.length() == 0) return null;
                }
                try {
                    return Double.valueOf(text);
                } catch (NumberFormatException ex) {
                    return null;
                }
            }

            @Override
            public Object _fromVal(JSONValue val) {
                final JSONNumber num = (val == null ? null : val.isNumber());
                if (num != null) return Double.valueOf(num.doubleValue());
                else return super._fromVal(val);
            }
        };
        final SimpleType dateType = new SimpleType("date") {

            @Override
            public Object _fromVal(JSONValue val) {
                final JSONObject obj;
                if (val != null && (obj = val.isObject()) != null) {
                    final Object jObj = obj.getJavaScriptObject();
                    if (jObj instanceof LogicalDate) return (LogicalDate)jObj;
                }
                return super._fromVal(val);
            }

            @Override
            public LogicalDate parseInput(String text) {
                final String datePrefix = "$$DATE$$:";
                if (text != null && (text = text.trim()).startsWith(datePrefix)) text = text.substring(datePrefix.length());
                return DateUtil.parseInput(text);
            }
        };
        DATETIME_TYPE._setInheritsFrom(dateType);
        @SuppressWarnings("unused")
        final SimpleType timeType = new SimpleType("time") {

            @Override
            public Object _fromVal(JSONValue val) {
                final JSONObject obj;
                if (val != null && (obj = val.isObject()) != null) {
                    final Object jObj = obj.getJavaScriptObject();
                    if (jObj instanceof LogicalTime) return (LogicalTime)jObj;
                }
                return super._fromVal(val);
            }

            @Override
            public LogicalTime parseInput(String text) {
                final String timePrefix = "$$TIME$$:";
                if (text != null && (text = text.trim()).startsWith(timePrefix)) text = text.substring(timePrefix.length());
                return TimeUtil._parseInput(text);
            }
        };

        // Register synonyms
        registeredTypes.put("string", TEXT_TYPE);
        registeredTypes.put("int", INTEGER_TYPE);
        registeredTypes.put("long", INTEGER_TYPE);
        registeredTypes.put("number", INTEGER_TYPE);
        registeredTypes.put("sequence", INTEGER_TYPE);
        registeredTypes.put("decimal", floatType);
        registeredTypes.put("double", floatType);
        registeredTypes.put("dateTime", DATETIME_TYPE);
    }

    public static SimpleType getType(String name) {
        if (name == null) return null;
        return registeredTypes.get(name);
    }

    private SimpleType inheritsFrom;
    private String name;

    public SimpleType(String name) {
        if (name == null) throw new NullPointerException("`name' cannot be `null'.");
        if (registeredTypes.containsKey(name)) {
            throw new IllegalArgumentException("SimpleType '" + name + "' already registered");
        }
        this.name = name;
        registeredTypes.put(name, this);
    }

    @SGWTInternal
    public final SimpleType _getInheritsFrom() {
        return inheritsFrom;
    }

    @SGWTInternal
    public void _setInheritsFrom(SimpleType inheritsFrom) {
        this.inheritsFrom = inheritsFrom;
    }

    public final String getName() {
        return name;
    }

    @SGWTInternal
    public Object _fromNode(Node node) {
        if (node == null) return null;
        String text = null;
        if (node instanceof Element) {
            text = XMLUtil.getTextContent((Element)node);
        } else {
            text = node.getNodeValue();
        }
        return parseInput(text);
    }

    @SGWTInternal
    public Object _fromVal(JSONValue val) {
        String text = null;
        if (val != null && val.isNull() == null) { // Not `null'.
            JSONString str = val.isString();
            if (str != null) text = str.stringValue();
            else text = val.toString();
        }
        return parseInput(text);
    }

    public boolean _inheritsFrom(final SimpleType otherType) {
        if (otherType == null) return false;

        SimpleType type = this;
        do {
            if (type.equals(otherType)) return true;
            type = type._getInheritsFrom();
        } while (type != null);
        return false;
    }

    @SGWTInternal
    public final boolean _inheritsFrom(String otherType) {
        return _inheritsFrom(getType(otherType));
    }

    /**
     * Converts the input string to a value.
     * 
     * <p>This function should not throw an exception.  If an input string cannot be converted
     * to a value, then an implementation should return <code>null</code> or some type-specific
     * default value.
     * 
     * @param text the input string to parse.
     * @return the value represented by <code>text</code>, or <code>null</code> or a type-specific
     * default value if it could not be parsed.
     */
    public abstract Object parseInput(String text);
}
