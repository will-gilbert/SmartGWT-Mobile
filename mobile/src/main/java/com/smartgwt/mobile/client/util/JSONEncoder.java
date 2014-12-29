package com.smartgwt.mobile.client.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.Array;
import com.smartgwt.mobile.client.internal.Comm;
import com.smartgwt.mobile.client.internal.util.CanSerialize;
import com.smartgwt.mobile.client.internal.util.HasSerializeableFields;
import com.smartgwt.mobile.client.types.JSONCircularReferenceMode;
import com.smartgwt.mobile.client.types.JSONDateFormat;
import com.smartgwt.mobile.client.types.ValueEnum;
import com.smartgwt.mobile.client.widgets.Canvas;

public class JSONEncoder {

    @SGWTInternal
    public static boolean _defaultPrettyPrint = false;

    /*private static String spaces = "                ";

    private static String getSpaces(int length) {
        assert length >= 0;
        while (spaces.length() < length) {
            spaces += spaces;
        }
        return spaces.substring(0, length);
    }*/

    @SGWTInternal
    public static Appendable _appendQuotedFormTo(Appendable out, CharSequence in, boolean strict) throws IOException {
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
                // ISC `RESTHandler' does not support the \ uXXXX syntax in string literals for
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

    private static native String nativeToString(double d) /*-{
        return String(d);
    }-*/;

    private String circularReferenceMarker;
    private JSONCircularReferenceMode circularReferenceMode;
    private JSONDateFormat dateFormat;
    private transient Map<Object, String> objPaths = new HashMap<Object, String>();
    private Boolean prettyPrint;
    private Boolean strictJSON;
    private Boolean strictQuoting;

    public final String getCircularReferenceMarker() {
        return circularReferenceMarker;
    }

    @SGWTInternal
    public final String _getCircularReferenceMarker() {
        String ret = getCircularReferenceMarker();
        if (ret == null) ret = "$$BACKREF$$";
        return ret;
    }

    public void setCircularReferenceMarker(String circularReferenceMarker) {
        this.circularReferenceMarker = circularReferenceMarker;
    }

    public final JSONCircularReferenceMode getCircularReferenceMode() {
        return circularReferenceMode;
    }

    @SGWTInternal
    public final JSONCircularReferenceMode _getCircularReferenceMode() {
        JSONCircularReferenceMode ret = getCircularReferenceMode();
        if (ret == null) ret = JSONCircularReferenceMode.PATH;
        return ret;
    }

    public void setCircularReferenceMode(JSONCircularReferenceMode circularReferenceMode) {
        this.circularReferenceMode = circularReferenceMode;
    }

    public final JSONDateFormat getDateFormat() {
        return dateFormat;
    }

    @SGWTInternal
    public final JSONDateFormat _getDateFormat() {
        JSONDateFormat ret = getDateFormat();
        if (ret == null) ret = JSONDateFormat.XML_SCHEMA;
        return ret;
    }

    public void setDateFormat(JSONDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public final Boolean getPrettyPrint() {
        return prettyPrint;
    }

    @SGWTInternal
    public final boolean _getPrettyPrint() {
        return Canvas._booleanValue(getPrettyPrint(), _defaultPrettyPrint);
    }

    public void setPrettyPrint(Boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    public final Boolean getStrictJSON() {
        return strictJSON;
    }

    @SGWTInternal
    public final boolean _getStrictJSON() {
        return Canvas._booleanValue(getStrictJSON(), false);
    }

    public void setStrictJSON(Boolean strictJSON) {
        this.strictJSON = strictJSON;
    }

    public final Boolean getStrictQuoting() {
        return strictQuoting;
    }

    @SGWTInternal
    public final boolean _getStrictQuoting() {
        return Canvas._booleanValue(getStrictQuoting(), true);
    }

    public void setStrictQuoting(Boolean strictQuoting) {
        this.strictQuoting = strictQuoting;
    }

    public String encode(Object object) {
        objPaths.clear();
        final StringBuilder sb = new StringBuilder();
        try {
            _serializeTo(sb, object, _getPrettyPrint() ? "" : null, null);
        } catch (IOException ex) {
            assert false;
            throw new RuntimeException(ex);
        } finally {
            objPaths.clear();
        }
        return sb.toString();
    }

    public String encodeDate(Date d) {
        if (_getDateFormat() == JSONDateFormat.DATE_CONSTRUCTOR) {
            return DateUtil._serialize(d);
        } else {
            return DateUtil._toSchemaDate(d);
        }
    }

    private String serialize_addToPath(String objPath, int i) {
        return objPath + "[" + Integer.toString(i) + "]";
    }

    private String serialize_addToPath(String objPath, String newIdentifier) {
        if (Comm.SIMPLE_IDENTIFIER_RE.test(newIdentifier)) {
            return objPath + "[\"" + newIdentifier + "\"]";
        } else {
            return objPath + "." + newIdentifier;
        }
    }

    @SGWTInternal
    public Appendable _serializeTo(Appendable a, Object object, String prefix, String objPath) throws IOException {
        if (object == null) return a.append("null");

        if (objPath == null) objPath = "";

        if (object instanceof CharSequence) {
            return _appendQuotedFormTo(a, (CharSequence)object, _getStrictJSON());
        } else if (object instanceof Character) {
            return _appendQuotedFormTo(a, object.toString(), _getStrictJSON());
        } else if (object instanceof Enum) {
            return _appendQuotedFormTo(a, (object instanceof ValueEnum ? ((ValueEnum)object).getValue() : object.toString()), _getStrictJSON());
        } else if (object instanceof Long) {
            final long l = ((Long)object).longValue();
            if (Math.abs(l) > 9007199254740992L) {
                SC.logWarn("Truncation of " + Long.toString(l) + " may occur when parsing the encoded JSON.");
                // TODO Add option to output a JSON string instead?
            }
            return a.append(object.toString());
        } else if (object instanceof Number) {
            // This accomplishes converting 1 to "1" rather than "1.0".
            return a.append(nativeToString(((Number)object).doubleValue()));
        } else if (object instanceof Boolean) {
            return a.append(object.toString());
        } else if (object instanceof Date) {
            return _appendQuotedFormTo(a, encodeDate((Date)object), _getStrictJSON());
        }

        final String prevPath = objPaths.get(object);
        if (prevPath != null && objPath.startsWith(prevPath)) {
            // Note: check that the first char after "prevPath" is a path separator char in order
            // to avoid false loop detection with "prop" and "prop2" having the same non-looping
            // object (since "prop2" contains "prop").
            final char nextChar = objPath.charAt(prevPath.length());
            if (nextChar == '.' || nextChar == '[' || nextChar == ']') {
                final JSONCircularReferenceMode mode = _getCircularReferenceMode();
                switch (_getCircularReferenceMode()) {
                    case MARKER:
                        return _appendQuotedFormTo(a, _getCircularReferenceMarker(), _getStrictJSON());
                    case PATH:
                        return _appendQuotedFormTo(a, _getCircularReferenceMarker() + ":" + prevPath, _getStrictJSON());
                    case OMIT:
                        return a.append("null");
                }
            }
        }

        // add the object to the list of objRefs so we can avoid an endless loop
        objPaths.put(object, objPath);

        // if there is a serialize method associated with this object, call that
        if (object instanceof CanSerialize) {
            return ((CanSerialize)object)._serializeTo(a, objPaths, objPath, prefix, this);
        }

        // handle arrays as a special case
        if (Array.isArray(object)) object = Array.asList(object);
        if (object instanceof List) {
            return _serializeListTo(a, (List<?>)object, objPath, prefix);
        }

        final Object data;
        // if the object has a getSerializeableFields, use whatever it returns, otherwise just use the object
        if (object instanceof HasSerializeableFields) {
            data = ((HasSerializeableFields)object)._getSerializableFields(new ArrayList<Object>(0), new ArrayList<Object>(0));
        } else {
            data = object;
        }

        // and return anything else as a simple object
        return _serializeObjectTo(a, data, objPath, prefix);
    }

    @SGWTInternal
    public Appendable _serializeListTo(Appendable a, List<?> list, String objPath, String prefix) throws IOException {
        a.append('[');
        final int len = list.size();
        boolean wroteOne = false;
        final boolean prettyPrint = _getPrettyPrint();
        final String innerPrefix = (prefix != null ? prefix + Comm.indent : null);
        for (int i = 0; i < len; ++i) {
            final Object value = list.get(i);
            if (wroteOne) a.append(prettyPrint ? ", " : ",");
            wroteOne = true;
            if (prettyPrint && prefix != null) a.append('\n').append(prefix).append(Comm.indent);

            final String valueObjPath = serialize_addToPath(objPath, i);
            _serializeTo(a, value, innerPrefix, valueObjPath);
        }
        if (prettyPrint && prefix != null && wroteOne) a.append('\n').append(prefix);
        a.append(']');
        return a;
    }

    @SGWTInternal
    public Appendable _serializeObjectTo(Appendable a, Object object, String objPath, String prefix) throws IOException {
        final boolean prettyPrint = _getPrettyPrint();
        final boolean strictQuoting = _getStrictQuoting();
        if (!(object instanceof Map)) {
            final String message = "unspecified error";

            a.append('{');
            if (prettyPrint) {
                a.append(strictQuoting ? " \"cantEchoObject\": " : " cantEchoObject: ");
            } else {
                a.append(strictQuoting ? "\"cantEchoObject\":" : "cantEchoObject:");
            }
            _appendQuotedFormTo(a, message, _getStrictJSON());
            return a.append(prettyPrint ? " }" : "}");
        }
        final Map<?, ?> map = (Map<?, ?>)object;
        a.append('{');
        boolean wroteOne = false;
        final String innerPrefix = (prefix != null ? prefix + Comm.indent : null);
        for (final Map.Entry<?, ?> e : map.entrySet()) {
            final Object key = e.getKey();
            if (key == null) continue;

            if (wroteOne) a.append(',');
            wroteOne = true;
            if (prettyPrint && prefix != null) a.append('\n').append(prefix).append(Comm.indent);

            final String keyStr = key.toString();
            if (strictQuoting || !Comm.SIMPLE_IDENTIFIER_RE.test(keyStr)) {
                _appendQuotedFormTo(a, keyStr, _getStrictJSON());
            } else {
                a.append(keyStr);
            }
            a.append(prettyPrint ? ": " : ":");

            final String otherObjPath = serialize_addToPath(objPath, keyStr);
            _serializeTo(a, e.getValue(), innerPrefix, otherObjPath);
        }
        a.append('}');
        return a;
    }
}
