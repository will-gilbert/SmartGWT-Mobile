package com.smartgwt.mobile.client.util;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.types.IntValueEnum;
import com.smartgwt.mobile.client.types.DateDisplayFormat;
import com.smartgwt.mobile.client.types.ValueEnum;

public final class EnumUtil {

    public static <E extends ValueEnum> E getEnum(E[] enumValues, String value) {
        if (value == null) return null;

        for (E e : enumValues) {
            if (e.getValue().equals(value)) return e;
        }
        return null;
    }

    @SGWTInternal
    public static DateDisplayFormat _getEnum(String value) {
        if ("toUSShortDateTime".equals(value)) return DateDisplayFormat.TOUSSHORTDATETIME;
        else if ("toEuropeanShortDateTime".equals(value)) return DateDisplayFormat.TOEUROPEANSHORTDATETIME;
        else if ("toJapanShortDateTime".equals(value)) return DateDisplayFormat.TOJAPANSHORTDATETIME;
        else return EnumUtil.getEnum(DateDisplayFormat.values(), value);
    }

    @SGWTInternal
    public static <E extends IntValueEnum> E _getEnum(E[] enumValues, int code) {
        for (E e : enumValues) {
            if (e._getCode() == code) return e;
        }
        return null;
    }

    private EnumUtil() {}
}
