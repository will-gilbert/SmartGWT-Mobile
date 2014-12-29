package com.smartgwt.mobile.client.cordova.camera.types;

import com.smartgwt.mobile.client.internal.types.IntValueEnum;

public enum DestinationType implements IntValueEnum {

    BASE64_ENCODED_STRING(getCode("DATA_URL", 0)),
    FILE_URI(getCode("FILE_URI", 1));

    public static final DestinationType DATA_URL = BASE64_ENCODED_STRING;

    private static native int getCode(String value, int defaultCode) /*-{
        if ($wnd.Camera == null || $wnd.Camera.DestinationType == null) return defaultCode;
        var code = parseInt($wnd.Camera.DestinationType[value]);
        return (isNaN(code) ? defaultCode : code);
    }-*/;

    private final int code;

    private DestinationType(int code) {
        this.code = code;
    }

    @Override
    public final int _getCode() {
        return code;
    }
}
