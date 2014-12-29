package com.smartgwt.mobile.client.internal.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.json.JSONSerializer;
import com.smartgwt.mobile.client.types.ValueEnum;

@SGWTInternal
public class URIBuilder {

    private String uri;
    transient int qsStartPos;

    public URIBuilder(String uri) {
        this.uri = uri == null ? "" : uri;
        this.qsStartPos = getQsStartPos();
    }

    private final int getQsStartPos() {
        int hashStartPos = uri.indexOf('#');
        if (hashStartPos == -1) {
            return uri.indexOf('?');
        } else {
            int qsStartPos = uri.indexOf('?');
            if (qsStartPos >= hashStartPos) {
                return -1;
            }
            return qsStartPos;
        }
    }

    public void appendPath(String path) {
        if (path.length() == 0) return;

        final String encodedPath = URL.encode(path).replace("?", "%3F").replace("#", "%23");

        if (uri.length() == 0) {
            uri = encodedPath;
            assert getQsStartPos() == -1;
            assert qsStartPos == -1;
            assert getQsStartPos() == qsStartPos;
        } else {
            int pathEndPos = qsStartPos;
            if (pathEndPos == -1) {
                pathEndPos = uri.indexOf('#');
            }
            if (pathEndPos == -1) {
                pathEndPos = uri.length();
            }

            String tmp = uri.substring(0, pathEndPos);
            if (pathEndPos > 0 && uri.charAt(pathEndPos - 1) != '/' && path.charAt(0) != '/') {
                tmp += '/';
            }
            tmp += encodedPath;
            tmp += uri.substring(pathEndPos);
            uri = tmp;
            qsStartPos = getQsStartPos();
        }
    }

    private int indexOfQueryParam(String encodedName) {
        return indexOfQueryParam(encodedName, qsStartPos);
    }
    private int indexOfQueryParam(String encodedName, int pos) {
        if (qsStartPos == -1) return -1;

        final int hashStartPos = uri.indexOf('#', qsStartPos + 1);
        final int qsEndPos = hashStartPos == -1 ? uri.length() : hashStartPos;
        for (; pos < qsEndPos && (pos = uri.indexOf(encodedName, pos)) != -1; pos += encodedName.length()) {
            assert pos >= 1;
            if (uri.charAt(pos - 1) == '&' || uri.charAt(pos - 1) == '?') {
                int pos2 = pos + encodedName.length();
                if (pos2 <= qsEndPos && (pos2 == qsEndPos ||
                                         uri.charAt(pos2) == '=' ||
                                         uri.charAt(pos2) == '&'))
                {
                    return pos;
                }
            }
        }
        return -1;
    }

    public final boolean containsQueryParam(String name) {
        return name != null && indexOfQueryParam(URL.encodeQueryString(name)) != -1;
    }

    private void appendQueryParamHelper(final String prefix, Object value, boolean strictJSON, boolean explodeLists, DateTimeFormat datetimeFormat) {
        if (value == null) return;
        if (value instanceof CharSequence) {
            int hashStartPos = uri.indexOf('#', qsStartPos == -1 ? 0 : qsStartPos + 1);
            if (hashStartPos == -1) hashStartPos = uri.length();

            String tmp = uri.substring(0, hashStartPos);

            if (qsStartPos == -1) {
                qsStartPos = hashStartPos;
                tmp += '?';
            } else tmp += '&';
            tmp += prefix;
            tmp += URL.encodeQueryString(value.toString());
            tmp += uri.substring(hashStartPos);
            uri = tmp;
            assert getQsStartPos() == qsStartPos;
        } else if (value instanceof Object[]) {
            if (explodeLists) {
                for (Object val : (Object[])value) {
                    appendQueryParamHelper(prefix, val, strictJSON, false, datetimeFormat);
                }
            } else {
                appendQueryParamHelper(prefix, Arrays.asList((Object[])value), strictJSON, false, datetimeFormat);
            }
        } else if (explodeLists && value instanceof List) {
            List<?> l = (List<?>)value;
            for (Object val : l) {
                appendQueryParamHelper(prefix, val, strictJSON, false, datetimeFormat);
            }
        } else if (value instanceof ValueEnum) {
            appendQueryParamHelper(prefix, ((ValueEnum)value).getValue(), strictJSON, false, datetimeFormat);
        } else if (value instanceof Map || value instanceof List) {
            // As JSONSerializer supports more types, for best compatibility with SmartClient,
            // instances of these types should be serialized with JSONSerializer rather than
            // being converted to a String via toString().
            // See the SC documentation for RPCRequest.params:
            // http://www.smartclient.com/docs/8.2/a/b/c/go.html#attr..RPCRequest.params
            // "Any non-atomic type, such as an Object, will be serialized to JSON"
            appendQueryParamHelper(prefix, JSONSerializer._serialize(value, strictJSON, null), strictJSON, false, datetimeFormat);
        } else {
            appendQueryParamHelper(prefix, value.toString(), strictJSON, false, datetimeFormat);
        }
    }

    // For convenience. If the value is a `CharSequence' object (e.g. `String') then the `strictJSON'
    // and `datetimeFormat' options are irrelevant.
    public void appendQueryParam(String name, CharSequence value) {
        appendQueryParam(name, value, false, null);
    }

    public void appendQueryParam(String name, Object value, boolean strictJSON, DateTimeFormat datetimeFormat) {
        appendQueryParam(name, value, strictJSON, true, datetimeFormat);
    }

    public void appendQueryParam(String name, Object value, boolean strictJSON, boolean explodeLists, DateTimeFormat datetimeFormat) {
        if (name == null) return;

        final String encodedName = URL.encodeQueryString(name);
        final String prefix = encodedName + '=';

        appendQueryParamHelper(prefix, value, strictJSON, explodeLists, datetimeFormat);
    }

    // For convenience. If the value is a `CharSequence' object (e.g. `String') then the `strictJSON'
    // and `datetimeFormat' options are irrelevant.
    public void setQueryParam(String name, CharSequence value) {
        setQueryParam(name, value, false, null);
    }

    public void setQueryParam(String name, Object value, boolean strictJSON, DateTimeFormat datetimeFormat) {
        setQueryParam(name, value, strictJSON, true, datetimeFormat);
    }

    public void setQueryParam(String name, Object value, boolean strictJSON, boolean explodeLists, DateTimeFormat datetimeFormat) {
        final String encodedName = URL.encodeQueryString(name);
        final String prefix = encodedName + '=';

        if (qsStartPos != -1) {
            final int hashStartPos = uri.indexOf('#', qsStartPos + 1);
            final int qsEndPos = hashStartPos == -1 ? uri.length() : hashStartPos;
            StringBuilder sb = new StringBuilder();
            sb.append(uri, 0, qsStartPos);
            int prevPos = qsStartPos, pos = qsStartPos;
            while (pos < qsEndPos && (pos = uri.indexOf(prefix, pos)) != -1) {
                assert pos >= 1;
                int ampPos = uri.indexOf('&', pos + prefix.length());

                if (uri.charAt(pos - 1) == '&' || uri.charAt(pos - 1) == '?') {
                    sb.append(uri, prevPos, pos);
                    if (ampPos != -1 && ampPos < qsEndPos) {
                        pos = ampPos + 1;
                    } else {
                        pos = qsEndPos;
                        sb.deleteCharAt(sb.length() - 1);
                    }
                } else {
                    pos = (ampPos != -1 && ampPos < qsEndPos ? ampPos + 1 : qsEndPos);
                    sb.append(uri, prevPos, pos);
                }
                prevPos = pos;
            }
            sb.append(uri, prevPos, uri.length());
            uri = sb.toString();
            qsStartPos = getQsStartPos();
        }

        appendQueryParam(name, value, strictJSON, explodeLists, datetimeFormat);
    }

    @Override
    public String toString() {
        return uri;
    }

    @Override
    public int hashCode() {
        return uri.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || ! (other instanceof URIBuilder)) {
            return false;
        }
        return uri.equals(((URIBuilder)other).uri);
    }
}
