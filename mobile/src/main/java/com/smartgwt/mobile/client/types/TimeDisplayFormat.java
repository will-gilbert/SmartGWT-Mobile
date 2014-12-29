package com.smartgwt.mobile.client.types;

import com.smartgwt.mobile.SGWTInternal;

public enum TimeDisplayFormat implements ValueEnum {

    TOTIME("toTime", true, false, false),
    TO24HOURTIME("to24HourTime", true, false, true),

    TOPADDEDTIME("toPaddedTime", true, true, false),
    TOPADDED24HOURTIME("toPadded24HourTime", true, true, true),

    TOSHORTTIME("toShortTime", false, false, false),
    TOSHORT24HOURTIME("toShort24HourTime", false, false, true),

    TOSHORTPADDEDTIME("toShortPaddedTime", false, true, false),
    TOSHORTPADDED24HOURTIME("toShortPadded24HourTime", false, true, true);

    private final String value;
    private final boolean showSeconds;
    private final boolean padded;
    private final boolean show24;

    private TimeDisplayFormat(String value, boolean showSeconds, boolean padded, boolean show24) {
        this.value = value;
        this.showSeconds = showSeconds;
        this.padded = padded;
        this.show24 = show24;
    }

    @Override
    public final String getValue() {
        return value;
    }

    @SGWTInternal
    public final boolean _getShowSeconds() {
        return showSeconds;
    }

    @SGWTInternal
    public final boolean _isPadded() {
        return padded;
    }

    @SGWTInternal
    public final boolean _getShow24() {
        return show24;
    }
}
