package com.smartgwt.mobile.client.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.regexp.shared.SplitResult;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.types.TimeDisplayFormat;

public final class TimeUtil {

    private static final Map<TimeDisplayFormat, DateTimeFormat> DATE_TIME_FORMATS = new HashMap<TimeDisplayFormat, DateTimeFormat>();

    private static TimeDisplayFormat displayFormat = TimeDisplayFormat.TOTIME;
    private static TimeDisplayFormat shortDisplayFormat = TimeDisplayFormat.TOSHORTTIME;

    @SGWTInternal
    public static String _format(Date date, TimeDisplayFormat format, boolean shortFormat, Boolean logicalTime) {
        if (date == null) return null;

        if (format == null) {
            format = shortFormat ? TimeUtil.shortDisplayFormat : TimeUtil.displayFormat;
            if (format == null) format = shortFormat ? TimeDisplayFormat.TOSHORTTIME : TimeDisplayFormat.TOTIME;
        }

        assert format != null;
        final boolean showSeconds = format._getShowSeconds(),
                padded = format._isPadded(),
                show24 = format._getShow24();

        final boolean useCustomTimezone;

        if (logicalTime != null) useCustomTimezone = !logicalTime.booleanValue();
        else useCustomTimezone = !(date instanceof LogicalTime) && !(date instanceof LogicalDate);

        DateTimeFormat dtf = DATE_TIME_FORMATS.get(format);
        if (dtf == null) {
            String pattern = show24 ? "k" : "h";
            if (padded) pattern += pattern;

            pattern += ":mm";

            if (showSeconds) pattern += ":ss";

            if (show24) pattern += " a";

            dtf = DateTimeFormat.getFormat(pattern);
            DATE_TIME_FORMATS.put(format, dtf);
        }
        assert dtf != null;

        // TODO custom timezone

        return dtf.format(date);
    }

    @SGWTInternal
    public static LogicalTime _parseInput(String timeString) {
        if (timeString == null) return null;

        // Remove fractional seconds if present.
        RegExp re = RegExp.compile("\\s*\\.\\s*\\d+$");
        timeString = re.replace(timeString.trim(), "");

        re = RegExp.compile("\\s*[: ]\\s*");
        final SplitResult parts = re.split(timeString);
        if (parts == null || parts.length() == 0 || 3 < parts.length()) return null;
        int hrs, min = 0, sec = 0;
        try {
            hrs = Integer.parseInt(parts.get(0), 10);
            if (parts.length() > 1) {
                min = Integer.parseInt(parts.get(1), 10);
                if (parts.length() > 2) {
                    sec = Integer.parseInt(parts.get(2), 10);
                }
            }
        } catch (NumberFormatException ex) {
            return null;
        }
        if (hrs == 24) hrs = 0;
        else hrs = Math.max(0, Math.min(hrs, 23));
        min = Math.max(0, Math.min(min, 59));
        sec = Math.max(0, Math.min(sec, 59));
        return new LogicalTime(hrs, min, sec);
    }

    @SGWTInternal
    public static String _toShortTime(Date date, TimeDisplayFormat format, Boolean logicalTime) {
        return _format(date, format, true, logicalTime);
    }

    private TimeUtil() {}
}
