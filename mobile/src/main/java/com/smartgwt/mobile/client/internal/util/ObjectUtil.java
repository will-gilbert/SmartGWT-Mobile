package com.smartgwt.mobile.client.internal.util;

import com.google.gwt.regexp.shared.RegExp;
import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public class ObjectUtil {

    private static final RegExp LEADING_ZEROES_RE = RegExp.compile("^([-+]?)\\s*0+");
    public static double toNumber(String str) {
        if (str == null) return Double.NaN;
        str = LEADING_ZEROES_RE.replace(str.trim(), "$1");
        if (str.isEmpty()) return 0.0;
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException ex) {
            return Double.NaN;
        }
    }

    /**
     * Performs the Java equivalent of the JavaScript abstract equality comparison algorithm.
     * @see &#167;11.9.3, ECMA-262 5.1 Edition / June 2011
     */
    public static boolean abstractCompareValues(Object x, Object y) {
        if (x == null) return (y == null);
        if (y == null) return false;

        if (x instanceof Number) {
            if (y instanceof Number) {
                // TODO Support long values?

                // assert !(Double.NaN == Double.NaN);
                // +0 == -0 in Java, too:
                //     final double posZero = Double.longBitsToDouble(0x0000000000000000L),
                //         negZero = Double.longBitsToDouble(0x8000000000000000L);
                //     System.out.println(posZero);
                //     System.out.println(negZero);
                //     System.out.println(posZero == negZero);
                // Results in:
                // 0.0
                // -0.0
                // true
                // http://ideone.com/YEUON7
                return (((Number)x).doubleValue() == ((Number)y).doubleValue());
            } else if (y instanceof CharSequence) {
                return abstractCompareValues(x, toNumber(y.toString()));
            } else if (y instanceof Character) {
                return abstractCompareValues(x, toNumber(Character.toString(((Character)y).charValue())));
            }
        } else if (x instanceof CharSequence) {
            if (y instanceof CharSequence) {
                return x.toString().equals(y.toString());
            } else if (y instanceof Character) {
                final CharSequence xSeq = (CharSequence)x;
                return (xSeq.length() == 1 && xSeq.charAt(0) == ((Character)y).charValue());
            } else if (y instanceof Number) {
                return abstractCompareValues(toNumber(x.toString()), y);
            }
        } else if (x instanceof Boolean) {
            if (y instanceof Boolean) {
                return (((Boolean)x).booleanValue() == ((Boolean)y).booleanValue());
            } else {
                return abstractCompareValues(Double.valueOf(((Boolean)x).booleanValue() ? 1 : 0), y);
            }
        } else if (x instanceof Character) {
            if (y instanceof Character) {
                return (((Character)x).charValue() == ((Character)y).charValue());
            } else if (y instanceof CharSequence) {
                final CharSequence ySeq = (CharSequence)y;
                return (ySeq.length() == 1 && ySeq.charAt(0) == ((Character)x).charValue());
            } else {
                return abstractCompareValues(Character.toString(((Character)x).charValue()), y);
            }
        }

        if (y instanceof Boolean) {
            return abstractCompareValues(x, Double.valueOf(((Boolean)y).booleanValue() ? 1 : 0));
        }

        return (x == y);
    }

    public static Object normalize(Object obj) {
        if (obj == null) return null;
        if (obj instanceof String) return obj;
        if (obj instanceof CharSequence || obj instanceof Character) return obj.toString();
        if (obj instanceof Number) return normalize((Number)obj);
        return obj;
    }

    public static Number normalize(Number num) {
        if (num == null) return null;
        if (num instanceof Double) return num;
        if (num instanceof Float) return Double.valueOf(num.doubleValue());
        else {
            final long l = num.longValue();
            if (Integer.MIN_VALUE <= l && l <= Integer.MAX_VALUE) return Integer.valueOf((int)l);
            else {
                assert -9007199254740992L <= l && l <= 9007199254740992L : "Long values outside of the range [-2^53, 2^53] are not supported.";
                return Long.valueOf(l);
            }
        }
    }
}
