package com.smartgwt.mobile.client.util;

import static com.smartgwt.mobile.client.data.Storage.LOCAL_STORAGE;

import java.util.Date;
import java.util.Map;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.Storage.QuotaExceededException;

public class Offline {

    private static final String KEY_PREFIX = "isc-";

    private static String makeKey(String key) {
        return KEY_PREFIX + (key == null ? "null" : "\"" + key);
    }

    public static final Boolean isOffline() {
        return Boolean.FALSE;
    }

    private static String getOriginalKey(String key) {
        if (key != null && key.startsWith(KEY_PREFIX)) {
            if (key.length() > KEY_PREFIX.length()) {
                if (key.charAt(KEY_PREFIX.length()) == '"') {
                    key = key.substring(KEY_PREFIX.length() + 1);
                } else {
                    key = key.substring(KEY_PREFIX.length());
                    if ("null".equals(key)) {
                        return null;
                    }
                }
            }
        }
        return key;
    }

    public static Object toJava(final JSONValue value) {
        if (value == null || value.isNull() != null) {
            return null;
        }

        JSONString jsonStr = value.isString();
        if (jsonStr != null) {
            return jsonStr.stringValue();
        }

        JSONNumber jsonNum = value.isNumber();
        if (jsonNum != null) {
            double d = jsonNum.doubleValue();
            if (d == Math.floor(d)) {
                return Integer.valueOf((int)d);
            } else {
                return Double.valueOf(d);
            }
        }

        JSONBoolean jsonBool = value.isBoolean();
        if (jsonBool != null) {
            return Boolean.valueOf(jsonBool.booleanValue());
        }

        JSONArray jsonArr = value.isArray();
        if (jsonArr != null) {
            Object[] arr = new Object[jsonArr.size()];
            for (int i = 0; i < arr.length; ++i) {
                arr[i] = toJava(jsonArr.get(i));
            }
            return arr;
        }

        JSONObject jsonObj = value.isObject();
        if (jsonObj != null) {
            final JSONValue classValue = jsonObj.get("class");

            if (classValue != null) {
                jsonStr = classValue.isString();
                if (jsonStr == null) {
                    SC.logWarn("Offline.toJava(): No class key was found.");
                } else {
                    final String cls = jsonStr.stringValue();

                    if ("com.smartgwt.mobile.client.data.Record".equals(cls)) {
                        final JSONValue $Value = jsonObj.get("$");
                        if ($Value != null) {
                            jsonObj = $Value.isObject();

                            if (jsonObj != null) {
                                final Record record = new Record();
                                for (String key : jsonObj.keySet()) {
                                    record.put(key, toJava(jsonObj.get(key)));
                                }
                                return record;
                            }
                        }
                    } else if ("java.util.Date".equals(cls)) {
                        final JSONValue timeValue = jsonObj.get("time");
                        if (timeValue != null) {
                            jsonStr = timeValue.isString();
                            try {
                                long time = Long.parseLong(jsonStr.stringValue());
                                return new Date(time);
                            } catch (NumberFormatException ex) {
                                SC.logWarn("Offline.toJava(): Failed to parse the time value '" + jsonStr.stringValue() + "': " + ex.getMessage());
                            }
                        }
                    } else {
                        SC.logWarn("Offline.toJava(): Unsupported class `" + cls + "`");
                    }
                }
            }
        }

        return null;
    }

    public static JSONValue toJSONValue(final Object value) {
        if (value == null) {
            return JSONNull.getInstance();
        }

        if (value instanceof String) {
            String str = (String)value;
            return new JSONString(str);
        }

        if (value instanceof Number) {
            Number num = (Number)value;
            return new JSONNumber(num.doubleValue());
        }

        if (value instanceof Boolean) {
            Boolean bool = (Boolean)value;
            return JSONBoolean.getInstance(bool.booleanValue());
        }

        JSONObject ret = new JSONObject();
        final String cls = value.getClass().getName();
        ret.put("class", new JSONString(cls));
        if (value instanceof Record) {
            JSONObject jsonObj = new JSONObject();
            for (Map.Entry<String, Object> e : ((Record)value).entrySet()) {
                jsonObj.put(e.getKey(), toJSONValue(e.getValue()));
            }
            ret.put("$", jsonObj);
        } else if (value instanceof Date) {
            ret.put("time", new JSONString(Long.toString(((Date)value).getTime())));
        } else {
            SC.logWarn("Offline.toJSONValue(): Unsupported class `" + cls + "`");
        }

        return ret;
    }

    public static String toJSON(Object value) {
        return toJSONValue(value).toString();
    }

    public static Object fromJSON(String json) {
        JSONValue value = JSONParser.parseStrict(json);
        return toJava(value);
    }

    public static Object get(String key) {
        String json = LOCAL_STORAGE.get(makeKey(key));
        if (json == null) {
            return null;
        }
        return fromJSON(json);
    }

    public static void put(String key, Object value) {
        put(key, value, true);
    }

    public static void put(String key, Object value, boolean recycleValues) {
        put(key, toJSONValue(value), recycleValues);
    }

    private static void put(String key, JSONValue value, boolean recycleValues) {
        assert value != null;
        String json = value.toString();

        do {
            try {
                LOCAL_STORAGE.put(makeKey(key), json);
            } catch (QuotaExceededException ex) {
                // TODO
            }

            break;
        } while (true);
    }

    private Offline() {
    }
}
