package com.smartgwt.mobile.client.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.JsDate;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.regexp.shared.SplitResult;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.data.DataSource;
import com.smartgwt.mobile.client.i18n.SmartGwtMessages;
import com.smartgwt.mobile.client.internal.Comm;
import com.smartgwt.mobile.client.types.DateDisplayFormat;
import com.smartgwt.mobile.client.types.TimeDisplayFormat;

public final class DateUtil {

    private static final Map<String, DateTimeFormat> DATE_TIME_FORMATS = new HashMap<String, DateTimeFormat>();
    private static final Map<DateDisplayFormat, String> INPUT_FORMATS = new HashMap<DateDisplayFormat, String>();
    private static final DateTimeFormat SERIALIZEABLE_DATE_FORMAT = DateTimeFormat.getFormat("yyyy-MM-dd kk:mm:ss"),
            SERIALIZEABLE_DATE_FORMAT_XML_SCHEMA_MODE = DateTimeFormat.getFormat("yyyy-MM-dd'T'HH:mm:ss");

    public static final DateDisplayFormatter TODATESTAMP = new DateDisplayFormatter() {

        private final DateTimeFormat dtf = DateTimeFormat.getFormat("yyyyMMdd'T'HHmmss'Z'");

        @Override
        public String format(Date date) {
            return dtf.format(date, DataSource._UTC);
        }
    };

    public static final DateDisplayFormatter TOEUROPEANSHORTDATE = new DateDisplayFormatter() {
        @Override
        public String format(Date date) {
            return toShortDate(date, "DMY", false);
        }
    };

    public static final DateDisplayFormatter TOEUROPEANSHORTDATETIME = new DateDisplayFormatter() {
        @Override
        public String format(Date date) {
            return TOEUROPEANSHORTDATE.format(date) + " " + toShortTime(date);
        }
    };

    public static final DateDisplayFormatter TOJAPANSHORTDATE = new DateDisplayFormatter() {
        @Override
        public String format(Date date) {
            return toShortDate(date, "YMD", false);
        }
    };

    public static final DateDisplayFormatter TOJAPANSHORTDATETIME = new DateDisplayFormatter() {
        @Override
        public String format(Date date) {
            return TOJAPANSHORTDATE.format(date) + " " + toShortTime(date);
        }
    };

    public static final DateDisplayFormatter TOLOCALESTRING = new DateDisplayFormatter() {
        @Override
        public String format(Date date) {
            final JsDate jsD = JsDate.create((double)date.getTime());
            return jsD.toLocaleString();
        }
    };

    public static final DateDisplayFormatter TOSERIALIZEABLEDATE = new DateDisplayFormatter() {
        @Override
        public String format(Date date) {
            return SERIALIZEABLE_DATE_FORMAT.format(date);
        }
    };

    public static final DateDisplayFormatter TOSTRING = new DateDisplayFormatter() {
        @Override
        public String format(Date date) {
            final JsDate jsD = JsDate.create((double)date.getTime());
            return jsD.toString();
        }
    };

    public static final DateDisplayFormatter TOUSSHORTDATE = new DateDisplayFormatter() {
        @Override
        public String format(Date date) {
            return toShortDate(date, "MDY", false);
        }
    };

    public static final DateDisplayFormatter TOUSSHORTDATETIME = new DateDisplayFormatter() {
        @Override
        public String format(Date date) {
            return TOUSSHORTDATE.format(date) + " " + toShortTime(date);
        }
    };

    private static String inputFormat;
    private static DateDisplayFormatter normalDateDisplayFormatter;
    private static DateDisplayFormatter shortDateDisplayFormatter;
    private static DateDisplayFormatter shortDatetimeDisplayFormatter;
    private static String separator = SmartGwtMessages.INSTANCE.date_dateSeparator();
    private static DateDisplayFormat shortFormat;

    static {
        INPUT_FORMATS.put(DateDisplayFormat.TOUSSHORTDATE, "MDY");
        INPUT_FORMATS.put(DateDisplayFormat.TOUSSHORTDATETIME, "MDY");
        INPUT_FORMATS.put(DateDisplayFormat.TOEUROPEANSHORTDATE, "DMY");
        INPUT_FORMATS.put(DateDisplayFormat.TOEUROPEANSHORTDATETIME, "DMY");
        INPUT_FORMATS.put(DateDisplayFormat.TOJAPANSHORTDATE, "YMD");
        INPUT_FORMATS.put(DateDisplayFormat.TOJAPANSHORTDATETIME, "YMD");

        shortFormat = EnumUtil._getEnum(SmartGwtMessages.INSTANCE.date_shortDateFormat());
    }

    @SGWTInternal
    public static DateDisplayFormatter _getDateDisplayFormatter(DateDisplayFormat displayFormat) {
        if (displayFormat == null) {
            displayFormat = (shortFormat != null ? shortFormat : DateDisplayFormat.TOUSSHORTDATE);
        }
        switch (displayFormat) {
            case TOSTRING:
                return TOSTRING;
            case TOLOCALESTRING:
                return TOLOCALESTRING;
            case TOUSSHORTDATE:
                return TOUSSHORTDATE;
            case TOUSSHORTDATETIME:
                return TOUSSHORTDATETIME;
            case TOEUROPEANSHORTDATE:
                return TOEUROPEANSHORTDATE;
            case TOEUROPEANSHORTDATETIME:
                return TOEUROPEANSHORTDATETIME;
            case TOJAPANSHORTDATE:
                return TOJAPANSHORTDATE;
            case TOJAPANSHORTDATETIME:
                return TOJAPANSHORTDATETIME;
            case TOSERIALIZEABLEDATE:
                return TOSERIALIZEABLEDATE;
            case TODATESTAMP:
                return TODATESTAMP;
        }

        assert false;
        throw new RuntimeException();
    }

    public static String getDefaultDateSeparator() {
        return separator;
    }

    public static void setDefaultDateSeparator(String separator) {
        if ((DateUtil.separator == null && separator != null) ||
            (DateUtil.separator != null && !DateUtil.separator.equals(separator)))
        {
            DateUtil.separator = separator;
            // Need to clear `DATE_TIME_FORMAT_MAP' because the `DateTimeFormat' instances
            // were based on the old separator string.
            DATE_TIME_FORMATS.clear();
        }
    }

    public static String getInputFormat() {
        if (inputFormat != null) return inputFormat;
        return _mapDisplayFormatToInputFormat(null);
    }

    public static LogicalDate getLogicalDateOnly(Date date) {
        if (date == null) return null;
        if (date instanceof LogicalDate) return (LogicalDate)date;
        return new LogicalDate(date.getYear() + 1900, date.getMonth(), date.getDate());
    }

    public static LogicalTime getLogicalTimeOnly(Date date) {
        if (date == null) return null;
        if (date instanceof LogicalTime) return (LogicalTime)date;
        return new LogicalTime(date.getHours(), date.getMinutes(), date.getSeconds());
    }

    @SGWTInternal
    public static DateDisplayFormatter _getShortDateDisplayFormatter() {
        if (shortDateDisplayFormatter != null) return shortDateDisplayFormatter;
        return _getDateDisplayFormatter(DateUtil.shortFormat);
    }

    public static void setShortDateDisplayFormatter(DateDisplayFormatter formatter) {
        shortDateDisplayFormatter = formatter;
    }

    @SGWTInternal
    public static DateDisplayFormatter _getShortDatetimeDisplayFormatter() {
        if (shortDatetimeDisplayFormatter != null) return shortDatetimeDisplayFormatter;
        DateDisplayFormat datetimeFormatter = DateUtil.shortFormat;
        if (datetimeFormatter == null) datetimeFormatter = DateDisplayFormat.TOUSSHORTDATETIME;
        else {
            if (datetimeFormatter == DateDisplayFormat.TOEUROPEANSHORTDATE) {
                datetimeFormatter = DateDisplayFormat.TOEUROPEANSHORTDATETIME;
            } else if (datetimeFormatter == DateDisplayFormat.TOJAPANSHORTDATE) {
                datetimeFormatter = DateDisplayFormat.TOJAPANSHORTDATETIME;
            } else if (datetimeFormatter == DateDisplayFormat.TOUSSHORTDATE) {
                datetimeFormatter = DateDisplayFormat.TOUSSHORTDATETIME;
            }
        }
        return _getDateDisplayFormatter(datetimeFormatter);
    }

    public static void setShortDatetimeDisplayFormatter(DateDisplayFormatter formatter) {
        shortDatetimeDisplayFormatter = formatter;
    }

    public static Date combineLogicalDateAndTime(LogicalDate date, LogicalTime time) {
        // TODO custom timezone

        return new Date(date.getYear(), date.getMonth(), date.getDate(), time.getHours(), time.getMinutes(), time.getSeconds());
    }

    public static int compareDates(Date a, Date b) {
        final long aval = (a != null ? a.getTime() : 0L),
                bval = (b != null ? b.getTime() : 0L);
        return (aval > bval ? -1 : (bval > aval ? 1 : 0));
    }

    private static String escapeGWTDateTimeFormatPattern(String text) {
        if (text == null) return "";
        return "'" + text.replace("'", "''") + "'";
    }

    public static String format(Date date) {
        if (normalDateDisplayFormatter != null) {
            return normalDateDisplayFormatter.format(date);
        }
        return null;
    }

    public static String formatAsNormalDate(Date date) {
        if (normalDateDisplayFormatter != null) {
            return normalDateDisplayFormatter.format(date);
        }
        return null;
    }

    public static String formatAsShortDate(Date date) {
        if (shortDateDisplayFormatter != null) {
            return shortDateDisplayFormatter.format(date);
        }
        return null;
    }

    public static String formatAsShortDatetime(Date date) {
        if (shortDatetimeDisplayFormatter != null) {
            return shortDatetimeDisplayFormatter.format(date);
        }
        return null;
    }

    @SGWTInternal
    public static String _mapDisplayFormatToInputFormat(DateDisplayFormat displayFormat) {
        if (displayFormat == null) {
            displayFormat = shortFormat;
        }

        String inputFormat = INPUT_FORMATS.get(displayFormat);
        if (inputFormat != null) return inputFormat;

        SC.logWarn("Unable to determine input format associated with display format " +
                displayFormat.getValue() + " - returning default input format");
        if (DateUtil.inputFormat != null) return DateUtil.inputFormat;
        return "MDY";
    }

    public static LogicalDate parseInput(String dateString) {
        if (dateString == null) return null;

        // If for some reason there is a time component, remove it.
        final int tPos = dateString.indexOf('T');
        if (tPos >= 0) dateString = dateString.substring(0, tPos);

        final RegExp re = RegExp.compile("\\s*[-. ]\\s*");
        final SplitResult parts = re.split(dateString.trim());
        if (parts == null || parts.length() != 3) return null;
        int year, month, date;
        try {
            year = Integer.parseInt(parts.get(0), 10);
            month = Integer.parseInt(parts.get(1), 10);
            date = Integer.parseInt(parts.get(2), 10);
        } catch (NumberFormatException ex) {
            return null;
        }
        month = Math.max(0, Math.min(month - 1, 11));
        date = Math.max(1, Math.min(date, 31));
        return new LogicalDate(year, month, date);
    }

    @SGWTInternal
    public static String _serialize(Date d) {
        if (Comm.legacyJSMode) {
            return "\"" + _toDBDate(d) + "\"";
        } else {
            return "new Date(" + d.getTime() + ")";
        }
    }

    @SGWTInternal
    public static String _toDBDate(Date d) {
        return "$$DATE$$:" + _toSerializeableDate(d);
    }

    @SGWTInternal
    public static String _toSchemaDate(Date d) {
        return _toSchemaDate(d, null);
    }

    @SGWTInternal
    public static String _toSchemaDate(Date d, String logicalType) {
        if ("date".equals(logicalType) || d instanceof LogicalDate) {
            return LogicalDate._toString(d);
        }

        if (!DataSource._serializeTimeAsDatetime &&
            ("time".equals(logicalType) || d instanceof LogicalTime))
        {
            return LogicalTime._toString(d);
        }

        return SERIALIZEABLE_DATE_FORMAT_XML_SCHEMA_MODE.format(d, DataSource._UTC);
    }

    @SGWTInternal
    public static String _toSerializeableDate(Date d) {
        final DateTimeFormat dtf = (Comm.xmlSchemaMode ? SERIALIZEABLE_DATE_FORMAT_XML_SCHEMA_MODE
                                                       : SERIALIZEABLE_DATE_FORMAT);
        return dtf.format(d);
    }

    // format - "MDY" / "DMY" / etc.
    private static String toShortDate(Date date, String format, boolean useCustomTimezone) {
        DateTimeFormat dtf = DATE_TIME_FORMATS.get(format);
        if (dtf == null) {
            final String escapedSeparatorText = escapeGWTDateTimeFormatPattern(separator);
            final String pattern;
            if ("DMY".equals(format)) {
                pattern = "d" + escapedSeparatorText + "M" + escapedSeparatorText + "yyyy";
            } else if ("YMD".equals(format)) {
                pattern = "yyyy" + escapedSeparatorText + "M" + escapedSeparatorText + "d";
            } else {
                assert "MDY".equals(format) : "Unsupported input format '" + format + "'";
                pattern = "M" + escapedSeparatorText + "d" + escapedSeparatorText + "yyyy";
            }
            dtf = DateTimeFormat.getFormat(pattern);
            DATE_TIME_FORMATS.put(format, dtf);
        }
        assert dtf != null;

        // TODO custom timezone

        return dtf.format(date);
    }

    private static String toShortTime(Date date) {
        return TimeUtil._toShortTime(date, TimeDisplayFormat.TOSHORTPADDED24HOURTIME, null);
    }

    private DateUtil() {}
}
